package com.digital_enabling.android_aries_sdk.didexchangeTests.models.dids

import com.digital_enabling.android_aries_sdk.didexchange.models.dids.DidDoc
import com.digital_enabling.android_aries_sdk.didexchange.models.dids.DidDocKey
import com.digital_enabling.android_aries_sdk.didexchange.models.dids.IndyAgentDidDocService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.lang.Exception

class DidDocTests {
    @Test
    @DisplayName("Serialization with all properties set works.")
    fun serialization_allPropertiesSet(){
        val testKey = DidDocKey("testId","testType", "testController", "testKey")
        val testService = IndyAgentDidDocService("testId", "testType", listOf("testRecipientKey"), listOf("testRoutingKey"), "testEndpoint")
        val testObject = DidDoc("testContext", "testId", listOf(testKey), listOf(testService))

        val expected = "{\"@context\":\"testContext\",\"id\":\"testId\",\"publicKey\":[{\"id\":\"testId\",\"type\":\"testType\",\"controller\":\"testController\",\"publicKeyBase58\":\"testKey\"}],\"service\":[{\"id\":\"testId\",\"type\":\"testType\",\"recipientKeys\":[\"testRecipientKey\"],\"routingKeys\":[\"testRoutingKey\"],\"serviceEndpoint\":\"testEndpoint\"}]}"
        val actual = Json.encodeToString(testObject)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Deserializing with all properties set works.")
    fun deserialization_allPropertiesSet(){
        val testKey = DidDocKey("testId","testType", "testController", "testKey")
        val testService = IndyAgentDidDocService("testId", "testType", listOf("testRecipientKey"), listOf("testRoutingKey"), "testEndpoint")

        val testJson = "{\"@context\":\"testContext\",\"id\":\"testId\",\"publicKey\":[{\"id\":\"testId\",\"type\":\"testType\",\"controller\":\"testController\",\"publicKeyBase58\":\"testKey\"}],\"service\":[{\"id\":\"testId\",\"type\":\"testType\",\"recipientKeys\":[\"testRecipientKey\"],\"routingKeys\":[\"testRoutingKey\"],\"serviceEndpoint\":\"testEndpoint\"}]}"
        val actualObject = Json.decodeFromString<DidDoc>(testJson)

        Assertions.assertEquals("testContext", actualObject.context)
        Assertions.assertEquals("testId", actualObject.id)
        Assertions.assertEquals(listOf(testKey), actualObject.keys)
        Assertions.assertEquals(listOf(testService), actualObject.services)
    }

    @Test
    @DisplayName("Deserializing with missing id throws exception.")
    fun deserialization_missingId(){
        val testJson = "{\"@context\":\"testContext\",\"publicKey\":[{\"id\":\"testId\",\"type\":\"testType\",\"controller\":\"testController\",\"publicKeyBase58\":\"testKey\"}],\"service\":[{\"id\":\"testId\",\"type\":\"testType\",\"recipientKeys\":[\"testRecipientKey\"],\"routingKeys\":[\"testRoutingKey\"],\"serviceEndpoint\":\"testEndpoint\"}]}"

        assertThrows<Exception> { Json.decodeFromString<DidDoc>(testJson)}
    }

    @Test
    @DisplayName("Deserializing with missing keys throws exception.")
    fun deserialization_missingKeys(){
        val testJson = "{\"@context\":\"testContext\",\"id\":\"testId\",\"service\":[{\"id\":\"testId\",\"type\":\"testType\",\"recipientKeys\":[\"testRecipientKey\"],\"routingKeys\":[\"testRoutingKey\"],\"serviceEndpoint\":\"testEndpoint\"}]}"

        assertThrows<Exception> { Json.decodeFromString<DidDoc>(testJson)}
    }

    @Test
    @DisplayName("Deserializing with missing service throws exception.")
    fun deserialization_missingService(){
        val testJson = "{\"@context\":\"testContext\",\"id\":\"testId\",\"publicKey\":[{\"id\":\"testId\",\"type\":\"testType\",\"controller\":\"testController\",\"publicKeyBase58\":\"testKey\"}]}"

        assertThrows<Exception> { Json.decodeFromString<DidDoc>(testJson)}
    }
}