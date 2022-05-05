package com.digital_enabling.android_aries_sdk.agentsTests

import com.digital_enabling.android_aries_sdk.agents.DefaultAgent
import com.digital_enabling.android_aries_sdk.agents.DefaultAgentContext
import com.digital_enabling.android_aries_sdk.agents.DefaultAgentProvider
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.extensions.getSupportedMessageTypes
import com.digital_enabling.android_aries_sdk.configuration.AgentOptions
import com.digital_enabling.android_aries_sdk.koin.DiInitializier
import com.digital_enabling.android_aries_sdk.koin.KoinInteractionProvider
import com.digital_enabling.android_aries_sdk.ledger.abstractions.IPoolService
import com.digital_enabling.android_aries_sdk.ledger.models.PoolAwaitable
import com.digital_enabling.android_aries_sdk.wallet.IWalletService
import kotlinx.coroutines.runBlocking
import org.hyperledger.indy.sdk.pool.Pool
import org.hyperledger.indy.sdk.wallet.Wallet
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.koin.core.component.get
import org.mockito.Mockito
import org.mockito.kotlin.any

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultAgentProviderTests {

    private val mockWalletService = Mockito.mock(IWalletService::class.java)
    private val mockPoolService = Mockito.mock(IPoolService::class.java)
    private val koinProvider = KoinInteractionProvider()

    @BeforeAll
    fun setup(): Unit = runBlocking {
        DiInitializier.setupDi()
    }

    //region Tests for getAgent
    @Test
    @DisplayName("GetAgent method returns the agent set in constructor.")
    fun getAgent_defaultSettings() {
        val mockAgentOptions = AgentOptions()
        val mockAgent = DefaultAgent(
            koinProvider.get(),
            koinProvider.get(),
            koinProvider.get(),
            koinProvider.get(),
            koinProvider.get(),
            koinProvider.get(),
            koinProvider.get(),
            koinProvider.get(),
            koinProvider.get()
        )

        val agentProvider =
            DefaultAgentProvider(mockAgentOptions, mockAgent, mockWalletService, mockPoolService)
        val actualAgent = agentProvider.getAgent(emptyArray())

        assertEquals(mockAgent, actualAgent)
    }
    //endregion

    //region Tests for getContext
    @Test
    @DisplayName("GetContext method returns the agent context with the set default settings.")
    fun getContext_defaultSettings() {
        val mockAgentOptions = AgentOptions()
        val mockAgent = DefaultAgent(
            koinProvider.get(),
            koinProvider.get(),
            koinProvider.get(),
            koinProvider.get(),
            koinProvider.get(),
            koinProvider.get(),
            koinProvider.get(),
            koinProvider.get(),
            koinProvider.get()
        )
        val mockWallet = Mockito.mock(Wallet::class.java)
        val mockPool = Mockito.mock(Pool::class.java)

        runBlocking {
            Mockito.`when`(mockWalletService.getWallet(any(), any())).thenReturn(mockWallet)
            Mockito.`when`(mockPoolService.getPool(any(), any())).thenReturn(mockPool)
        }

        val agentProvider =
            DefaultAgentProvider(mockAgentOptions, mockAgent, mockWalletService, mockPoolService)
        val expectedContext = DefaultAgentContext()
        expectedContext.wallet = mockWallet
        expectedContext.pool = PoolAwaitable { mockPool }
        expectedContext.supportedMessages = mockAgent.getSupportedMessageTypes()
        expectedContext.agent = mockAgent
        expectedContext.useMessageTypesHttps = mockAgentOptions.useMessageTypesHttps

        var actualContext: IAgentContext?
        runBlocking {
            actualContext = agentProvider.getContext(emptyArray())
        }

        assertEquals(expectedContext.wallet, actualContext?.wallet)
        assertEquals(expectedContext.supportedMessages, actualContext?.supportedMessages)
        assertEquals(expectedContext.agent, actualContext?.agent)
        assertEquals(expectedContext.useMessageTypesHttps, actualContext?.useMessageTypesHttps)
    }
    //endregion
}