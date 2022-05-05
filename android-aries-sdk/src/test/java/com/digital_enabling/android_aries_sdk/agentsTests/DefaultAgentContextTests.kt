package com.digital_enabling.android_aries_sdk.agentsTests

import com.digital_enabling.android_aries_sdk.agents.DefaultAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.MessageContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class DefaultAgentContextTests {

    private val mockMessageContext = Mockito.mock(MessageContext::class.java)

    //region Tests for addNext
    @Test
    @DisplayName("AddNext method adds a message context correctly to the message queue.")
    fun addNext_addsCorrectly(){
        val agentContext = DefaultAgentContext()
        agentContext.addNext(mockMessageContext)

        assertEquals(mockMessageContext, agentContext.tryGetNext().second)
    }
    //endregion

    //region Tests for TryGetNext
    @Test
    @DisplayName("TryGetNext method gets and deletes one element from the message queue.")
    fun tryGetNext_oneElementInQueue(){
        val agentContext = DefaultAgentContext()

        agentContext.addNext(mockMessageContext)

        assertTrue(agentContext.tryGetNext().first)
    }

    @Test
    @DisplayName("TryGetNext method returns false trying to delete from an empty message queue.")
    fun tryGetNext_noElementInQueue(){
        val agentContext = DefaultAgentContext()
        assertFalse(agentContext.tryGetNext().first)
    }

    @Test
    @DisplayName("TryGetNext method returns and removes the message queue elements in the right order.")
    fun tryGetNext_multipleElements(){
        val mockMessageContext1 = Mockito.mock(MessageContext::class.java)
        val mockMessageContext2 = Mockito.mock(MessageContext::class.java)
        val mockMessageContext3 = Mockito.mock(MessageContext::class.java)

        val agentContext = DefaultAgentContext()
        agentContext.addNext(mockMessageContext1)
        agentContext.addNext(mockMessageContext2)
        agentContext.addNext(mockMessageContext3)

        assertEquals(mockMessageContext1, agentContext.tryGetNext().second)
        assertEquals(mockMessageContext2, agentContext.tryGetNext().second)
        assertEquals(mockMessageContext3, agentContext.tryGetNext().second)
    }
    //endregion
}