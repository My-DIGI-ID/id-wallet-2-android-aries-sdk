package com.digital_enabling.android_aries_sdk.credential

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.common.IEventAggregator
import com.digital_enabling.android_aries_sdk.common.ServiceMessageProcessingEvent
import com.digital_enabling.android_aries_sdk.configuration.IProvisioningService
import com.digital_enabling.android_aries_sdk.credential.abstractions.ICredentialService
import com.digital_enabling.android_aries_sdk.credential.abstractions.ISchemaService
import com.digital_enabling.android_aries_sdk.credential.abstractions.ITailsService
import com.digital_enabling.android_aries_sdk.credential.models.*
import com.digital_enabling.android_aries_sdk.credential.records.*
import com.digital_enabling.android_aries_sdk.decorators.DecoratorNames
import com.digital_enabling.android_aries_sdk.decorators.attachments.Attachment
import com.digital_enabling.android_aries_sdk.decorators.attachments.AttachmentContent
import com.digital_enabling.android_aries_sdk.decorators.service.ServiceDecorator
import com.digital_enabling.android_aries_sdk.decorators.threading.getThreadId
import com.digital_enabling.android_aries_sdk.decorators.threading.threadFrom
import com.digital_enabling.android_aries_sdk.decorators.transport.ReturnRouteTypes
import com.digital_enabling.android_aries_sdk.didexchange.IConnectionService
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionState
import com.digital_enabling.android_aries_sdk.ledger.abstractions.ILedgerService
import com.digital_enabling.android_aries_sdk.ledger.models.TransactionTypes
import com.digital_enabling.android_aries_sdk.messagedispatcher.IMessageService
import com.digital_enabling.android_aries_sdk.messagedispatcher.sendReceiveExtended
import com.digital_enabling.android_aries_sdk.payments.IPaymentService
import com.digital_enabling.android_aries_sdk.utils.CredentialUtils
import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.utils.TagConstants
import com.digital_enabling.android_aries_sdk.wallet.IWalletRecordService
import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchQuery
import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.hyperledger.indy.sdk.IndyException
import org.hyperledger.indy.sdk.anoncreds.Anoncreds
import org.hyperledger.indy.sdk.anoncreds.AnoncredsResults
import org.hyperledger.indy.sdk.anoncreds.RevocationRegistryFullException
import org.hyperledger.indy.sdk.blob_storage.BlobStorageReader
import org.hyperledger.indy.sdk.did.Did
import java.util.*
import kotlin.math.pow

