package com.digital_enabling.android_aries_sdk.messagedispatcher

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.MessageContext
import com.digital_enabling.android_aries_sdk.agents.models.PackedMessageContext
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext
import com.digital_enabling.android_aries_sdk.decorators.transport.ReturnRouteTypes
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import org.hyperledger.indy.sdk.wallet.Wallet

/**
 * Router service.
 */
interface IMessageService {
    fun unpack(
        wallet: Wallet,
        message: PackedMessageContext,
        senderKey: String
    ): UnpackedMessageContext

    /**
     * Sends the agent message to the endpoint asynchronously.
     * @param agentContext The agentContext.
     * @param message The message.
     * @param recipientKey The recipients key.
     * @param endpointUri The destination endpoint.
     * @param routingKeys The routing keys.
     * @param senderKey The senders key.
     * @param additionalHeaders List of additionalHeaders
     */
    suspend fun <TInput: AgentMessage> send(
        agentContext: IAgentContext,
        message: TInput,
        recipientKey: String,
        endpointUri: String,
        routingKeys: Array<String>? = null,
        senderKey: String? = null,
        additionalHeaders: List<Pair<String, String>> = listOf()
    )

    /**
     * Sends the agent message.
     * @param agentContext The agentContext.
     * @param message The message.
     * @param connection The connection.
     * @param additionalHeaders List of additionalHeaders
     */
    suspend fun <TInput: AgentMessage> send(
        agentContext: IAgentContext,
        message: TInput,
        connection: ConnectionRecord,
        additionalHeaders: List<Pair<String, String>> = listOf()
    )

    /**
     * Sends the message and receives a response by adding return routing decorator
     * according to the Routing RFC.
     * @param agentContext The agentContext.
     * @param message The message.
     * @param recipientKey The recipients key.
     * @param endpointUri The destination endpoint.
     * @param routingKeys The routing keys.
     * @param senderKey The senders key.
     * @param returnType The type of return routing.
     * @param additionalHeaders List of additionalHeaders
     * @return The response as a message context object if return routing requested
     */
    suspend fun <TInput: AgentMessage> sendReceive(
        agentContext: IAgentContext,
        message: TInput,
        recipientKey: String,
        endpointUri: String,
        routingKeys: Array<String>? = null,
        senderKey: String? = null,
        returnType: ReturnRouteTypes = ReturnRouteTypes.ALL,
        additionalHeaders: List<Pair<String, String>> = listOf()
    ): MessageContext?

    /**
     * Sends the message and receives a response by adding return routing decorator
     * according to the Routing RFC.
     * @param agentContext The agentContext.
     * @param message The message.
     * @param connection The connection.
     * @param returnType The type of return routing.
     * @param additionalHeaders List of additionalHeaders
     * @return The response as a message context object if return routing requested
     */
    suspend fun <TInput: AgentMessage> sendReceive(
        agentContext: IAgentContext,
        message: TInput,
        connection: ConnectionRecord,
        returnType: ReturnRouteTypes,
        additionalHeaders: List<Pair<String, String>> = listOf()
    ): MessageContext?

    fun getDispatcher(scheme: String): IMessageDispatcher?
}