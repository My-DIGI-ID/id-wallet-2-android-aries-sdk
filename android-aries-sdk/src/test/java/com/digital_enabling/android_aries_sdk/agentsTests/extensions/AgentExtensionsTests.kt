package com.digital_enabling.android_aries_sdk.agentsTests.extensions

import com.digital_enabling.android_aries_sdk.agents.*
import com.digital_enabling.android_aries_sdk.agents.abstractions.IMessageHandler
import com.digital_enabling.android_aries_sdk.agents.extensions.getSupportedMessageTypes
import com.digital_enabling.android_aries_sdk.configuration.AgentOptions
import com.digital_enabling.android_aries_sdk.koin.DiInitializier
import com.digital_enabling.android_aries_sdk.koin.KoinInteractionProvider
import com.digital_enabling.android_aries_sdk.ledger.abstractions.IPoolService
import com.digital_enabling.android_aries_sdk.wallet.IWalletService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.koin.core.component.get
import org.mockito.Mockito

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AgentExtensionsTests {

    private val koinProvider = KoinInteractionProvider()

    @BeforeAll
    fun setup(): Unit = runBlocking {
        DiInitializier.setupDi()
    }

    @Test
    @DisplayName("Get an empty list when an agent has no handlers.")
    fun getSupportedMessageTypes_noHandlers() {
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
        val mockWalletService = Mockito.mock(IWalletService::class.java)
        val mockPoolService = Mockito.mock(IPoolService::class.java)

        val agentProvider =
            DefaultAgentProvider(mockAgentOptions, mockAgent, mockWalletService, mockPoolService)
        val agent = agentProvider.getAgent(emptyArray())

        assertEquals(emptyList<MessageType>(), agent.getSupportedMessageTypes())
    }

    @Test
    @DisplayName("Get an empty list when agent handlers have no supported types.")
    fun getSupportedMessageTypes_noTypes() {
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
        val mockHandler = Mockito.mock(IMessageHandler::class.java)
        Mockito.`when`(mockHandler.supportedMessageTypes).thenReturn(emptyList())
        mockAgent.addHandler(mockHandler)
        val mockWalletService = Mockito.mock(IWalletService::class.java)
        val mockPoolService = Mockito.mock(IPoolService::class.java)

        val agentProvider =
            DefaultAgentProvider(mockAgentOptions, mockAgent, mockWalletService, mockPoolService)
        val agent = agentProvider.getAgent(emptyArray())

        assertEquals(emptyList<MessageType>(), agent.getSupportedMessageTypes())
    }

    @Test
    @DisplayName("Get the correct list of supported types of one message handler.")
    fun getSupportedMessageTypes_getCorrectTypesOneHandler() {
        val mockTypes = listOf(
            MessageType(MessageTypes.BASIC_MESSAGE_TYPE),
            MessageType(MessageTypes.DISCOVERY_DISCLOSE_MESSAGE_TYPE),
            MessageType(MessageTypesHttps.BASIC_MESSAGE_TYPE)
        )

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
        val mockHandler = Mockito.mock(IMessageHandler::class.java)
        Mockito.`when`(mockHandler.supportedMessageTypes).thenReturn(mockTypes)
        mockAgent.addHandler(mockHandler)
        val mockWalletService = Mockito.mock(IWalletService::class.java)
        val mockPoolService = Mockito.mock(IPoolService::class.java)

        val agentProvider =
            DefaultAgentProvider(mockAgentOptions, mockAgent, mockWalletService, mockPoolService)
        val agent = agentProvider.getAgent(emptyArray())

        assertEquals(mockTypes, agent.getSupportedMessageTypes())
    }

    @Test
    @DisplayName("Get the correct (distinct) list of supported types of multiple message handler.")
    fun getSupportedMessageTypes_getCorrectTypesMultipleHandlers() {
        val mockTypes1 = listOf(
            MessageType(MessageTypes.BASIC_MESSAGE_TYPE),
            MessageType(MessageTypes.DISCOVERY_DISCLOSE_MESSAGE_TYPE),
            MessageType(MessageTypesHttps.BASIC_MESSAGE_TYPE)
        )

        val mockTypes2 = listOf(
            MessageType(MessageTypes.BASIC_MESSAGE_TYPE),
            MessageType(MessageTypes.TRUST_PING_MESSAGE_TYPE)
        )

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
        val mockHandler1 = Mockito.mock(IMessageHandler::class.java)
        Mockito.`when`(mockHandler1.supportedMessageTypes).thenReturn(mockTypes1)
        mockAgent.addHandler(mockHandler1)
        val mockHandler2 = Mockito.mock(IMessageHandler::class.java)
        Mockito.`when`(mockHandler2.supportedMessageTypes).thenReturn(mockTypes2)
        mockAgent.addHandler(mockHandler2)

        val mockWalletService = Mockito.mock(IWalletService::class.java)
        val mockPoolService = Mockito.mock(IPoolService::class.java)

        val agentProvider =
            DefaultAgentProvider(mockAgentOptions, mockAgent, mockWalletService, mockPoolService)
        val agent = agentProvider.getAgent(emptyArray())

        assertEquals((mockTypes1 + mockTypes2).distinct(), agent.getSupportedMessageTypes())
    }

}