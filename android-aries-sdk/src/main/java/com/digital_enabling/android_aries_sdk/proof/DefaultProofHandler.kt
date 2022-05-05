package com.digital_enabling.android_aries_sdk.proof

import com.digital_enabling.android_aries_sdk.agents.MessageType
import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.abstractions.IMessageHandler
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.proof.messages.PresentationMessage
import com.digital_enabling.android_aries_sdk.proof.messages.ProposePresentationMessage
import com.digital_enabling.android_aries_sdk.proof.messages.RequestPresentationMessage

class DefaultProofHandler(val proofService: IProofService) : IMessageHandler {

    /**
     * The supported message types.
     */
    override val supportedMessageTypes: Iterable<MessageType> = listOf(
        MessageType(MessageTypes.PresentProofNames.PRESENTATION),
        MessageType(MessageTypes.PresentProofNames.PROPOSE_PRESENTATION),
        MessageType(MessageTypes.PresentProofNames.REQUEST_PRESENTATION),
        MessageType(MessageTypesHttps.PresentProofNames.PRESENTATION),
        MessageType(MessageTypesHttps.PresentProofNames.PROPOSE_PRESENTATION),
        MessageType(MessageTypesHttps.PresentProofNames.REQUEST_PRESENTATION)
    )

    /**
     * Processes the agent message
     * @param agentContext Agent context.
     * @param messageContext The agent message agentContext.
     * @exception [AriesFrameworkException] Unsupported message type {messageType}
     */
    override suspend fun process(
        agentContext: IAgentContext,
        messageContext: UnpackedMessageContext
    ): AgentMessage? {
        when (messageContext.getMessageType()) {
            MessageTypes.PresentProofNames.PROPOSE_PRESENTATION,
            MessageTypesHttps.PresentProofNames.PROPOSE_PRESENTATION -> {
                val message = messageContext.getMessage<ProposePresentationMessage>()
                val connection = messageContext.connection ?: throw IllegalArgumentException("Connection is null")
                val record = proofService.processProposal(agentContext, message, connection)

                messageContext.contextRecord = record
                return null
            }
            MessageTypes.PresentProofNames.REQUEST_PRESENTATION,
            MessageTypesHttps.PresentProofNames.REQUEST_PRESENTATION -> {
                val message = messageContext.getMessage<RequestPresentationMessage>()
                val record = proofService.processRequest(agentContext, message, messageContext.connection)

                messageContext.contextRecord = record
                return null
            }
            MessageTypes.PresentProofNames.PRESENTATION,
            MessageTypesHttps.PresentProofNames.PRESENTATION -> {
                val message = messageContext.getMessage<PresentationMessage>()
                val record = proofService.processPresentation(agentContext, message)

                messageContext.contextRecord = record
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