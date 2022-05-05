package com.digital_enabling.android_aries_sdk.agents.abstractions

import com.digital_enabling.android_aries_sdk.agents.models.MessageContext

/**
 * Agent.
 */
interface IAgent {
    /**
     * Processes the message context.
     * @param context Context.
     * @param messageContext Context.
     * @return Message context.
     */
    suspend fun process(context: IAgentContext, messageContext: MessageContext): MessageContext?

    /**
     * The handlers.
     */
    var handlers: MutableList<IMessageHandler>
}