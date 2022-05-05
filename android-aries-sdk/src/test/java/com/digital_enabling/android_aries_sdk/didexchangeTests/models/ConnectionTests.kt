package com.digital_enabling.android_aries_sdk.didexchangeTests.models

import com.digital_enabling.android_aries_sdk.didexchange.models.Connection
import com.digital_enabling.android_aries_sdk.didexchange.models.dids.DidDoc
import com.digital_enabling.android_aries_sdk.didexchange.models.dids.DidDocKey
import com.digital_enabling.android_aries_sdk.didexchange.models.dids.IndyAgentDidDocService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ConnectionTests {

    private val testKey = DidDocKey("testId","testType", "testController", "testKey")
    private val testService = IndyAgentDidDocService("testId", "testType", listOf("testRecipientKey"), listOf("testRoutingKey"), "testEndpoint")
    private val testDidDoc = DidDoc("testContext", "testId", listOf(testKey), listOf(testService))

    @Test
    @DisplayName("Serialization works with all properties set.")
    fun serialization_allPropertiesSet(){
        val testObject = Connection("testDid", testDidDoc)

        val expected = "{\"DID\":\"testDid\",\"DIDDoc\":{\"@context\":\"testContext\",\"id\":\"testId\",\"publicKey\":[{\"id\":\"testId\",\"type\":\"testType\",\"controller\":\"testController\",\"publicKeyBase58\":\"testKey\"}],\"service\":[{\"id\":\"testId\",\"type\":\"testType\",\"recipientKeys\":[\"testRecipientKey\"],\"routingKeys\":[\"testRoutingKey\"],\"serviceEndpoint\":\"testEndpoint\"}]}}"
        val actual = Json.encodeToString(testObject)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Deserialization works with all properties set.")
    fun deserialization_allPropertiesSet(){
        val testJson = "{\"DID\":\"testDid\",\"DIDDoc\":{\"@context\":\"testContext\",\"id\":\"testId\",\"publicKey\":[{\"id\":\"testId\",\"type\":\"testType\",\"controller\":\"testController\",\"publicKeyBase58\":\"testKey\"}],\"service\":[{\"id\":\"testId\",\"type\":\"testType\",\"recipientKeys\":[\"testRecipientKey\"],\"routingKeys\":[\"testRoutingKey\"],\"serviceEndpoint\":\"testEndpoint\"}]}}"
        val actualObject = Json.decodeFromString<Connection>(testJson)

        assertEquals("testDid", actualObject.did)
        assertEquals(testDidDoc, actualObject.didDoc)
    }

    @Test
    @DisplayName("Deserialization throws exception when did is missing.")
    fun deserialization_missingDid(){
        val testJson = "{\"DIDDoc\":{\"@context\":\"testContext\",\"id\":\"testId\",\"publicKey\":[{\"id\":\"testId\",\"type\":\"testType\",\"controller\":\"testController\",\"publicKeyBase58\":\"testKey\"}],\"service\":[{\"id\":\"testId\",\"type\":\"testType\",\"recipientKeys\":[\"testRecipientKey\"],\"routingKeys\":[\"testRoutingKey\"],\"serviceEndpoint\":\"testEndpoint\"}]}}"
        assertThrows<Exception> { Json.decodeFromString<Connection>(testJson) }
    }

    @Test
    @DisplayName("Deserialization throws exception when didDoc is missing.")
    fun deserialization_missingDidDoc(){
        val testJson = "{\"DID\":\"testDid\"}"
        assertThrows<Exception> { Json.decodeFromString<Connection>(testJson) }
    }

}