package com.digital_enabling.android_aries_sdk.agentsTests.models

import com.digital_enabling.android_aries_sdk.agents.models.PackedMessageContext
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PackedMessageContextTests {
    @Test
    @DisplayName("PackedMessageContext can be constructed from ByteArray")
    fun constructor_fromByteArray() {
        //Arrange
        val testByteArray = ("testByteArray").toByteArray(Charsets.UTF_8)
        val testObject = PackedMessageContext(testByteArray)

        //Act
        val actualPacked = testObject.packed
        val actualPayload = testObject.payload

        //Assert
        Assertions.assertEquals(testByteArray, actualPayload)
        Assertions.assertTrue(actualPacked)
        Assertions.assertNull(testObject.connection)
        Assertions.assertNull(testObject.messageJson)
        Assertions.assertNull(testObject.contextRecord)
    }

    @Test
    @DisplayName("PackedMessageContext can be constructed from just a String")
    fun constructor_fromString() {
        //Arrange
        val testString = "testString"
        val testObject = PackedMessageContext(testString)

        //Act
        val actualPacked = testObject.packed
        val actualPayload = testObject.payload

        //Assert
        Assertions.assertArrayEquals(("testString").toByteArray(Charsets.UTF_8), actualPayload)
        Assertions.assertTrue(actualPacked)
        Assertions.assertNull(testObject.connection)
        Assertions.assertNull(testObject.messageJson)
        Assertions.assertNull(testObject.contextRecord)
    }

    @Test
    @DisplayName("PackedMessageContext can be constructed from JsonObject")
    fun constructor_fromJsonObject() {
        //Arrange
        val testMap = LinkedHashMap<String, JsonElement>()
        testMap["testKey"] = Json.encodeToJsonElement("testValue")
        val testJsonObject = JsonObject(testMap)
        val testObject = PackedMessageContext(testJsonObject)

        //Act
        val actualPacked = testObject.packed
        val actualPayload = testObject.payload

        //Assert
        Assertions.assertArrayEquals(testJsonObject.toString().toByteArray(Charsets.UTF_8), actualPayload)
        Assertions.assertTrue(actualPacked)
        Assertions.assertNull(testObject.connection)
        Assertions.assertNull(testObject.messageJson)
        Assertions.assertNull(testObject.contextRecord)
    }

    @Test
    @DisplayName("PackedMessageContext can be constructed from String and ConnectionRecord")
    fun constructor_fromStringAndConnectionRecord() {
        //Arrange
        val testString = "testString"
        val testRecord = ConnectionRecord("testId")
        val testObject = PackedMessageContext(testString, testRecord)

        //Act
        val actualPacked = testObject.packed
        val actualPayload = testObject.payload
        val actualConnection = testObject.connection

        //Assert
        Assertions.assertArrayEquals(("testString").toByteArray(Charsets.UTF_8), actualPayload)
        Assertions.assertEquals(testRecord, actualConnection)
        Assertions.assertTrue(actualPacked)
        Assertions.assertNull(testObject.messageJson)
        Assertions.assertNull(testObject.contextRecord)
    }
}