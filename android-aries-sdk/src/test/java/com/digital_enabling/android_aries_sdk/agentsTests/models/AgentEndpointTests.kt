package com.digital_enabling.android_aries_sdk.agentsTests.models

import com.digital_enabling.android_aries_sdk.agents.models.AgentEndpoint
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AgentEndpointTests {

    @Test
    @DisplayName("toString method with filled did, uri and one existing verkey")
    fun toString_FilledVerkey() {
        //Arrange
        val testObject = AgentEndpoint()
        testObject.did = "testDid"
        testObject.uri = "testUri"
        testObject.verkey = arrayOf("testVerkey")

        //Act
        val expected =
            "AgentEndpoint: Did=testDid, Verkey=[hidden], Uri=testUri"
        val actual = testObject.toString()

        //Assert
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("toString method with filled did, uri and no existing verkey")
    fun toString_EmptyVerkey() {
        //Arrange
        val testObject = AgentEndpoint()
        testObject.did = "testDid"
        testObject.uri = "testUri"
        testObject.verkey = emptyArray()

        //Act
        val expected =
            "AgentEndpoint: Did=testDid, Verkey=null, Uri=testUri"
        val actual = testObject.toString()

        //Assert
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("toString method with did not initialized")
    fun toString_NoDid() {
        //Arrange
        val testObject = AgentEndpoint()
        testObject.uri = "testUri"
        testObject.verkey = emptyArray()

        //Act
        val expected =
            "AgentEndpoint: Did=null, Verkey=null, Uri=testUri"
        val actual = testObject.toString()

        //Assert
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("toString method with uri not initialized")
    fun toString_NoUri() {
        //Arrange
        val testObject = AgentEndpoint()
        testObject.did = "testDid"
        testObject.verkey = emptyArray()

        //Act
        val expected =
            "AgentEndpoint: Did=testDid, Verkey=null, Uri=null"
        val actual = testObject.toString()

        //Assert
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("toString method with verkey not initialized")
    fun toString_NoVerkey() {
        //Arrange
        val testObject = AgentEndpoint()
        testObject.did = "testDid"
        testObject.uri = "testUri"

        //Act
        val actual = testObject.toString()
        val expected = "AgentEndpoint: Did=testDid, Verkey=null, Uri=testUri"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Serialized AgentEndpoint has key did")
    fun serialization_didExists() {
        //Arrange
        val testObject = AgentEndpoint()
        testObject.did = "testDid"
        testObject.uri = "testUri"
        testObject.verkey = emptyArray()

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject)
        val actual = (serializedObject as JsonObject).containsKey("did")

        //Assert
        assertTrue(actual)
    }

    @Test
    @DisplayName("Serialized AgentEndpoint has key uri")
    fun serialization_uriExists() {
        //Arrange
        val testObject = AgentEndpoint()
        testObject.did = "testDid"
        testObject.uri = "testUri"
        testObject.verkey = emptyArray()

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject)
        val actual = (serializedObject as JsonObject).containsKey("uri")

        //Assert
        assertTrue(actual)
    }

    @Test
    @DisplayName("Serialized AgentEndpoint has key verkey")
    fun serialization_verkeyExists() {
        //Arrange
        val testObject = AgentEndpoint()
        testObject.did = "testDid"
        testObject.uri = "testUri"
        testObject.verkey = emptyArray()

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject)
        val actual = (serializedObject as JsonObject).containsKey("verkey")

        //Assert
        assertTrue(actual)
    }

    @Test
    @DisplayName("Serialized AgentEndpoint can be deserialized")
    fun canDeserialize() {
        //Arrange
        val testObject = AgentEndpoint()
        testObject.did = "testDid"
        testObject.uri = "testUri"
        testObject.verkey = emptyArray()

        val testString = "{\"did\":\"testDid\", \"verkey\":[], \"uri\":\"testUri\"}"

        //Act
        val deserializedObject = Json.decodeFromString<AgentEndpoint>(testString)

        //Assert
        assertEquals(testObject.did, deserializedObject.did)
        assertEquals(testObject.uri, deserializedObject.uri)
        assertArrayEquals(testObject.verkey, deserializedObject.verkey)
    }

    @Test
    @DisplayName("AgentEndpoint can be constructed from another AgentEndpoint")
    fun construction_fromAgentEndpoint() {
        //Arrange
        val testAgentEndpoint = AgentEndpoint()
        testAgentEndpoint.did = "testDid"
        testAgentEndpoint.uri = "testUri"
        testAgentEndpoint.verkey = arrayOf("testValue")

        //Act
        val testObject = AgentEndpoint(testAgentEndpoint)

        //Assert
        assertEquals(testAgentEndpoint.did, testObject.did)
        assertEquals(testAgentEndpoint.uri, testObject.uri)
        assertArrayEquals(testAgentEndpoint.verkey, testObject.verkey)
    }
}