class DefaultCredentialService(
    /**
     * The event aggregator.
     */
    private val eventAggregator: IEventAggregator,
    /**
     * The ledger service
     */
    private val ledgerService: ILedgerService,
    /**
     * The connection service
     */
    private val connectionService: IConnectionService,
    /**
     * The record service
     */
    private val recordService: IWalletRecordService,
    /**
     * The schema service
     */
    private val schemaService: ISchemaService,
    /**
     * The tails service
     */
    private val tailsService: ITailsService,
    /**
     * The provisioning service
     */
    private val provisioningService: IProvisioningService,
    /**
     * The payment service
     */
    private val paymentService: IPaymentService,
    /**
     * The message service
     */
    private val messageService: IMessageService
) : ICredentialService {

    /**
     * @see ICredentialService
     */
    override suspend fun get(
        agentContext: IAgentContext,
        credentialId: String
    ): CredentialRecord {
        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        return recordService.get(
            wallet,
            credentialId,
            RecordType.CREDENTIAL_RECORD
        )
            ?: throw AriesFrameworkException(
                ErrorCode.RECORD_NOT_FOUND, "Credential record not found"
            )
    }

    /**
     * @see ICredentialService
     */
    override suspend fun list(
        agentContext: IAgentContext,
        query: ISearchQuery?,
        count: Int,
        skip: Int
    ): List<CredentialRecord> {
        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        return recordService.search(
            wallet,
            query,
            null,
            count,
            skip,
            RecordType.CREDENTIAL_RECORD
        )
    }

    /**
     * @see ICredentialService
     */
    override suspend fun rejectOffer(
        agentContext: IAgentContext,
        credentialId: String
    ) {

        val credential = get(agentContext, credentialId)

        if (credential.state != CredentialState.OFFERED)
            throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Credential state was invalid. Expected '{${CredentialState.OFFERED}}', found '{${credential.state}}'"
            )

        credential.trigger(CredentialTrigger.REJECT)

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        recordService.update(wallet, credential)

    }

    /**
     * @see ICredentialService
     */
    override suspend fun revokeCredentialOffer(
        agentContext: IAgentContext,
        offerId: String
    ) {
        val credentialRecord = get(agentContext, offerId)

        if (credentialRecord.state != CredentialState.OFFERED)
            throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Credential state was invalid. Expected '{${CredentialState.OFFERED}}', found '{${credentialRecord.state}}'"
            )

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        recordService.delete<CredentialRecord>(
            wallet,
            offerId,
            RecordType.CONNECTION_RECORD
        )
    }

    override suspend fun processCredentialRequest(
        agentContext: IAgentContext,
        credentialRequest: CredentialRequestMessage,
        connection: ConnectionRecord
    ): String {
        var credential: CredentialRecord? = null

        val threadId = credentialRequest.getThreadId()
            ?: throw IllegalArgumentException("Credential thread id not found")

        for (retry in 1..3) {
            try {
                credential = this.getByThreadId(agentContext, threadId)
            } catch (e: AriesFrameworkException) {
                delay(((retry.toDouble()).pow(2) * 100) as Long)
                if (retry == 3) {
                    throw e
                }
                continue
            }
            break
        }
        val credentialAttachment: Attachment =
            credentialRequest.request?.firstOrNull { it.id == "libindy-cred-offer-0" }
                ?: throw IllegalArgumentException("Credential request attachment not found.")
        if (credential!!.state != CredentialState.OFFERED) {
            throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Credential state was invalid. Expected '${CredentialState.OFFERED}', found '${credential.state}'"
            )
        }

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        val data = credentialAttachment.data
            ?: throw IllegalArgumentException("Credential attachment has no data")
        val base64 = data.base64
            ?: throw IllegalArgumentException("Credential attachment data has no base64 string")

        credential.requestJson = String(Base64.getDecoder().decode(base64), Charsets.UTF_8)
        credential.connectionId = connection.id
        credential.trigger(CredentialTrigger.REQUEST)
        recordService.update(wallet, credential)

        val type = credentialRequest.type
            ?: throw IllegalArgumentException("CredentialRequest has no type")
        val serviceMessageProcessingEvent = ServiceMessageProcessingEvent(
            credential.id,
            type,
            threadId
        )
        eventAggregator.publish(serviceMessageProcessingEvent)
        return credential.id
    }

    /**
     * @see ICredentialService
     */
    override suspend fun rejectCredentialRequest(
        agentContext: IAgentContext,
        credentialId: String
    ) {
        val credential = get(agentContext, credentialId)

        if (credential.state != CredentialState.REQUESTED)
            throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Credential state was invalid. Expected '${CredentialState.REQUESTED}', found '${credential.state}'"
            )

        credential.trigger(CredentialTrigger.REJECT)

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        recordService.update(wallet, credential)
    }

    /**
     * @see ICredentialService
     */
    override suspend fun revokeCredential(
        agentContext: IAgentContext,
        credentialId: String
    ) {

        val credentialRecord = get(agentContext, credentialId)

        if (credentialRecord.state != CredentialState.ISSUED)
            throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Credential state was invalid. Expected '{${CredentialState.ISSUED}}', found '{${credentialRecord.state}}'"
            )

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        val provisioning = provisioningService.getProvisioning(wallet)

        credentialRecord.trigger(CredentialTrigger.REVOKE)

        val revocationRegistryId = credentialRecord.revocationRegistryId
            ?: throw IllegalArgumentException("CredentialRecord has no revocationRegistryId")

        val revocationRecord = recordService.get<RevocationRegistryRecord>(
            wallet,
            revocationRegistryId,
            RecordType.REVOCATION_REGISTRY_RECORD
        ) ?: throw AriesFrameworkException(ErrorCode.RECORD_NOT_FOUND, "RevocationRecord not found")

        val tailsFile = revocationRecord.tailsFile
            ?: throw IllegalArgumentException("RevocationRecord has no tailsFile")

        val tailsReader = tailsService.openTails(tailsFile)
        val revocRegistryDeltaJson = Anoncreds.issuerRevokeCredential(
            agentContext.wallet,
            tailsReader.blobStorageReaderHandle,
            revocationRecord.id,
            credentialRecord.credentialRevocationId
        )

        val paymentInfo =
            paymentService.getTransactionCost(agentContext, TransactionTypes.REVOC_REG_ENTRY)

        val issuerDid = provisioning.issuerDid ?: throw AriesFrameworkException(
            ErrorCode.INVALID_RECORD_DATA,
            "ProvisioningRecord has no issuerDid"
        )

        ledgerService.sendRevocationRegistryEntry(
            agentContext,
            issuerDid,
            revocationRecord.id,
            "CL_ACCUM",
            revocRegistryDeltaJson.get(),
            paymentInfo
        );

        recordService.update(wallet, paymentInfo.paymentAddress)

        recordService.update(wallet, credentialRecord)
    }

    /**
     * @see ICredentialService
     */
    override suspend fun deleteCredential(
        agentContext: IAgentContext,
        credentialId: String
    ) {

        val credentialRecord = get(agentContext, credentialId)
        try {
            Anoncreds.proverDeleteCredential(agentContext.wallet, credentialRecord.credentialId)
        } catch (e: Exception) {
            // OK
        }

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        recordService.delete<CredentialRecord>(
            wallet,
            credentialId,
            RecordType.CREDENTIAL_RECORD
        )
    }

    /**
     * @see ICredentialService
     */
    override suspend fun processOffer(
        agentContext: IAgentContext,
        credentialOffer: CredentialOfferMessage,
        connection: ConnectionRecord?
    ): String {
        val offerAttachment =
            credentialOffer.offers?.firstOrNull { it.id == "libindy-cred-offer-0" }
                ?: throw Exception(credentialOffer.offers.toString(), NullPointerException())

        val data = offerAttachment.data
            ?: throw IllegalArgumentException("Credential attachment has no data")
        val base64 = data.base64
            ?: throw IllegalArgumentException("Credential attachment data has no base64 string")

        val offerJson = String(Base64.getDecoder().decode(base64), Charsets.UTF_8)
        val offer = Json.parseToJsonElement(offerJson) as JsonObject
        val definitionId = offer["cred_def_id"].toString()
        val schemaId = offer["schema_id"].toString()

        val threadId = credentialOffer.getThreadId() ?: UUID.randomUUID().toString()
        // Write offer record to local wallet
        val credentialRecord = CredentialRecord(threadId)

        val preview = credentialOffer.credentialPreview
            ?: throw IllegalArgumentException("CredentialOffer has no credentialPreview")

        val attributes = preview.attributes
            ?: throw IllegalArgumentException("CredentialPreview has no attributes")

        credentialRecord.credentialAttributesValues =
            attributes.onEach {
                if (it != null) {
                    it.name?.let { it1 ->
                        it.value?.let { it2 ->
                            CredentialPreviewAttribute(
                                it1,
                                it2
                            )
                        }
                    }
                }
            }.asList()

        credentialRecord.offerJson = offerJson
        if (connection != null) {
            credentialRecord.connectionId = connection.id
        }
        credentialRecord.credentialId = definitionId
        credentialRecord.schemaId = schemaId

        credentialRecord.setTag(TagConstants.ROLE, TagConstants.HOLDER)
        credentialRecord.setTag(TagConstants.LAST_THREAD_ID, threadId)

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        recordService.add(wallet, credentialRecord)

        val type =
            credentialOffer.type ?: throw IllegalArgumentException("CredentialOffer has no type")

        eventAggregator.publish(
            ServiceMessageProcessingEvent(
                threadId,
                credentialRecord.id,
                type
            )
        )

        return credentialRecord.id
    }

    /**
     * @see ICredentialService
     */
    override suspend fun createCredential(
        agentContext: IAgentContext,
        message: CredentialOfferMessage
    ): CredentialRecord? {

        var credentialRecordId = ""
        try {
            //TODO <DecoratorNames> oder <String> ?
            val service = message.getDecorator<ServiceDecorator>(DecoratorNames.SERVICE_DECORATOR)
            credentialRecordId = processOffer(agentContext, message, null)

            val (request, record) = createRequest(agentContext, credentialRecordId)

            val wallet =
                agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

            val provisioning = provisioningService.getProvisioning(wallet)

            try {
                val credentialIssueMessage =
                    service.serviceEndpoint?.let {
                        messageService.sendReceiveExtended<CredentialRequestMessage, CredentialIssueMessage>(
                            agentContext,
                            request,
                            service.recipientKeys.toString(),
                            it,
                            service.routingKeys?.toTypedArray(), //as Array<String>,
                            provisioning.issuerVerkey,
                            ReturnRouteTypes.ALL,
                        )
                    }
                val recordId =
                    credentialIssueMessage?.let { processCredential(agentContext, it, null) };
                return recordId?.let {
                    recordService.get<CredentialRecord>(
                        wallet,
                        it,
                        RecordType.CREDENTIAL_RECORD
                    )
                }
            } catch (ex: AriesFrameworkException) {
                if (ex.errorCode == ErrorCode.A2A_MESSAGE_TRANSMISSION_ERROR) {
                    throw AriesFrameworkException(ex.errorCode, ex.message ?: "")
                } else {
                    throw AriesFrameworkException(ErrorCode.A2A_MESSAGE_TRANSMISSION_ERROR)
                }

            }
        } catch (e: IndyException) {
            if (e.sdkErrorCode == 309) {
                throw AriesFrameworkException(
                    ErrorCode.LEDGER_ITEM_NOT_FOUND,
                    e.message ?: "",
                    credentialRecordId
                )
            } else {
                throw AriesFrameworkException(ErrorCode.LEDGER_ITEM_NOT_FOUND)
            }

        }
    }

    override suspend fun createCredential(
        agentContext: IAgentContext,
        credentialId: String
    ): Pair<CredentialIssueMessage, CredentialRecord> {
        return createCredential(agentContext, credentialId, listOf())
    }

    override suspend fun createCredential(
        agentContext: IAgentContext,
        credentialId: String,
        values: List<CredentialPreviewAttribute>
    ): Pair<CredentialIssueMessage, CredentialRecord> {

        val credentialRecord = get(agentContext, credentialId)
        if (credentialRecord.state != CredentialState.REQUESTED) {
            throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Credential state was invalid. Expected '${CredentialState.REQUESTED}', found '${credentialRecord.state}'"
            )
        }
        if (values.any()) {
            credentialRecord.credentialAttributesValues = values
        }

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        val defId = credentialRecord.credentialDefinitionId
        if (defId.isNullOrEmpty()) {
            throw IllegalArgumentException("CredentialRecord has no credentialDefinitionId")
        }

        val definitionRecord = schemaService.getCredentialDefinition(
            wallet,
            defId
        )
        if (credentialRecord.connectionId != null) {
            val connection = connectionService.get(agentContext, credentialRecord.connectionId!!)
            if (connection.state != ConnectionState.CONNECTED) {
                throw AriesFrameworkException(
                    ErrorCode.RECORD_IN_INVALID_STATE,
                    "Connection state was invalid. Expected '${ConnectionState.CONNECTED}', found '${connection.state}'"
                )
            }
        }
        val issuedCredentialRevocationRecordPair =
            issueCredentialSafe(agentContext, definitionRecord, credentialRecord)
        if (definitionRecord.supportsRevocation) {
            var provisioning = provisioningService.getProvisioning(wallet)
            var paymentInfo = paymentService.getTransactionCost(
                agentContext,
                TransactionTypes.REVOC_REG_ENTRY
            )

            val issuerDid = provisioning.issuerDid ?: throw AriesFrameworkException(
                ErrorCode.INVALID_RECORD_DATA,
                "ProvisioningRecord has no issuerDid"
            )

            if (issuedCredentialRevocationRecordPair.first.revocRegDeltaJson != null) {
                ledgerService.sendRevocationRegistryEntry(
                    agentContext,
                    issuerDid,
                    issuedCredentialRevocationRecordPair.second.id,
                    "CL_ACCUM",
                    issuedCredentialRevocationRecordPair.first.revocRegDeltaJson,
                    paymentInfo
                )
            }

            // store data relevant for credential revocation
            credentialRecord.credentialRevocationId =
                issuedCredentialRevocationRecordPair.first.revocId
            credentialRecord.revocationRegistryId = issuedCredentialRevocationRecordPair.second.id

            recordService.update(wallet, paymentInfo.paymentAddress)
        }
        credentialRecord.trigger(CredentialTrigger.ISSUE)
        recordService.update(wallet, credentialRecord)

        val threadId = credentialRecord.getTag(TagConstants.LAST_THREAD_ID)

        val credentialMsg = CredentialIssueMessage(agentContext.useMessageTypesHttps)
        val inputAttachment = Attachment("libindy-cred-0")
        inputAttachment.mimeType = CredentialMimeTypes.APPLICATION_JSON_MIME_TYPE
        val data = AttachmentContent()
        data.base64 = issuedCredentialRevocationRecordPair.first.credentialJson
        inputAttachment.data = data

        credentialMsg.credentials = listOf(inputAttachment)
        if (threadId != null) {
            credentialMsg.threadFrom(threadId)
        } else {
            // TODO throw?
            throw Exception()
        }
        return Pair(credentialMsg, credentialRecord)
    }

    private suspend fun issueCredentialSafe(
        agentContext: IAgentContext,
        definitionRecord: DefinitionRecord,
        credentialRecord: CredentialRecord
    ): Pair<AnoncredsResults.IssuerCreateCredentialResult, RevocationRegistryRecord> {
        var tailsReader: BlobStorageReader? = null
        var revocationRecord: RevocationRegistryRecord? = null

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        if (definitionRecord.supportsRevocation) {
            val revocRegId = definitionRecord.currentRevocationRegistryId
            if (revocRegId.isNullOrEmpty()) {
                throw IllegalArgumentException("DefinitionRecord has no currentRevocationRegistryId")
            }
            runBlocking {
                revocationRecord = recordService.get(
                    wallet,
                    revocRegId,
                    RecordType.REVOCATION_REGISTRY_RECORD
                )
            }
            val tailsFile = revocationRecord?.tailsFile
            if (tailsFile.isNullOrEmpty()) {
                throw IllegalArgumentException("RevocationRecord has no tailsFile")
            }
            tailsReader = tailsService.openTails(tailsFile)
        }

        try {
            val attributeValues = credentialRecord.credentialAttributesValues?.toList()
                ?: throw IllegalArgumentException("CredentialRecord has no credentialAttributesValues")

            return Pair(
                Anoncreds.issuerCreateCredential(
                    agentContext.wallet,
                    credentialRecord.offerJson,
                    credentialRecord.requestJson,
                    CredentialUtils.formatCredentialValues(attributeValues),
                    definitionRecord.currentRevocationRegistryId,
                    tailsReader!!.blobStorageReaderHandle
                ).get(), revocationRecord!!
            )
        } catch (e: RevocationRegistryFullException) {
            if (!definitionRecord.revocationAutoScale) throw e
        }

        val registryIndex =
            definitionRecord.currentRevocationRegistryId?.split(':')?.lastOrNull()?.split('-')
                ?.firstOrNull()

        val registryTag: String = try {
            val currentIndex = registryIndex!!.toInt()
            "${currentIndex + 1}-${definitionRecord.maxCredentialCount}"
        } catch (e: Exception) {
            "1-{${definitionRecord.maxCredentialCount}"
        }

        val (_, nextRevocationRecord) = schemaService.createRevocationRegistry(
            agentContext, registryTag,
            definitionRecord
        )

        definitionRecord.currentRevocationRegistryId = nextRevocationRecord.id

        recordService.update(wallet, definitionRecord);
        tailsReader = nextRevocationRecord.tailsFile?.let { tailsService.openTails(it) };

        val attributeValues = credentialRecord.credentialAttributesValues?.toList()
            ?: throw IllegalArgumentException("CredentialRecord has no credentialAttributesValues")

        if (tailsReader != null) {
            return Pair(
                Anoncreds.issuerCreateCredential(
                    agentContext.wallet,
                    credentialRecord.offerJson,
                    credentialRecord.requestJson,
                    CredentialUtils.formatCredentialValues(attributeValues),
                    nextRevocationRecord.id,
                    tailsReader.blobStorageReaderHandle
                ).get(),
                nextRevocationRecord
            )
        }
        throw IllegalArgumentException("Could not open tails file")
    }

    /**
     * @see ICredentialService
     */
    override suspend fun createRequest(
        agentContext: IAgentContext,
        credentialId: String
    ): Pair<CredentialRequestMessage, CredentialRecord> {

        val credential = get(agentContext, credentialId)
        if (credential.state != CredentialState.OFFERED)
            throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Credential state was invalid. Expected '{${CredentialState.OFFERED}}', found '{${credential.state}}'"
            )

        val proverDid: String = if (credential.connectionId != null) {
            val connection = connectionService.get(agentContext, credential.connectionId!!)
            connection.myDid.toString()
        } else {
            val newDid = Did.createAndStoreMyDid(agentContext.wallet, "{}")
            newDid.get().did
        }

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        val definition =
            credential.credentialId?.let { ledgerService.lookupDefinition(agentContext, it) }
        val provisioning = provisioningService.getProvisioning(wallet)

        val request = Anoncreds.proverCreateCredentialReq(
            wallet,
            proverDid,
            credential.offerJson,
            definition?.objectJson,
            provisioning.masterSecretId
        )

        // Update local credential record with new info
        credential.credentialRequestMetadataJson = request.get().credentialRequestMetadataJson
        credential.trigger(CredentialTrigger.REQUEST)
        recordService.update(wallet, credential)
        val threadId = credential.getTag(TagConstants.LAST_THREAD_ID)

        val response = CredentialRequestMessage(agentContext.useMessageTypesHttps)
        response.comment = ""

        val attachment = Attachment("libindy-cred-request-0")
        attachment.mimeType = CredentialMimeTypes.APPLICATION_JSON_MIME_TYPE
        attachment.data = AttachmentContent()
        if (attachment.data == null) {
            attachment.data = AttachmentContent()
        }
        attachment.data!!.base64 = request.get().credentialRequestJson

        response.request = arrayOf(attachment)

        if (threadId != null) {
            response.threadFrom(threadId)
        }
        return Pair(response, credential);
    }

    override suspend fun processCredential(
        agentContext: IAgentContext,
        credential: CredentialIssueMessage,
        connection: ConnectionRecord?
    ): String {
        val credentialAttachment = credential.credentials?.firstOrNull { it.id == "libindy-cred-0" }
            ?: throw IllegalArgumentException("Credential attachment not found")

        val credentialJson = Json.encodeToJsonElement(credentialAttachment.data?.base64.toString())
        val credentialJobj = credentialJson as JsonObject
        val definitionId = credentialJobj["cred_def_id"].toString()
        val revRegId = credentialJobj["rev_reg_id"].toString()

        var credentialRecord: CredentialRecord? = null

        val threadId = credential.getThreadId()
            ?: throw IllegalArgumentException("Credential thread id not found")

        for (retry in 1..3) { // TODO resilience framework
            try {
                credentialRecord = this.getByThreadId(agentContext, threadId)
            } catch (e: AriesFrameworkException) {
                delay(((retry.toDouble()).pow(2) * 100) as Long)
                if (retry == 3) {
                    throw e
                }
                continue
            }
            break
        }

        if (credentialRecord == null || credentialRecord.state != CredentialState.REQUESTED) {
            throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Credential state was invalid. Expected '${CredentialState.REQUESTED}', found '${credentialRecord?.state}'"
            )
        }
        val credentialDefinition = ledgerService.lookupDefinition(agentContext, definitionId)
        var revocationRegistryDefinitionJson: String? = null
        if (revRegId.isNotEmpty()) {
            val revocationRegistry =
                ledgerService.lookupRevocationRegistryDefinition(agentContext, revRegId)
            revocationRegistryDefinitionJson = revocationRegistry.objectJson
            credentialRecord.revocationRegistryId = revRegId
        }

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        val credentialId = Anoncreds.proverStoreCredential(
            wallet,
            credentialRecord.id,
            credentialRecord.credentialRequestMetadataJson,
            Json.decodeFromJsonElement(credentialJson),
            credentialDefinition.objectJson,
            revocationRegistryDefinitionJson
        ).get()

        credentialRecord.credentialId = credentialId
        credentialRecord.trigger(CredentialTrigger.ISSUE)
        recordService.update(wallet, credentialRecord)

        val type =
            credential.type ?: throw IllegalArgumentException("Credential has no type")

        eventAggregator.publish(
            ServiceMessageProcessingEvent(
                threadId,
                credentialRecord.id,
                type
            )
        )

        return credentialRecord.id
    }

    /**
     * @see ICredentialService
     */
    override suspend fun createOffer(
        agentContext: IAgentContext,
        config: OfferConfiguration,
        connectionId: String?
    ): Pair<CredentialOfferMessage, CredentialRecord> {
        // logger in dotnet

        val threadId = UUID.randomUUID().toString()
        if (connectionId != null) {
            val connection = connectionService.get(agentContext, connectionId)

            if (connection.state != ConnectionState.CONNECTED) {
                throw AriesFrameworkException(
                    ErrorCode.RECORD_IN_INVALID_STATE,
                    "Credential state was invalid. Expected '{${ConnectionState.CONNECTED}}', found '{${connection.state}}'"
                )
            }
        }

        if (config.credentialAttributeValues.any()) {

            CredentialUtils.validateCredentialPreviewAttributes(config.credentialAttributeValues)
        }

        val offerJson = Anoncreds.issuerCreateCredentialOffer(
            agentContext.wallet,
            config.credentialDefinitionId
        ).get()

        // Todo check with dotnet code
        val offerJsonE = Json.encodeToJsonElement(offerJson)
        val offerJobj = offerJsonE as JsonObject
        val schemaId = offerJobj["schema_id"].toString()

        // Write offer record to local wallet
        val credentialRecord = CredentialRecord(threadId)
        credentialRecord.credentialId = config.credentialDefinitionId
        credentialRecord.offerJson = offerJson
        credentialRecord.connectionId = connectionId
        credentialRecord.schemaId = schemaId
        credentialRecord.credentialAttributesValues = config.credentialAttributeValues

        credentialRecord.setTag(TagConstants.LAST_THREAD_ID, threadId)
        credentialRecord.setTag(TagConstants.ROLE, TagConstants.ISSUER)
        if (config.issuerDid.isNotEmpty())
            credentialRecord.setTag(TagConstants.ISSUER_DID, config.issuerDid)
        config.tags.forEach {
            if (!credentialRecord.tags.containsKey(it.key))
                credentialRecord.tags[it.key] = it.value
        }

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        recordService.add(wallet, credentialRecord)
        val credentialOfferMessage = CredentialOfferMessage(agentContext.useMessageTypesHttps)

        credentialOfferMessage.id = threadId
        val attachment = Attachment("libindy-cred-offer-0")
        attachment.mimeType = CredentialMimeTypes.APPLICATION_JSON_MIME_TYPE
        attachment.data = AttachmentContent()
        if (attachment.data == null) {
            attachment.data = AttachmentContent()
        }
        attachment.data!!.base64 = offerJson
        credentialOfferMessage.offers = arrayOf(attachment)

        //if(credentialRecord.credentialAttributesValues != null) {
        val credentialPreview = CredentialPreviewMessage(agentContext.useMessageTypesHttps)

        val values = credentialRecord.credentialAttributesValues
        if (values == null) {
            throw IllegalArgumentException("CredentialRecord has no credentialAttributesValues")
        }
        val attributes =
            values.onEach {
                if (it != null) {
                    it.name?.let { it1 ->
                        it.value?.let { it2 ->
                            CredentialPreviewAttribute(
                                it1,
                                it2
                            )
                        }
                    }
                }
            }.toList().toTypedArray()

        credentialPreview.attributes = attributes

        credentialOfferMessage.credentialPreview = credentialPreview
        return Pair(credentialOfferMessage, credentialRecord);
    }

    override suspend fun createOffer(
        agentContext: IAgentContext,
        config: OfferConfiguration
    ): Pair<CredentialOfferMessage, CredentialRecord> {

        if (!config.credentialAttributeValues.any()) {
            throw UnsupportedOperationException(
                "You must supply credential values when creating connectionless credential offer"
            )
        }

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        val (message, record) = createOffer(agentContext, config, null)
        val provisioning = provisioningService.getProvisioning(wallet)
        message.addDecorator(provisioning.toServiceDecorator(), DecoratorNames.SERVICE_DECORATOR)

        recordService.update(wallet, record);
        return Pair(message, record)
    }

    // Extension Functions

    fun getByThreadId(context: IAgentContext, threadId: String): CredentialRecord {
        var credentialRecord: CredentialRecord

        try {
            runBlocking { credentialRecord = get(context, threadId) }
        } catch (e: AriesFrameworkException) {
            // Record was not found, thread ID didn't match record ID. This is OK
        }

        val search: List<CredentialRecord>
        runBlocking {
            search =
                list(context, SearchQuery.equal(TagConstants.LAST_THREAD_ID, threadId), 100)
        }

        if (search.isEmpty()) {
            throw AriesFrameworkException(
                ErrorCode.RECORD_NOT_FOUND,
                "Credential record not found by thread id : $threadId"
            )
        }

        if (search.size > 1) {
            throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Multiple credential records found by thread id : $threadId"
            )
        }

        credentialRecord = search.single()
        return credentialRecord
    }
}