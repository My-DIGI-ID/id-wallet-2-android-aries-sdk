package com.digital_enabling.android_aries_sdk.discoveryTests

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.discovery.DiscoveryQueryMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals

class DiscoveryQueryMessageTests {

    //region Tests for constructor
    @Test
    @DisplayName("Constructor call without any arguments returns correct http DISCOVERY_DISCLOSE_MESSAGE_TYPE.")
    fun constructor_withoutArgument(){
        val testObject = DiscoveryQueryMessage()

        val expectedMessageType = MessageTypes.DISCOVERY_QUERY_MESSAGE_TYPE
        val actualMessageType = testObject.type

        Assertions.assertEquals(expectedMessageType, actualMessageType)
    }

    @Test
    @DisplayName("Constructor call with argument returns correct https DISCOVERY_DISCLOSE_MESSAGE_TYPE")
    fun constructor_withArgument(){
        val testObject = DiscoveryQueryMessage(true)

        val expectedMessageType = MessageTypesHttps.DISCOVERY_QUERY_MESSAGE_TYPE
        val actualMessageType = testObject.type

        Assertions.assertEquals(expectedMessageType, actualMessageType)
    }
    //endregion

    //region Tests for serialization
    @Test
    @DisplayName("Throws no exception when query is missing while serializing")
    fun serialisation_missingQuery(){
        val testObject = DiscoveryQueryMessage()

        assertDoesNotThrow { Json.encodeToJsonElement(testObject) }
    }

    @Test
    @DisplayName("Object can be serialized to json without comment property.")
    fun serialisation_missingComment(){
        val testObject = DiscoveryQueryMessage()
        testObject.query = "testQuery"

        val serializedObject = Json.encodeToJsonElement(testObject)
        val actual = Json.decodeFromJsonElement<String>((serializedObject as JsonObject)["query"]!!)

        assertEquals(testObject.query, actual)
    }

    @Test
    @DisplayName("Serialized object can be deserialized from json.")
    fun canDeserialize(){
        val testObject = DiscoveryQueryMessage()
        testObject.query = "testQuery"
        testObject.comment = "testComment"
        testObject.id = "testId"

        val testJsonString = "{\"@id\": \"testId\", \"@type\": \"${MessageTypes.DISCOVERY_QUERY_MESSAGE_TYPE}\" \"query\": \"testQuery\", \"comment\": \"testComment\"}"
        val jsonObject = Json.decodeFromString<DiscoveryQueryMessage>(testJsonString)

        assertEquals(testObject.id, jsonObject.id)
        assertEquals(testObject.type, jsonObject.type)
        assertEquals(testObject.query, jsonObject.query)
        assertEquals(testObject.comment, jsonObject.comment)
    }
    //endregion
}