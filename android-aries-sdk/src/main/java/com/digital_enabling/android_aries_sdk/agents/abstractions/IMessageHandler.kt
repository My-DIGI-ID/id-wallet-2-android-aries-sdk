package com.digital_enabling.android_aries_sdk.agents.abstractions

import com.digital_enabling.android_aries_sdk.agents.MessageType
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext

interface IMessageHandler {
    /**
     * Supported message types.
     */
    val supportedMessageTypes: Iterable<MessageType>

    /**
     * Processes the agent message.
     * @param agentContext The agent context.
     * @param messageContext The message context.
     *
     * @return Outgoing message context.
     */
    suspend fun process(
        agentContext: IAgentContext,
        messageContext: UnpackedMessageContext
    ): AgentMessage?
}