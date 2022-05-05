package com.digital_enabling.android_aries_sdk.didexchangeTests.models

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.decorators.DecoratorNames
import com.digital_enabling.android_aries_sdk.decorators.transport.ReturnRouteTypes
import com.digital_enabling.android_aries_sdk.decorators.transport.TransportDecorator
import com.digital_enabling.android_aries_sdk.didexchange.models.Connection
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRequestMessage
import com.digital_enabling.android_aries_sdk.didexchange.models.dids.DidDoc
import com.digital_enabling.android_aries_sdk.didexchange.models.dids.DidDocKey
import com.digital_enabling.android_aries_sdk.didexchange.models.dids.IndyAgentDidDocService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals

class ConnectionRequestMessageTests {

    private val testKey = DidDocKey("testId","testType", "testController", "testKey")
    private val testService = IndyAgentDidDocService("testId", "testType", listOf("testRecipientKey"), listOf("testRoutingKey"), "testEndpoint")
    private val testDidDoc = DidDoc("testContext", "testId", listOf(testKey), listOf(testService))
    private val testConnection = Connection("testDid", testDidDoc)


    //region Tests for serialization
    @Test
    @DisplayName("Serialization works properly if connection property is set.")
    fun serialization_allPropertiesSet(){
        val testObject = ConnectionRequestMessage()
        testObject.id = "testId"
        testObject.connection = testConnection

        val expected = "{\"@id\":\"testId\",\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/request\",\"connection\":{\"DID\":\"testDid\",\"DIDDoc\":{\"@context\":\"testContext\",\"id\":\"testId\",\"publicKey\":[{\"id\":\"testId\",\"type\":\"testType\",\"controller\":\"testController\",\"publicKeyBase58\":\"testKey\"}],\"service\":[{\"id\":\"testId\",\"type\":\"testType\",\"recipientKeys\":[\"testRecipientKey\"],\"routingKeys\":[\"testRoutingKey\"],\"serviceEndpoint\":\"testEndpoint\"}]}}}"
        val actual = Json.encodeToString(testObject)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Deserialization works properly if all properties are set.")
    fun deserialization_allPropertiesSet(){
        val testJson = "{\"@id\":\"testId\",\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/request\",\"label\":\"testLabel\",\"imageUrl\":\"testUrl\",\"connection\":{\"DID\":\"testDid\",\"DIDDoc\":{\"@context\":\"testContext\",\"id\":\"testId\",\"publicKey\":[{\"id\":\"testId\",\"type\":\"testType\",\"controller\":\"testController\",\"publicKeyBase58\":\"testKey\"}],\"service\":[{\"id\":\"testId\",\"type\":\"testType\",\"recipientKeys\":[\"testRecipientKey\"],\"routingKeys\":[\"testRoutingKey\"],\"serviceEndpoint\":\"testEndpoint\"}]}}}"
        val actual = Json.decodeFromString<ConnectionRequestMessage>(testJson)

        assertEquals("testId", actual.id)
        assertEquals("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/request", actual.type)
        assertEquals("testLabel", actual.label)
        assertEquals("testUrl", actual.imageUrl)
        assertEquals(testConnection.did, actual.connection!!.did)
    }

    @Test
    @DisplayName("Deserialization works properly if label property is missing.")
    fun deserialization_missingLabel(){
        val testJson = "{\"@id\":\"testId\",\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/request\",\"imageUrl\":\"testUrl\",\"connection\":{\"DID\":\"testDid\",\"DIDDoc\":{\"@context\":\"testContext\",\"id\":\"testId\",\"publicKey\":[{\"id\":\"testId\",\"type\":\"testType\",\"controller\":\"testController\",\"publicKeyBase58\":\"testKey\"}],\"service\":[{\"id\":\"testId\",\"type\":\"testType\",\"recipientKeys\":[\"testRecipientKey\"],\"routingKeys\":[\"testRoutingKey\"],\"serviceEndpoint\":\"testEndpoint\"}]}}}"
        val actual = Json.decodeFromString<ConnectionRequestMessage>(testJson)

        assertEquals("testId", actual.id)
        assertEquals("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/request", actual.type)
        assertEquals("", actual.label)
        assertEquals("testUrl", actual.imageUrl)
        assertEquals(testConnection.did, actual.connection!!.did)
    }

    @Test
    @DisplayName("Deserialization works properly if imageUrl property is missing.")
    fun deserialization_missingimageUrl(){
        val testJson = "{\"@id\":\"testId\",\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/request\",\"label\":\"testLabel\",\"connection\":{\"DID\":\"testDid\",\"DIDDoc\":{\"@context\":\"testContext\",\"id\":\"testId\",\"publicKey\":[{\"id\":\"testId\",\"type\":\"testType\",\"controller\":\"testController\",\"publicKeyBase58\":\"testKey\"}],\"service\":[{\"id\":\"testId\",\"type\":\"testType\",\"recipientKeys\":[\"testRecipientKey\"],\"routingKeys\":[\"testRoutingKey\"],\"serviceEndpoint\":\"testEndpoint\"}]}}}"
        val actual = Json.decodeFromString<ConnectionRequestMessage>(testJson)

        assertEquals("testId", actual.id)
        assertEquals("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/request", actual.type)
        assertEquals("testLabel", actual.label)
        assertEquals("", actual.imageUrl)
        assertEquals(testConnection.did, actual.connection!!.did)
    }

    @Test
    @DisplayName("Deserialization throws no Exception if connection property is missing.")
    fun deserialization_missingConnection(){
        //Assign
        val testJson = "{\"@id\":\"testId\",\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/request\",\"label\":\"testLabel\",\"imageUrl\":\"testUrl\"}"

        //Act
        val actual = Json.decodeFromString<ConnectionRequestMessage>(testJson)

        //Assert
        Assertions.assertTrue(actual.connection == null)
    }
    //endregion

    //region Tests for constructor
    @Test
    @DisplayName("Messagetype is https if constructor parameter is set true.")
    fun constructor_withParameter(){
        val testObject = ConnectionRequestMessage(true)
        assertEquals(MessageTypesHttps.CONNECTION_REQUEST, testObject.type)
    }

    @Test
    @DisplayName("Messagetype is http if constructor parameter is missing.")
    fun constructor_withoutParameter(){
        val testObject = ConnectionRequestMessage()
        assertEquals(MessageTypes.CONNECTION_REQUEST, testObject.type)
    }
    //endregion

    //region Tests for toString
    @Test
    @DisplayName("ToString method works properly if all properties are set.")
    fun toString_allPropertiesSet(){
        val testConnection = Connection("testDid", testDidDoc)

        val testObject = ConnectionRequestMessage()
        testObject.id = "testId"
        testObject.label = "testLabel"
        testObject.imageUrl = "testUrl"
        testObject.connection = testConnection

        val expected = "ConnectionRequestMessage: Id=testId, Type=did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/request, Did=testDid, Name=testLabel, ImageUrl=testUrl"
        val actual = testObject.toString()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("ToString method works if every property is missing.")
    fun toString_missingConnection(){
        val testObject = ConnectionRequestMessage()
        assertDoesNotThrow { testObject.toString() }
    }
    //endregion
}