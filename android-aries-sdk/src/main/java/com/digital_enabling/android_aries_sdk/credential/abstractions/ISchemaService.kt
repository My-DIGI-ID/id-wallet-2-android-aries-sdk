package com.digital_enabling.android_aries_sdk.credential.abstractions

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.credential.models.CredentialDefinitionConfiguration
import com.digital_enabling.android_aries_sdk.credential.records.DefinitionRecord
import com.digital_enabling.android_aries_sdk.credential.records.RevocationRegistryRecord
import com.digital_enabling.android_aries_sdk.credential.records.SchemaRecord
import org.hyperledger.indy.sdk.anoncreds.AnoncredsResults
import org.hyperledger.indy.sdk.wallet.Wallet

/**
 * Schema service
 */
interface ISchemaService {
    /**
     * Creates and registers schema on the ledger.
     * @param context The agent context
     * @param issuerDid The issuer did.
     * @param name The name.
     * @param version The version.
     * @param attributeNames The attribute names.
     * @return The schema identifier of the stored schema object. This identifier can be used for ledger schema lookup
     */
    suspend fun createSchema(
        context: IAgentContext,
        issuerDid: String,
        name: String,
        version: String,
        attributeNames: List<String>
    ): String

    /**
     * Creates and registers schema on the ledger.
     * @param context The agent context
     * @param name The name
     * @param version The version
     * @param attributeNames the attribute names
     */
    suspend fun createSchema(
        context: IAgentContext, name: String,
        version: String,
        attributeNames: List<String>
    ): String

    /**
     * Creates the credential definition and registers it on the ledger.
     * @param context The agent context
     * @param configuration The credential definition configuration
     * @return The credential definition identifier of the stored definition record. This identifier can be used for ledger definition lookup.
     */
    suspend fun createCredentialDefinition(
        context: IAgentContext,
        configuration: CredentialDefinitionConfiguration
    ): String

    /**
     * Creates new revocation registry record and definition for the given credential definition.
     * @param context The context
     * @param tag The tag
     * @param definitionRecord The definition record
     */
    suspend fun createRevocationRegistry(
        context: IAgentContext,
        tag: String,
        definitionRecord: DefinitionRecord
    ): Pair<AnoncredsResults.IssuerCreateAndStoreRevocRegResult, RevocationRegistryRecord>

    /**
     * Gets the schemas.
     * @param wallet The wallet
     * @return A list of schema records that this issuer has created.
     */
    fun listSchemas(wallet: Wallet): List<SchemaRecord>

    /**
     * Gets the credential definition.
     * @param wallet
     * @return A list of credential definition records that this issuer has created
     */
    fun listCredentialDefinitions(wallet: Wallet): List<DefinitionRecord>

    /**
     * Gets the credential definition
     * @param wallet
     * @param credentialDefinitionId The credential definition identifier.
     * @return The credential definition record.
     */
    fun getCredentialDefinition(
        wallet: Wallet,
        credentialDefinitionId: String
    ): DefinitionRecord

    /**
     * Looks up the credential definition on the ledger.
     * @param context The agent context
     * @param definitionId The identifier of the definition to resolve.
     *
     * @return A json string of the credential definition
     */
    fun lookupCredentialDefinition(context: IAgentContext, definitionId: String): String

    /**
     * Looks up the schema definition on the ledger given a credential definition identifier.
     * @param context the agent context
     * @param credentialDefinitionId The credential definition id.
     * @return A json string of the schema
     */
    fun lookupSchemaFromCredentialDefinition(
        context: IAgentContext,
        credentialDefinitionId: String
    ): String?

    /**
     * Looks up the schema definition on the ledger.
     * @param context The agent context
     * @param sequenceId The sequence identifier of the schema to resolve.
     *
     * @return A json string of the schema
     */
    fun lookupSchema(context: IAgentContext, sequenceId: Int): String?

    /**
     * Looks up the schema definition on the ledger.
     * @param context The agent context
     * @param schemaId The identifier of the schema definition to resolve.
     * @return A json string of the schema
     */
    fun lookupSchema(context: IAgentContext, schemaId: String): String


}