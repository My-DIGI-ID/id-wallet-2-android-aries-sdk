package com.digital_enabling.android_aries_sdk.credential

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.configuration.AgentOptions
import com.digital_enabling.android_aries_sdk.configuration.IProvisioningService
import com.digital_enabling.android_aries_sdk.credential.abstractions.ISchemaService
import com.digital_enabling.android_aries_sdk.credential.abstractions.ITailsService
import com.digital_enabling.android_aries_sdk.credential.models.CredentialDefinitionConfiguration
import com.digital_enabling.android_aries_sdk.credential.records.DefinitionRecord
import com.digital_enabling.android_aries_sdk.credential.records.RevocationRegistryRecord
import com.digital_enabling.android_aries_sdk.credential.records.SchemaRecord
import com.digital_enabling.android_aries_sdk.ledger.abstractions.ILedgerService
import com.digital_enabling.android_aries_sdk.ledger.models.TransactionTypes
import com.digital_enabling.android_aries_sdk.payments.IPaymentService
import com.digital_enabling.android_aries_sdk.utils.IIndyWrapper
import com.digital_enabling.android_aries_sdk.utils.IndyWrapper
import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.wallet.IWalletRecordService
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.hyperledger.indy.sdk.anoncreds.AnoncredsResults
import org.hyperledger.indy.sdk.wallet.Wallet

