package com.digital_enabling.android_aries_sdk.discovery

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext

interface IDiscoveryService {
    /**
     * Creates a discovery query message.
     * @param agentContext Agent Context.
     * @param query Query for message.
     * @return A discovery query message.
     */
    fun createQuery(agentContext: IAgentContext, query: String): DiscoveryQueryMessage

    /**
     * Creates a discovery disclose message from a query message.
     * @param agentContext Agent Context.
     * @param message Query message.
     * @return A discovery disclose message.
     */
    fun createQueryResponse(agentContext: IAgentContext, message: DiscoveryQueryMessage): DiscoveryDiscloseMessage
}