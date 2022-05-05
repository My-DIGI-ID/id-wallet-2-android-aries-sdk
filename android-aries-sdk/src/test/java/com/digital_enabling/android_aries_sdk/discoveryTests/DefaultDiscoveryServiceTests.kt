package com.digital_enabling.android_aries_sdk.discoveryTests

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.IEventAggregator
import com.digital_enabling.android_aries_sdk.discovery.DefaultDiscoveryService
import com.digital_enabling.android_aries_sdk.discovery.DiscoveryQueryMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException

class DefaultDiscoveryServiceTests {

    private val mockEventAggregator = mock(IEventAggregator::class.java)

    //region Tests for createQuery
    @Test
    @DisplayName("CreateQuery method creates proper http DiscoveryQueryMessage when given the right arguments.")
    fun createQuery_properArgumentsForHttp(){
        val testObject = DefaultDiscoveryService(mockEventAggregator)
        val mockAgentContext = mock(IAgentContext::class.java)
        val mockQuery = "testQuery"

        val expected = DiscoveryQueryMessage()
        expected.query = mockQuery
        val actual = testObject.createQuery(mockAgentContext, mockQuery)

        assertEquals(expected.query, actual.query)
        assertEquals(expected.type, actual.type)
    }

    @Test
    @DisplayName("CreateQuery method creates proper https DiscoveryQueryMessage when given the right arguments.")
    fun createQuery_properArgumentsForHttps(){
        val testObject = DefaultDiscoveryService(mockEventAggregator)
        val mockAgentContext = mock(IAgentContext::class.java)
        Mockito.`when`(mockAgentContext.useMessageTypesHttps).thenReturn(true)

        val mockQuery = "testQuery"

        val expected = DiscoveryQueryMessage(true)
        expected.query = mockQuery
        val actual = testObject.createQuery(mockAgentContext, mockQuery)

        assertEquals(expected.query, actual.query)
        assertEquals(expected.type, actual.type)
    }

    @Test
    @DisplayName("CreateQuery method throws IllegalArgumentException when query string is empty.")
    fun createQuery_missingQuery(){
        val testObject = DefaultDiscoveryService(mockEventAggregator)
        val mockAgentContext = mock(IAgentContext::class.java)

        assertThrows<IllegalArgumentException> { testObject.createQuery(mockAgentContext, "")  }
    }
    //endregion

    //region Tests for createQueryResponse
    @Test
    @DisplayName("CreateQueryResponse method throws IllegalArgumentException when message query is empty.")
    fun createQueryResponse_noMessageQuery(){
        val testObject = DefaultDiscoveryService(mockEventAggregator)
        val mockAgentContext = mock(IAgentContext::class.java)
        val testMessage = DiscoveryQueryMessage()
        testMessage.query = "testQuery"

        assertThrows<AriesFrameworkException> { testObject.createQueryResponse(mockAgentContext, testMessage) }
    }

    //endregion
}