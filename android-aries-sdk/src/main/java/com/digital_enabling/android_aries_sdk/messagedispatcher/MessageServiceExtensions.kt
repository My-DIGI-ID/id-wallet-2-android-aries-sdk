package com.digital_enabling.android_aries_sdk.messagedispatcher

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext
import com.digital_enabling.android_aries_sdk.decorators.transport.ReturnRouteTypes
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord

/**
 * Message Service Extensions
 */

/**
 * Sends the message and receives a response by adding return routing decorator
 * according to the Routing RFC.
 * It also tries to cast the response to an expected type of [AgentMessage]
 * @param agentContext The agent context.
 * @param message The message.
 * @param connection The connection.
 * @param returnType The returnType.
 * @param additionalHeaders List of additionalHeaders.
 * @return The response as type TOutput of [AgentMessage]
 */
suspend inline fun <reified TInput: AgentMessage, reified TOutput : AgentMessage> IMessageService.sendReceiveExtended(
    agentContext: IAgentContext,
    message: TInput,
    connection: ConnectionRecord,
    returnType: ReturnRouteTypes = ReturnRouteTypes.ALL,
    additionalHeaders: List<Pair<String, String>> = listOf()
): TOutput {
    val response =
        sendReceive(agentContext, message, connection, returnType, additionalHeaders)

    if (response is UnpackedMessageContext) {
        return response.getMessage()
    }
    throw UnsupportedOperationException("Couldn't cast the message to the expected type or response was invalid")
}

/**
 * Sends the message and receives a response by adding return routing decorator
 * according to the Routing RFC.
 * It also tries to cast the response to an expected type of [AgentMessage]
 * @param agentContext The agent context.
 * @param message The message.
 * @param recipientKey The recipientKey.
 * @param endpointUri The endpointUri.
 * @param routingKeys The routingKeys.
 * @param senderKey The senderKey.
 * @param returnType The returnType.
 * @param additionalHeaders List of additionalHeaders.
 * @return The response as type TOutput of [AgentMessage]
 */
suspend inline fun <reified TInput: AgentMessage, reified TOutput : AgentMessage> IMessageService.sendReceiveExtended(
    agentContext: IAgentContext,
    message: TInput,
    recipientKey: String,
    endpointUri: String,
    routingKeys: Array<String>?,
    senderKey: String?,
    returnType: ReturnRouteTypes = ReturnRouteTypes.ALL,
    additionalHeaders: List<Pair<String, String>> = listOf()
): TOutput {
    var response =
        sendReceive(agentContext, message, recipientKey, endpointUri, routingKeys, senderKey, returnType, additionalHeaders)
    if (response is UnpackedMessageContext) {
        return response.getMessage()
    }
    throw UnsupportedOperationException("Couldn't cast the message to the expected type or response was invalid")
}