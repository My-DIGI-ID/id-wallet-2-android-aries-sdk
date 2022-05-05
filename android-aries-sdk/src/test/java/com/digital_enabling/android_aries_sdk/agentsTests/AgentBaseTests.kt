package com.digital_enabling.android_aries_sdk.agentsTests

import com.digital_enabling.android_aries_sdk.agents.DefaultAgent
import com.digital_enabling.android_aries_sdk.agents.DefaultAgentContext
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.abstractions.IMessageHandler
import com.digital_enabling.android_aries_sdk.agents.models.MessageContext
import com.digital_enabling.android_aries_sdk.koin.DiInitializier
import com.digital_enabling.android_aries_sdk.koin.KoinInteractionProvider
import com.digital_enabling.android_aries_sdk.messagedispatcher.IMessageService
import com.digital_enabling.android_aries_sdk.utils.IIndyWrapper
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.koin.core.component.get
import org.mockito.Mockito.mock

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AgentBaseTests {
    private val mockEngine = MockEngine { _ ->
        respond(
            content = ByteReadChannel("""{"ip":"127.0.0.1"}"""),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }
    private val mockIndy = mock(IIndyWrapper::class.java)
    private val mockMessageService = mock(IMessageService::class.java)
    private val koinProvider = KoinInteractionProvider()

    @BeforeAll
    fun setup(): Unit = runBlocking {
        DiInitializier.setupDi()
    }

    //region Tests for addHandler
    @Test
    @DisplayName("AddHandler method adds a messageHandler correctly.")
    fun addHandler_addsCorrectly() {
        val mockHandler = mock(IMessageHandler::class.java)

        val agentBase = DefaultAgent(
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
        agentBase.addHandler(mockHandler)

        assertTrue(agentBase.handlers.contains(mockHandler))
    }
    //endregion

    //region Tests for process
    @Test
    @DisplayName("Process method throws Exception when not provided with DefaultAgentContext.")
    fun process_invalidAgentContext() {
        val agentBase = DefaultAgent(
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
        val mockAgentContext = mock(IAgentContext::class.java)
        val mockMessageContext = mock(MessageContext::class.java)

        val testClient = HttpClient(mockEngine)

        runBlocking {
            assertThrows<Exception> { agentBase.process(mockAgentContext, mockMessageContext) }
        }
    }

    @Test
    @DisplayName("Process method throws Exception when no handler is set.")
    fun process_notMessageHandler() {
        val agentBase = DefaultAgent(
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
        val mockAgentContext = DefaultAgentContext()
        val mockMessageContext = mock(MessageContext::class.java)
        val testClient = HttpClient(mockEngine)

        Assertions.assertDoesNotThrow { runBlocking {
            agentBase.process(
                mockAgentContext,
                mockMessageContext
            )
        } }
    }

    @Test
    @DisplayName("Process method processes a single unpacked messageContext correctly.")
    fun process_singleUnpackedMessageContext() {

    }

    @Test
    @DisplayName("Process method processes multiple messageContexts correctly.")
    fun process_multipleMessageContexts() {

    }
    //endregion

}