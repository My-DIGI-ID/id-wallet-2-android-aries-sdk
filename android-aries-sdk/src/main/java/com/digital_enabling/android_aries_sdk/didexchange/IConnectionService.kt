package com.digital_enabling.android_aries_sdk.didexchange

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.didexchange.models.*
import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery

interface IConnectionService {
    /**
     * Gets the ConnectionRecord.
     * @param agentContext Agent context.
     * @param connectionId Connection identifier.
     * @return The connection record.
     */
    suspend fun get(
        agentContext: IAgentContext,
        connectionId: String
    ): ConnectionRecord

    /**
     * Retrieves a list of [ConnectionRecord] items for the given search criteria
     * @param agentContext Agent context.
     * @param query The query used to filter the search results.
     * @param count The maximum item count of items to return to return.
     * @param skip The number of items to skip.
     * @return The list of [ConnectionRecord].
     */
    suspend fun list(
        agentContext: IAgentContext,
        query: ISearchQuery? = null,
        count: Int = 100,
        skip: Int = 0
    ): List<ConnectionRecord>

    /**
     * Creates the invitation.
     * @param agentContext Agent context.
     * @param config An optional configuration object used to configure the resulting invitations presentation
     * @return The invitation.
     */
    suspend fun createInvitation(
        agentContext: IAgentContext,
        config: InviteConfiguration? = null
    ): Pair<ConnectionInvitationMessage, ConnectionRecord>

    /**
     * Revokes an invitation.
     * @param agentContext Agent Context.
     * @param invitationId Id of the invitation.
     * @throws AriesFrameworkException with ErrorCode.RecordNotFound.
     * @throws AriesFrameworkException with ErrorCode.RecordInInvalidState.
     * @return The invitation.
     */
    suspend fun revokeInvitation(
        agentContext: IAgentContext,
        invitationId: String
    )

    /**
     * Accepts the connection invitation.
     * @param agentContext Agent context.
     * @param invitation Invitation
     * @throws AriesFrameworkException with ErrorCode.A2AMessageTransmissionError.
     * @return Connection identifier unique for this connection.
     */
    suspend fun createRequest(
        agentContext: IAgentContext,
        invitation: ConnectionInvitationMessage
    ): Pair<ConnectionRequestMessage, ConnectionRecord>

    /**
     * Process the connection request for a given connection.
     * @param agentContext Agent Context.
     * @param request Request.
     * @param connection Connection.
     * @throws AriesFrameworkException with ErrorCode.A2AMessageTransmissionError.
     * @return Connection identifier this request is related to.
     */
    suspend fun processRequest(
        agentContext: IAgentContext,
        request: ConnectionRequestMessage,
        connection: ConnectionRecord
    ): String

    /**
     * Accepts the connection request and sends a connection response.
     * @param agentContext Agent Context.
     * @param connectionId Connection identifier.
     * @throws AriesFrameworkException with ErrorCode.A2AMessageTransmissionError
     * @throws AriesFrameworkException with ErrorCode.RecordNotFound.
     * @throws AriesFrameworkException with ErrorCode.RecordInInvalidState.
     * @return The response.
     */
    suspend fun createResponse(
        agentContext: IAgentContext,
        connectionId: String
    ): Pair<ConnectionResponseMessage, ConnectionRecord>

    /**
     * Processes the connection response for a given connection.
     * @param agentContext Agent context.
     * @param response Response.
     * @param connection Connection.
     * @return Connection identifier this request is related to.
     */
    suspend fun processResponse(
        agentContext: IAgentContext,
        response: ConnectionResponseMessage,
        connection: ConnectionRecord
    ): String

    /**
     * Deletes a connection from the local store.
     * @param agentContext Agent context.
     * @param connectionId Connection identifier.
     * @return The response with a boolean indicating if deletion occured successfully.
     */
    suspend fun delete(
        agentContext: IAgentContext,
        connectionId: String
    ): Boolean

    /**
     * Retrieves a ConnectionRecord by key.
     * @param agentContext Agent Context.
     * @param myKey My verkey.
     */
    suspend fun resolveByMyKey(agentContext: IAgentContext, myKey: String): ConnectionRecord?
}