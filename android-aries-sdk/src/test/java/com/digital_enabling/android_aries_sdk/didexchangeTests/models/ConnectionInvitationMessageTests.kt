package com.digital_enabling.android_aries_sdk.didexchangeTests.models

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionInvitationMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ConnectionInvitationMessageTests {

    //region Tests for serialization
    @Test
    @DisplayName("Serialization works if all properties are properly set.")
    fun serialization_allPropertiesSet(){
        val testObject = ConnectionInvitationMessage()
        testObject.id = "testId"
        testObject.label = "testLabel"
        testObject.imageUrl = "testUrl"
        testObject.serviceEndpoint = "testEndpoint"
        testObject.recipientKeys = listOf("testRecipient")
        testObject.routingKeys = listOf("testRouting")

        val expected = "{\"@id\":\"testId\",\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/invitation\",\"label\":\"testLabel\",\"imageUrl\":\"testUrl\",\"serviceEndpoint\":\"testEndpoint\",\"routingKeys\":[\"testRouting\"],\"recipientKeys\":[\"testRecipient\"]}"
        val actual = Json.encodeToString(testObject)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Deserialization works properly if all properties are set.")
    fun deserialization_allPropertiesSet(){
        val testJson = "{\"@id\":\"testId\",\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/invitation\",\"label\":\"testLabel\",\"imageUrl\":\"testUrl\",\"serviceEndpoint\":\"testEndpoint\",\"routingKeys\":[\"testRouting\"],\"recipientKeys\":[\"testRecipient\"]}"
        val actualObject = Json.decodeFromString<ConnectionInvitationMessage>(testJson)

        assertEquals("testId", actualObject.id)
        assertEquals("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/invitation", actualObject.type)
        assertEquals("testLabel", actualObject.label)
        assertEquals("testUrl", actualObject.imageUrl)
        assertEquals("testEndpoint", actualObject.serviceEndpoint)
        assertEquals(listOf("testRecipient"), actualObject.recipientKeys)
        assertEquals(listOf("testRouting"), actualObject.routingKeys)
    }

    @Test
    @DisplayName("Deserialization works properly if label property is missing.")
    fun deserialization_missingLabel(){
        val testJson = "{\"@id\":\"testId\",\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/invitation\",\"imageUrl\":\"testUrl\",\"serviceEndpoint\":\"testEndpoint\",\"routingKeys\":[\"testRouting\"],\"recipientKeys\":[\"testRecipient\"]}"
        val actualObject = Json.decodeFromString<ConnectionInvitationMessage>(testJson)

        assertEquals("testId", actualObject.id)
        assertEquals("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/invitation", actualObject.type)
        assertEquals("", actualObject.label)
        assertEquals("testUrl", actualObject.imageUrl)
        assertEquals("testEndpoint", actualObject.serviceEndpoint)
        assertEquals(listOf("testRecipient"), actualObject.recipientKeys)
        assertEquals(listOf("testRouting"), actualObject.routingKeys)
    }

    @Test
    @DisplayName("Deserialization works properly if imageUrl property is missing.")
    fun deserialization_missingImageUrl(){
        val testJson = "{\"@id\":\"testId\",\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/invitation\",\"label\":\"testLabel\",\"serviceEndpoint\":\"testEndpoint\",\"routingKeys\":[\"testRouting\"],\"recipientKeys\":[\"testRecipient\"]}"
        val actualObject = Json.decodeFromString<ConnectionInvitationMessage>(testJson)

        assertEquals("testId", actualObject.id)
        assertEquals("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/invitation", actualObject.type)
        assertEquals("testLabel", actualObject.label)
        assertEquals("", actualObject.imageUrl)
        assertEquals("testEndpoint", actualObject.serviceEndpoint)
        assertEquals(listOf("testRecipient"), actualObject.recipientKeys)
        assertEquals(listOf("testRouting"), actualObject.routingKeys)
    }

    @Test
    @DisplayName("Deserialization works properly if serviceEndpoint property is missing.")
    fun deserialization_missingSerivceEndpoint(){
        val testJson = "{\"@id\":\"testId\",\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/invitation\",\"label\":\"testLabel\",\"imageUrl\":\"testUrl\",\"routingKeys\":[\"testRouting\"],\"recipientKeys\":[\"testRecipient\"]}"
        val actualObject = Json.decodeFromString<ConnectionInvitationMessage>(testJson)

        assertEquals("testId", actualObject.id)
        assertEquals("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/invitation", actualObject.type)
        assertEquals("testLabel", actualObject.label)
        assertEquals("testUrl", actualObject.imageUrl)
        assertEquals("", actualObject.serviceEndpoint)
        assertEquals(listOf("testRecipient"), actualObject.recipientKeys)
        assertEquals(listOf("testRouting"), actualObject.routingKeys)
    }

    @Test
    @DisplayName("Deserialization works properly if routingKeys proeprty is missing.")
    fun deserialization_missingRoutingKeys(){
        val testJson = "{\"@id\":\"testId\",\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/invitation\",\"label\":\"testLabel\",\"imageUrl\":\"testUrl\",\"serviceEndpoint\":\"testEndpoint\",\"recipientKeys\":[\"testRecipient\"]}"
        val actualObject = Json.decodeFromString<ConnectionInvitationMessage>(testJson)

        assertEquals("testId", actualObject.id)
        assertEquals("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/invitation", actualObject.type)
        assertEquals("testLabel", actualObject.label)
        assertEquals("testUrl", actualObject.imageUrl)
        assertEquals("testEndpoint", actualObject.serviceEndpoint)
        assertEquals(listOf("testRecipient"), actualObject.recipientKeys)
        assertEquals(emptyList<String>(), actualObject.routingKeys)
    }

    @Test
    @DisplayName("Deserialization works properly if recipientKeys property is missing.")
    fun deserialization_missingRecipientKeys(){
        val testJson = "{\"@id\":\"testId\",\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/invitation\",\"label\":\"testLabel\",\"imageUrl\":\"testUrl\",\"serviceEndpoint\":\"testEndpoint\",\"routingKeys\":[\"testRouting\"]}"
        val actualObject = Json.decodeFromString<ConnectionInvitationMessage>(testJson)

        assertEquals("testId", actualObject.id)
        assertEquals("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/invitation", actualObject.type)
        assertEquals("testLabel", actualObject.label)
        assertEquals("testUrl", actualObject.imageUrl)
        assertEquals("testEndpoint", actualObject.serviceEndpoint)
        assertEquals(emptyList<String>(), actualObject.recipientKeys)
        assertEquals(listOf("testRouting"), actualObject.routingKeys)
    }
    //endregion

    //region Tests for constructor
    @Test
    @DisplayName("Messagetype is https if constructor parameter is set true.")
    fun constructor_withParameter(){
        val testObject = ConnectionInvitationMessage(true)
        assertEquals(MessageTypesHttps.CONNECTION_INVITATION, testObject.type)
    }

    @Test
    @DisplayName("Messagetype is http if constructor parameter is missing")
    fun constructor_withoutParameter(){
        val testObject = ConnectionInvitationMessage()
        assertEquals(MessageTypes.CONNECTION_INVITATION, testObject.type)
    }
    //endregion
}