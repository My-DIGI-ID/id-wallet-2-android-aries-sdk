package com.digital_enabling.android_aries_sdk.didexchange

import com.digital_enabling.android_aries_sdk.agents.MessageType
import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.abstractions.IMessageHandler
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionInvitationMessage
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRequestMessage
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionResponseMessage
import com.digital_enabling.android_aries_sdk.messagedispatcher.IMessageService
import com.digital_enabling.android_aries_sdk.utils.TagConstants

/**
 * Initializes a new instance of the [DefaultConnectionHandler] class.
 * @param connectionService The connection service.
 * @param messageService The message service.
 */
class DefaultConnectionHandler(
    val connectionService: IConnectionService,
    val messageService: IMessageService
) : IMessageHandler {

    /**
     * The supported message types.
     */
    override val supportedMessageTypes: List<MessageType> = listOf(
        MessageType(MessageTypes.CONNECTION_INVITATION),
        MessageType(MessageTypes.CONNECTION_REQUEST),
        MessageType(MessageTypes.CONNECTION_RESPONSE),
        MessageType(MessageTypesHttps.CONNECTION_INVITATION),
        MessageType(MessageTypesHttps.CONNECTION_REQUEST),
        MessageType(MessageTypesHttps.CONNECTION_RESPONSE),
        MessageType(MessageTypesHttps.CONNECTION_RESPONSE),
    )

    /**
     * Processes the agent message
     * @param agentContext Agent context.
     * @param messageContext The agent message agentContext.
     * @return
     * @throws AriesFrameworkException Unsupported message type {message.Type}
     */
    override suspend fun process(
        agentContext: IAgentContext,
        messageContext: UnpackedMessageContext
    ): AgentMessage? {

        val messageContextConnection =
            messageContext.connection ?: throw Exception("Connection not found.")
        when (messageContext.getMessageType()) {
            MessageTypesHttps.CONNECTION_INVITATION,
            MessageTypes.CONNECTION_INVITATION -> {
                val invitation = messageContext.getMessage<ConnectionInvitationMessage>()
                connectionService.createRequest(
                    agentContext,
                    invitation
                )
                return null
            }

            MessageTypesHttps.CONNECTION_REQUEST,
            MessageTypes.CONNECTION_REQUEST -> {
                val request = messageContext.getMessage<ConnectionRequestMessage>()
                val connectionId = connectionService.processRequest(
                    agentContext,
                    request,
                    messageContextConnection
                )
                messageContext.contextRecord = messageContext.connection

                if (messageContextConnection.getTag(TagConstants.AUTO_ACCEPT_CONNECTION) == "true") {
                    val (message, record) = connectionService.createResponse(
                        agentContext,
                        connectionId
                    )
                    messageContext.contextRecord = record
                    return message
                }
                return null
            }

            MessageTypesHttps.CONNECTION_RESPONSE,
            MessageTypes.CONNECTION_RESPONSE -> {
                val response = messageContext.getMessage<ConnectionResponseMessage>()
                connectionService.processResponse(
                    agentContext,
                    response,
                    messageContextConnection
                )
                messageContext.contextRecord = messageContext.connection
                return null
            }

            else -> {
                throw AriesFrameworkException(
                    ErrorCode.INVALID_MESSAGE,
                    "Unsupported message type ${messageContext.getMessageType()}"
                )
            }
        }
    }
}