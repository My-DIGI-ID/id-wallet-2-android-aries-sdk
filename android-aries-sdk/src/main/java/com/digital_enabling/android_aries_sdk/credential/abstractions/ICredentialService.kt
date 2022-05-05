package com.digital_enabling.android_aries_sdk.credential.abstractions

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.credential.models.*
import com.digital_enabling.android_aries_sdk.credential.records.CredentialRecord
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery

/**
 * Credential service
 */
interface ICredentialService {

    /**
     * Gets credential record for the given identifier.
     * @param agentContext Agent context
     * @param credentialId The credential identifier.
     * @return The stored credential record.
     */
    suspend fun get(agentContext: IAgentContext, credentialId: String): CredentialRecord

    /**
     * Retrieves a list of [CredentialRecord] items for the given search criteria.
     * @param agentContext The agent context
     * @param query The query
     * @param count The number of items to return
     * @param skip The number of items to skip
     * @return A list of credential records matching the search criteria
     */
    suspend fun list(
        agentContext: IAgentContext,
        query: ISearchQuery? = null,
        count: Int = 100,
        skip: Int = 0
    ): List<CredentialRecord>

    /**
     * Process the offer and stores in the designated wallet.
     *
     * @param agentContext The agent context
     * @param credentialOffer The credential offer
     * @param connection The connection
     *
     * @return The credential identifier of the stored credential record.
     */
    suspend fun processOffer(
        agentContext: IAgentContext,
        credentialOffer: CredentialOfferMessage,
        connection: ConnectionRecord?
    ): String

    /**
     * Create a credential request for the given record on a previously received offer.
     * The credential record must be in state "Offered".
     *
     * @param agentContext Agent context
     * @param credentialId The offer identifier
     */
    suspend fun createRequest(
        agentContext: IAgentContext,
        credentialId: String
    ): Pair<CredentialRequestMessage, CredentialRecord>

    /**
     * Create a credential based on an offer message. This is method is used for connectionless credential exchange.
     * The credential request message will be sent over transport with return routing and an issued credential is
     * expected in the response.
     * If successful, this method will return a credential record in "Issued" state.
     *
     * @param agentContext Agent context
     * @param message
     * @return A credential record that contains the final issued credential.
     */
    suspend fun createCredential(
        agentContext: IAgentContext,
        message: CredentialOfferMessage
    ): CredentialRecord?

    /**
     * Rejects a credential offer.
     *
     * @param agentContext Agent context
     * @param offerId The offer identifier
     *
     */
    suspend fun rejectOffer(agentContext: IAgentContext, offerId: String)

    /**
     * Processes the issued credential and stores in the designated wallet.
     * @param agentContext Agent context
     * @param credential The credential
     * @param connection The connection
     * @return The identifier for the credential record.
     */
    suspend fun processCredential(
        agentContext: IAgentContext,
        credential: CredentialIssueMessage,
        connection: ConnectionRecord?
    ): String

    /**
     * Create a new credential offer for the specified connection. If "connectionId" is
     * [null] this offer must be delivered over connectionless transport.
     * The credential data will be stored in a tag named "CredentialOfferData" that can be retrieved
     * at a later stage.
     * @param agentContext Agent context
     * @param config A configuration object used to configure the resulting offers presentation.
     * @param connectionId The connection id.
     * @return The offer message and the identifier.
     */
    suspend fun createOffer(
        agentContext: IAgentContext,
        config: OfferConfiguration,
        connectionId: String?
    ): Pair<CredentialOfferMessage, CredentialRecord>

    /**
     * Create a new credential offer for connectionless transport.
     * The credential data will be stored in a tag named "CredentialOfferData" that can be retrieved
     * at a later stage. The output [CredentialOfferMessage] will have
     * the "~service" decorator set.
     */
    suspend fun createOffer(
        agentContext: IAgentContext,
        config: OfferConfiguration
    ): Pair<CredentialOfferMessage, CredentialRecord>

    /**
     * Revokes a credential offer.
     * @param agentContext Agent context
     * @param offerId Id of the credential offer.
     */
    suspend fun revokeCredentialOffer(agentContext: IAgentContext, offerId: String) {

    }

    /**
     * Processes the credential request and stores in the designated wallet.
     * @param agentContext Agent context
     * @param credentialRequest The credential request
     * @param connection The connection
     * @return The credential identifier of the stored credential record.
     */
    suspend fun processCredentialRequest(
        agentContext: IAgentContext,
        credentialRequest: CredentialRequestMessage,
        connection: ConnectionRecord
    ): String

    /**
     * Creates a credential with the given credential identifier.
     * @param agentContext Agent context
     * @param credentialId The credential identifier
     */
    suspend fun createCredential(
        agentContext: IAgentContext,
        credentialId: String
    ): Pair<CredentialIssueMessage, CredentialRecord>

    /**
     * Creates a credential with the given credential identifier.
     * The credential is issued with the attributeValues provided.
     * @param agentContext Agent context
     * @param credentialId The credential request identifier
     * @param values
     */
    suspend fun createCredential(
        agentContext: IAgentContext,
        credentialId: String,
        values: List<CredentialPreviewAttribute>
    ): Pair<CredentialIssueMessage, CredentialRecord>

    /**
     * Rejects a credential request.
     * @param agentContext Agent context
     * @param credentialId The credential identifier
     */
    suspend fun rejectCredentialRequest(agentContext: IAgentContext, credentialId: String)

    /**
     * Revokes an issued credentials and writes the updated revocation state to the ledger
     * @param agentContext Agent context.
     * @param credentialId Identifier of the credential to be revoked.
     */
    suspend fun revokeCredential(agentContext: IAgentContext, credentialId: String)

    /**
     * Deletes the credential and it's associated record.
     */
    suspend fun deleteCredential(agentContext: IAgentContext, credentialId: String)
}