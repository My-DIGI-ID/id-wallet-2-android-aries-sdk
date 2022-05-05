package com.digital_enabling.android_aries_sdk.agents.abstractions

import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext

/**
 * Agent middleware used to process a message after the message handler processing
 */
interface IAgentMiddleware {
    /**
     * Called when the message needs to be processed
     * @param agentContext The agent context.
     * @param messageContext The message context.
     */
    fun onMessage(agentContext: IAgentContext, messageContext: UnpackedMessageContext)
}