class DefaultSchemaService(
    /**
     * The provisioning service
     */
    private val provisioningService: IProvisioningService,
    /**
     * The record service
     */
    private val recordService: IWalletRecordService,
    /**
     * The ledger service
     */
    private val ledgerService: ILedgerService,
    /**
     * The payment service
     */
    private val paymentService: IPaymentService,
    /**
     * The tails service
     */
    private val tailsService: ITailsService,
    /**
     * The agent options
     */
    private val agentOptions: AgentOptions,
    private val indyWrapper: IIndyWrapper = IndyWrapper()
) : ISchemaService {
    override suspend fun createSchema(
        agentContext: IAgentContext,
        issuerDid: String,
        name: String,
        version: String,
        attributeNames: List<String>
    ): String {
        val schema = indyWrapper.issuerCreateSchema(
            issuerDid,
            name,
            version,
            Json.encodeToString(attributeNames)
        )
        val schemaRecord = SchemaRecord(schema.schemaId)
        schemaRecord.name = name
        schemaRecord.version = version
        schemaRecord.attributeNames = attributeNames

        val paymentInfo = paymentService.getTransactionCost(agentContext, TransactionTypes.SCHEMA)

        ledgerService.registerSchema(agentContext, issuerDid, schema.schemaJson, paymentInfo)

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        runBlocking { recordService.add(wallet, schemaRecord) }
        runBlocking { recordService.update(wallet, paymentInfo.paymentAddress) }

        return schemaRecord.id
    }

    /**
     * @see ISchemaService
     */
    override suspend fun createSchema(
        agentContext: IAgentContext,
        name: String,
        version: String,
        attributeNames: List<String>
    ): String {
        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        val provisioning = provisioningService.getProvisioning(wallet)

        val issuerDid = provisioning.issuerDid ?: throw AriesFrameworkException(
            ErrorCode.INVALID_RECORD_DATA,
            "ProvisioningRecord has no issuerDid"
        )

        return createSchema(agentContext, issuerDid, name, version, attributeNames)
    }

    /**
     * @see ISchemaService
     */
    override suspend fun createCredentialDefinition(
        agentContext: IAgentContext,
        configuration: CredentialDefinitionConfiguration
    ): String {
        if (configuration.enableRevocation and configuration.revocationRegistryBaseUri.isEmpty() and agentOptions.revocationRegistryUriPath.isEmpty()) {
            throw AriesFrameworkException(
                ErrorCode.INVALID_PARAMETER_FORMAT,
                "RevocationRegistryBaseUri must be specified either in the configuration or the AgentOptions"
            )
        }

        val schema = ledgerService.lookupSchema(agentContext, configuration.schemaId)

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        val credentialDefinition = indyWrapper.issuerCreateAndStoreCredentialDef(
            wallet,
            configuration.issuerDid,
            schema.objectJson,
            configuration.tag,
            null,
            "{\"support_revocation\": \"${configuration.enableRevocation}\"}"
        )

        val definitionRecord = DefinitionRecord(credentialDefinition.credDefId)
        definitionRecord.issuerDid = configuration.issuerDid

        ledgerService.registerCredentialDefinition(
            agentContext,
            configuration.issuerDid,
            credentialDefinition.credDefJson,
            null
        )

        definitionRecord.supportsRevocation = configuration.enableRevocation
        definitionRecord.schemaId = configuration.schemaId

        if (configuration.enableRevocation) {
            definitionRecord.maxCredentialCount = configuration.revocationRegistrySize
            definitionRecord.revocationAutoScale = configuration.revocationRegistryAutoScale
            val revocationRecord = createRevocationRegistry(
                agentContext,
                "1-${configuration.revocationRegistrySize}",
                definitionRecord
            ).second
            definitionRecord.currentRevocationRegistryId = revocationRecord.id
        }
        runBlocking { recordService.add(wallet, definitionRecord) }

        return credentialDefinition.credDefId
    }

    /**
     * @see ISchemaService
     */
    override suspend fun createRevocationRegistry(
        agentContext: IAgentContext,
        tag: String,
        definitionRecord: DefinitionRecord
    ): Pair<AnoncredsResults.IssuerCreateAndStoreRevocRegResult, RevocationRegistryRecord> {
        val tailsHandle = runBlocking {
            tailsService.createTails()
        }
        val revocationRegistryDefinitionJson =
            "{issuance_type=\"ISSUANCE_BY_DEFAULT\", max_cred_num=${definitionRecord.maxCredentialCount}}"

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        val revocationRegistry = indyWrapper.issuerCreateAndStoreRevocReg(
            wallet,
            definitionRecord.issuerDid,
            null,
            tag,
            definitionRecord.id,
            revocationRegistryDefinitionJson,
            tailsHandle
        )
        val revocationRecord = RevocationRegistryRecord(revocationRegistry.revRegId)
        revocationRecord.credentialDefinitionId = definitionRecord.id

        var revocationDefinition =
            Json.parseToJsonElement(revocationRegistry.revRegDefJson) as JsonObject
        val value = revocationDefinition["value"] as JsonObject
        val tailsfile =
            value["tailsLocation"].toString()
        val tailsLocation =
            "${agentOptions.endpointUri}/${agentOptions.revocationRegistryUriPath}/${tailsfile}"

        val valueMap = (value as Map<String, JsonElement>).toMutableMap()
        valueMap["tailsLocation"] = Json.parseToJsonElement(tailsLocation)

        val revocationDefinitionMap = (revocationDefinition as Map<String, JsonElement>).toMutableMap()
        revocationDefinitionMap["value"] = JsonObject(valueMap)

        revocationDefinition = JsonObject(revocationDefinitionMap)

        revocationRecord.tailsFile = tailsfile
        revocationRecord.tailsLocation = tailsLocation

        runBlocking { recordService.add(wallet, revocationRecord) }

        val issuerDid = definitionRecord.issuerDid ?: throw AriesFrameworkException(
            ErrorCode.INVALID_RECORD_DATA,
            "DefinitionRecord has no issuerDid"
        )

        ledgerService.registerRevocationRegistryDefinition(
            agentContext,
            issuerDid,
            revocationDefinition.toString(),
            null
        )

        recordService.add(wallet, revocationRecord)

        ledgerService.sendRevocationRegistryEntry(
            agentContext,
            issuerDid,
            revocationRegistry.revRegId,
            "CL_ACCUM",
            revocationRegistry.revRegEntryJson,
            null
        )

        return Pair(revocationRegistry, revocationRecord)
    }

    /**
     * @see ISchemaService
     */
    override fun listSchemas(wallet: Wallet): List<SchemaRecord> {
        var ret: List<SchemaRecord>
        runBlocking {
            ret = recordService.search(
                wallet,
                null,
                null,
                100,
                recordBaseType = RecordType.SCHEMA_RECORD
            )
        }
        return ret
    }

    /**
     * @see ISchemaService
     */
    override fun listCredentialDefinitions(wallet: Wallet): List<DefinitionRecord> {
        val list: List<DefinitionRecord>
        runBlocking {
            list = recordService.search(
                wallet,
                null,
                null,
                100,
                0,
                RecordType.CREDENTIAL_DEFINITION_RECORD
            )
        }

        return list
    }

    /**
     * @see ISchemaService
     */
    override fun getCredentialDefinition(
        wallet: Wallet,
        credentialDefinitionId: String
    ): DefinitionRecord {
        val record: DefinitionRecord?
        runBlocking {
            record = recordService.get(
                wallet,
                credentialDefinitionId,
                RecordType.CREDENTIAL_DEFINITION_RECORD
            )
        }

        return record ?: throw RuntimeException("Unable to get credential definition record")
    }

    /**
     * @see ISchemaService
     */
    override fun lookupCredentialDefinition(
        context: IAgentContext,
        definitionId: String
    ): String {
        val result = ledgerService.lookupDefinition(context, definitionId)
        return result.objectJson
    }

    /**
     * @see ISchemaService
     */
    override fun lookupSchemaFromCredentialDefinition(
        context: IAgentContext,
        credentialDefinitionId: String
    ): String? {
        val credDef = lookupCredentialDefinition(context, credentialDefinitionId)
        if (credDef.isEmpty()) {
            return null
        }

        try {
            val schemaSequenceId =
                (Json.encodeToJsonElement(credDef) as JsonObject)["schemaId"].toString().toInt()
            return lookupSchema(context, schemaSequenceId)
        } catch (e: Exception) {
        }
        return null
    }

    /**
     * @see ISchemaService
     */
    override fun lookupSchema(context: IAgentContext, sequenceId: Int): String? {
        val result = ledgerService.lookupTransaction(context, null, sequenceId)
        if (result.isNotEmpty()) {
            try {
                val txnData =
                    (((((Json.encodeToJsonElement(result) as JsonObject)["result"] as JsonObject)["data"] as JsonObject)["txn"] as JsonObject)["data"] as JsonObject)["data"] as JsonObject
                val txnId =
                    ((((Json.encodeToJsonElement(result) as JsonObject)["result"] as JsonObject)["data"] as JsonObject)["txnMetadata"] as JsonObject)["txnId"].toString()
                val seperator: Int = txnId.lastIndexOf(":")
                val ver = txnId.substring(seperator + 1, txnId.length - seperator - 1)

                val newMap = HashMap<String, JsonElement>()
                txnData.forEach { x -> newMap[x.key] = x.value }
                newMap["id"] = Json.encodeToJsonElement(txnId)
                newMap["ver"] = Json.encodeToJsonElement(ver)
                newMap["seqNo"] = Json.encodeToJsonElement(sequenceId)

                return JsonObject(newMap).toString()
            } catch (e: Exception) {

            }
        }
        return null
    }

    /**
     * @see ISchemaService
     */
    override fun lookupSchema(context: IAgentContext, schemaId: String): String {
        val result = ledgerService.lookupSchema(context, schemaId)
        return (Json.encodeToJsonElement(result) as JsonObject).toString()
    }
}