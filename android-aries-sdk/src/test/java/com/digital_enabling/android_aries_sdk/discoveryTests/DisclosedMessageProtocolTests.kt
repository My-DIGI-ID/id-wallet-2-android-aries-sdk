package com.digital_enabling.android_aries_sdk.discoveryTests

import com.digital_enabling.android_aries_sdk.discovery.DisclosedMessageProtocol
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DisclosedMessageProtocolTests {

    @Test
    @DisplayName("Serialization works.")
    fun canSerialize(){
        val testObject = DisclosedMessageProtocol("testPid", "testRoles")

        val expected = "{\"pid\":\"testPid\",\"roles\":\"testRoles\"}"
        val actual = Json.encodeToString(testObject)

        assertEquals(expected, actual)
    }


    @Test
    @DisplayName("Can be properly deserialized when all properties exist.")
    fun canDeserialize(){
        val testObject = DisclosedMessageProtocol("testPid", "testRoles")

        val testString = "{\"pid\": \"testPid\", \"roles\": \"testRoles\"}"

        val deserializedObject = Json.decodeFromString<DisclosedMessageProtocol>(testString)

        assertEquals(testObject.protocolId, deserializedObject.protocolId)
        assertEquals(testObject.roles, deserializedObject.roles)
    }

}