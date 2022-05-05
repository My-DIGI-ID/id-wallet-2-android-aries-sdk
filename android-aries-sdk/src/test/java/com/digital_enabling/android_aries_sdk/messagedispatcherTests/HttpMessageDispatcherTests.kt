package com.digital_enabling.android_aries_sdk.messagedispatcherTests

import com.digital_enabling.android_aries_sdk.agents.DefaultAgent
import com.digital_enabling.android_aries_sdk.agents.DefaultAgentProvider
import com.digital_enabling.android_aries_sdk.configuration.AgentOptions
import com.digital_enabling.android_aries_sdk.koin.DiInitializier
import com.digital_enabling.android_aries_sdk.koin.KoinInteractionProvider
import com.digital_enabling.android_aries_sdk.ledger.DefaultPoolService
import com.digital_enabling.android_aries_sdk.wallet.DefaultWalletService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.koin.core.component.get

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpMessageDispatcherTests {
    private val poolService = DefaultPoolService()
    private val walletService = DefaultWalletService()
    private val koinProvider = KoinInteractionProvider()

    @BeforeAll
    fun setup(): Unit = runBlocking {
        DiInitializier.setupDi()
    }

    @Test
    @DisplayName("dispatch method works")
    fun dispatch_works() = runBlocking {
        val agent = DefaultAgent(
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
        val agentOptions = AgentOptions()
        val agentProvider = DefaultAgentProvider(agentOptions, agent, walletService, poolService)
    }
}