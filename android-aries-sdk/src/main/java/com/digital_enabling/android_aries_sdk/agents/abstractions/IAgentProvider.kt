package com.digital_enabling.android_aries_sdk.agents.abstractions

/**
 * Agent context provider.
 */
interface IAgentProvider {
    /**
     * Retrieves an agent context.
     * @param args Arguments.
     * @return The agent context.
     */
    suspend fun getContext(args: Array<Any>) : IAgentContext

    /**
     * Retrieves an agent instance.
     * @param args Arguments.
     * @return The agent.
     */
    fun getAgent(args: Array<Any>) : IAgent
}