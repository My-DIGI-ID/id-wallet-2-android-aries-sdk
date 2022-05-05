package com.digital_enabling.android_aries_sdk.ledger.abstractions

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.ledger.models.AuthorizationRule
import com.digital_enabling.android_aries_sdk.payments.models.TransactionCost
import org.hyperledger.indy.sdk.ledger.LedgerResults

/**
 * Ledger service.
 */
interface ILedgerService {
    /**
     * Gets a list of all authorization rules for the given pool.
     * @param agentContext The agent context.
     */
    fun lookupAuthorizationRules(agentContext: IAgentContext): List<AuthorizationRule>

    /**
     * Looks up an attribute value on the ledger.
     * @param agentContext The agent context.
     * @param targetDid The target DID for the lookup.
     * @param attributeName Attribute name.
     * @return The attribute value or null if none were found.
     */
    fun lookupAttribute(
        agentContext: IAgentContext,
        targetDid: String,
        attributeName: String
    ): String?

    /**
     * Regist an attribute for the specified [targetDid] to the ledger.
     * @param agentContext Agent context.
     * @param submittedDid Submitted did.
     * @param targetDid Target did.
     * @param attributeName Attribute name.
     * @param value The attribute value.
     * @param paymentInfo Payment information
     * @return The attribute.
     */
    suspend fun registerAttribute(
        agentContext: IAgentContext,
        submittedDid: String,
        targetDid: String,
        attributeName: String,
        value: Any,
        paymentInfo: TransactionCost? = null
    )

    /**
     * Lookup the schema.
     * @param agentContext The Agent context.
     * @param schemaId Schema identifier.
     * @return The schema.
     */
    fun lookupSchema(
        agentContext: IAgentContext,
        schemaId: String
    ): LedgerResults.ParseResponseResult

    /**
     * Lookup NYM record on the ledger
     * @param agentContext The agent context.
     * @param did The did.
     * @return
     */
    fun lookupNym(agentContext: IAgentContext, did: String): String

    /**
     * Lookup the ledger transaction.
     * @param agentContext The agent context.
     * @param ledgerType The ledger type.
     * @param sequenceId The sequence identifier.
     * @return The transaction.
     */
    fun lookupTransaction(
        agentContext: IAgentContext,
        ledgerType: String?,
        sequenceId: Int
    ): String

    /**
     * Lookups the definition.
     * @param agentContext The agent context.
     * @param definitionId Definition identifier.
     * @return The definition.
     */
    fun lookupDefinition(
        agentContext: IAgentContext,
        definitionId: String
    ): LedgerResults.ParseResponseResult

    /**
     * Lookups the revocation registry definition.
     * @param agentContext The agent context.
     * @param registryId The registry identifier.
     */
    fun lookupRevocationRegistryDefinition(
        agentContext: IAgentContext,
        registryId: String
    ): LedgerResults.ParseResponseResult

    /**
     * Lookup the revocation registry delta for the given registry in the range specified.
     * @param agentContext The agent context.
     * @param revocationRegistryId Revocation registry identifier.
     * @param from From.
     * @param to To.
     * @return The revocation registry delta.
     */
    fun lookupRevocationRegistryDelta(
        agentContext: IAgentContext,
        revocationRegistryId: String,
        from: Long,
        to: Long
    ): LedgerResults.ParseRegistryResponseResult

    /**
     * Lookup revocation registry for the given point in time.
     * @param agentContext The agent context.
     * @param revocationRegistryId Revocation registry identifier.
     * @param timestamp Timestamp.
     * @return The revocation registry.
     */
    fun lookupRevocationRegistry(
        agentContext: IAgentContext,
        revocationRegistryId: String,
        timestamp: Long
    ): LedgerResults.ParseRegistryResponseResult

    /**
     * Registers the nym.
     * @param agentContext The agent context.
     * @param submitterDid The submitter Did.
     * @param theirDid their Did.
     * @param theirVerkey Their verkey.
     * @param role Role the new nym will assume.
     * @param paymentInfo Payment information.
     * @return Registration.
     */
    suspend fun registerNym(
        agentContext: IAgentContext,
        submitterDid: String,
        theirDid: String,
        theirVerkey: String,
        role: String,
        paymentInfo: TransactionCost? = null
    )

    /**
     * Registers the credential definition.
     * @param agentContext The agent context.
     * @param submitterDid The submitter Did.
     * @param data Data.
     * @param paymentInfo Payment information
     * @return The credential definition.
     */
    suspend fun registerCredentialDefinition(
        agentContext: IAgentContext,
        submitterDid: String,
        data: String,
        paymentInfo: TransactionCost? = null
    )

    /**
     * Registers the revocation registry definition.
     * @param agentContext The agent context.
     * @param submitterDid The submitter Did.
     * @param data The data.
     * @param paymentInfo Payment information.
     */
    suspend fun registerRevocationRegistryDefinition(
        agentContext: IAgentContext,
        submitterDid: String,
        data: String,
        paymentInfo: TransactionCost? = null
    )

    /**
     * Sends the revocation registry entry.
     * @param agentContext The agent context.
     * @param issuerDid The issuer did.
     * @param revocationRegistryDefinitionId The revocation registry definition identifier.
     * @param revocationDefinitionType Type of the revocation definition.
     * @param value The value.
     * @param paymentInfo Payment information.
     */
    suspend fun sendRevocationRegistryEntry(
        agentContext: IAgentContext,
        issuerDid: String,
        revocationRegistryDefinitionId: String,
        revocationDefinitionType: String,
        value: String,
        paymentInfo: TransactionCost? = null
    )

    /**
     * Registers the schema.
     * @param agentContext The agent context.
     * @param issuerDid The issuer did.
     * @param schemaJson The schema json.
     * @param paymentInfo Payment information.
     */
    suspend fun registerSchema(
        agentContext: IAgentContext,
        issuerDid: String,
        schemaJson: String,
        paymentInfo: TransactionCost? = null
    )
}