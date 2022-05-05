package com.digital_enabling.android_aries_sdk.agents

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgent
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentProvider
import com.digital_enabling.android_aries_sdk.agents.extensions.getSupportedMessageTypes
import com.digital_enabling.android_aries_sdk.configuration.AgentOptions
import com.digital_enabling.android_aries_sdk.ledger.abstractions.IPoolService
import com.digital_enabling.android_aries_sdk.ledger.models.PoolAwaitable
import com.digital_enabling.android_aries_sdk.wallet.IWalletService

class DefaultAgentProvider(
    private val agentOptions: AgentOptions,
    private val defaultAgent: IAgent,
    private val walletService: IWalletService,
    private val poolService: IPoolService
) : IAgentProvider {

    /**
     * @see IAgentProvider
     */
    override fun getAgent(args: Array<Any>): IAgent {
        return defaultAgent
    }

    /**
     * @see IAgentProvider
     */
    override suspend fun getContext(args: Array<Any>): IAgentContext {
        val agent = getAgent(args)
        val agentContext = DefaultAgentContext()
        agentContext.wallet = walletService.getWallet(
            agentOptions.walletConfiguration,
            agentOptions.walletCredentials
        )
        agentContext.pool = PoolAwaitable {
            poolService.getPool(
                agentOptions.poolName,
                agentOptions.protocolVersion
            )
        }
        agentContext.supportedMessages = agent.getSupportedMessageTypes()
        agentContext.agent = agent
        agentContext.useMessageTypesHttps = agentOptions.useMessageTypesHttps
        return agentContext
    }
}