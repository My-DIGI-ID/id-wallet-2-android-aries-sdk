package com.digital_enabling.android_aries_sdk.discoveryTests

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext
import com.digital_enabling.android_aries_sdk.discovery.DefaultDiscoveryHandler
import com.digital_enabling.android_aries_sdk.discovery.DiscoveryDiscloseMessage
import com.digital_enabling.android_aries_sdk.discovery.DiscoveryQueryMessage
import com.digital_enabling.android_aries_sdk.discovery.IDiscoveryService
import com.digital_enabling.android_aries_sdk.routing.ForwardMessage
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import java.util.HashMap

class DefaultDiscoveryHandlerTests {

    private val mockDiscoveryService = mock(IDiscoveryService::class.java)
    private val mockAgentContext = mock(IAgentContext::class.java)

    @Test
    @DisplayName("Process method calls createQueryResponse() from given DiscoveryService.")
    fun process_createsQuery() = runBlocking {
        val mockDiscoveryQueryMessage = DiscoveryQueryMessage()

        val testSenderVerkey = "testSenderVerkey"
        val testCreateInboxMessage = ForwardMessage()
        testCreateInboxMessage.id = "testId"
        testCreateInboxMessage.type = "testType"
        testCreateInboxMessage.to = "testTo"
        testCreateInboxMessage.message = JsonObject(HashMap())

        val testUnpackedMessageContext = UnpackedMessageContext(Json.encodeToString(testCreateInboxMessage), testSenderVerkey)

        Mockito.`when`(
            mockDiscoveryService.createQueryResponse(
                any(),
                any()
            )
        ).thenReturn(DiscoveryDiscloseMessage())

        val testObject = DefaultDiscoveryHandler(mockDiscoveryService)
        val mockAgentContext = mock(IAgentContext::class.java)

        val actual = testObject.process(mockDiscoveryQueryMessage, mockAgentContext, testUnpackedMessageContext)

        assertTrue(actual is DiscoveryDiscloseMessage)
    }
}