package com.digital_enabling.android_aries_sdk.discovery

import com.digital_enabling.android_aries_sdk.agents.MessageHandlerBase
import com.digital_enabling.android_aries_sdk.agents.MessageType
import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode

/**
 * Default discovery message handler.
 */
class DefaultDiscoveryHandler(private val discoveryService: IDiscoveryService) :
    MessageHandlerBase<DiscoveryQueryMessage>() {
    /**
     * The supported message types.
     */
    override val supportedMessageTypes = listOf(
        MessageType(MessageTypes.DISCOVERY_QUERY_MESSAGE_TYPE),
        MessageType(MessageTypesHttps.DISCOVERY_QUERY_MESSAGE_TYPE)
    )

    override fun process(
        message: AgentMessage,
        agentContext: IAgentContext,
        messageContext: UnpackedMessageContext
    ): AgentMessage {
        if (message is DiscoveryQueryMessage) {
            return discoveryService.createQueryResponse(agentContext, message)
        } else {
            throw AriesFrameworkException(
                ErrorCode.INVALID_MESSAGE,
                "Invalid message type. Expected 'DiscoveryQueryMessage', found '${message::class.java.simpleName}'"
            )
        }
    }
}