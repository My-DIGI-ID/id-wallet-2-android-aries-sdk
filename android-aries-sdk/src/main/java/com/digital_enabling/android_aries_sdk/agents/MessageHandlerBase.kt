package com.digital_enabling.android_aries_sdk.agents

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.abstractions.IMessageHandler
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext

abstract class MessageHandlerBase<T> protected constructor() : IMessageHandler where T : AgentMessage {

    private var supportedMessageType: String = ""
    override val supportedMessageTypes: List<MessageType> = mutableListOf()

    init {
        val message = AgentMessage() as T
        supportedMessageType = message::class.java.typeName
    }

    abstract fun process(
        message: AgentMessage,
        agentContext: IAgentContext,
        messageContext: UnpackedMessageContext
    ): AgentMessage?


    override suspend fun process(
        agentContext: IAgentContext,
        messageContext: UnpackedMessageContext
    ): AgentMessage? {
        return process(messageContext.getMessage(), agentContext, messageContext)
    }
}