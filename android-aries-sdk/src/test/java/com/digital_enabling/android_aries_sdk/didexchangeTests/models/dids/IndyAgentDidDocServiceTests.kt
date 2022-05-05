package com.digital_enabling.android_aries_sdk.didexchangeTests.models.dids

import com.digital_enabling.android_aries_sdk.didexchange.models.dids.DidDocServiceEndpointTypes
import com.digital_enabling.android_aries_sdk.didexchange.models.dids.IndyAgentDidDocService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class IndyAgentDidDocServiceTests {

    @Test
    @DisplayName("Serializing with all properties set works.")
    fun serialization_allPropertiesSet(){
        val testObject = IndyAgentDidDocService("testId", "testType", listOf("testRecipientKey"), listOf("testRoutingKey"), "testEndpoint")

        val expected = "{\"id\":\"testId\",\"type\":\"testType\",\"recipientKeys\":[\"testRecipientKey\"],\"routingKeys\":[\"testRoutingKey\"],\"serviceEndpoint\":\"testEndpoint\"}"
        val actual = Json.encodeToString(testObject)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Deserializing with all properties set works.")
    fun deserialization_allPropertiesSet(){
        val testJson = "{\"id\":\"testId\",\"type\":\"testType\",\"recipientKeys\":[\"testRecipientKey\"],\"routingKeys\":[\"testRoutingKey\"],\"serviceEndpoint\":\"testEndpoint\"}"
        val actualObject = Json.decodeFromString<IndyAgentDidDocService>(testJson)

        assertEquals("testId", actualObject.id)
        assertEquals("testType", actualObject.type)
        assertEquals(listOf("testRecipientKey"), actualObject.recipientKeys)
        assertEquals(listOf("testRoutingKey"), actualObject.routingKeys)
        assertEquals("testEndpoint", actualObject.serviceEndpoint)
    }

    @Test
    @DisplayName("Deserializing with missing id throws exception.")
    fun deserialization_missingId(){
        val testJson = "{\"type\":\"testType\",\"recipientKeys\":[\"testRecipientKey\"],\"routingKeys\":[\"testRoutingKey\"],\"serviceEndpoint\":\"testEndpoint\"}"

        assertThrows<Exception> { Json.decodeFromString<IndyAgentDidDocService>(testJson) }
    }

    @Test
    @DisplayName("Deserializing with missing recipientKeys throws exception.")
    fun deserialization_missingRecipientKeys(){
        val testJson = "{\"id\":\"testId\",\"type\":\"testType\",\"routingKeys\":[\"testRoutingKey\"],\"serviceEndpoint\":\"testEndpoint\"}"

        assertThrows<Exception> { Json.decodeFromString<IndyAgentDidDocService>(testJson) }
    }

    @Test
    @DisplayName("Deserializing with missing routingKeys throws exception.")
    fun deserialization_missingRoutingKeys(){
        val testJson = "{\"id\":\"testId\",\"type\":\"testType\",\"recipientKeys\":[\"testRecipientKey\"],\"serviceEndpoint\":\"testEndpoint\"}"

        assertThrows<Exception> { Json.decodeFromString<IndyAgentDidDocService>(testJson) }
    }

    @Test
    @DisplayName("Deserializing with missing serviceEndpoint throws exception.")
    fun deserialization_missinEndpoint(){
        val testJson = "{\"id\":\"testId\",\"type\":\"testType\",\"recipientKeys\":[\"testRecipientKey\"],\"routingKeys\":[\"testRoutingKey\"]}"

        assertThrows<Exception> { Json.decodeFromString<IndyAgentDidDocService>(testJson) }
    }
}