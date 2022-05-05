package com.digital_enabling.android_aries_sdk.didexchangeTests.models.dids

import com.digital_enabling.android_aries_sdk.didexchange.models.dids.DidDocKey
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.Exception

class DidDocKeyTests {

    @Test
    @DisplayName("Serialization with all properties set works.")
    fun serialization_allPropertiesSet(){
        val testObject = DidDocKey("testId","testType", "testController", "testKey")

        val expected = "{\"id\":\"testId\",\"type\":\"testType\",\"controller\":\"testController\",\"publicKeyBase58\":\"testKey\"}"
        val actual = Json.encodeToString(testObject)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Deserializing with all properties set works.")
    fun deserialization_allPropertiesSet(){
        val testJson = "{\"id\":\"testId\",\"type\":\"testType\",\"controller\":\"testController\",\"publicKeyBase58\":\"testKey\"}"
        val actualObject = Json.decodeFromString<DidDocKey>(testJson)

        assertEquals("testId", actualObject.id)
        assertEquals("testType", actualObject.type)
        assertEquals("testController", actualObject.controller)
        assertEquals("testKey", actualObject.publicKeyBase58)
    }

    @Test
    @DisplayName("Deserializing with missing id throws exception.")
    fun deserialization_missingId(){
        val testJson = "{\"type\":\"testType\",\"controller\":\"testController\",\"publicKeyBase58\":\"testKey\"}"

        assertThrows<Exception> {Json.decodeFromString<DidDocKey>(testJson)}
    }

    @Test
    @DisplayName("Deserializing with missing type throws exception.")
    fun deserialization_missingType(){
        val testJson = "{\"id\":\"testId\",\"controller\":\"testController\",\"publicKeyBase58\":\"testKey\"}"
        assertThrows<Exception> {Json.decodeFromString<DidDocKey>(testJson)}
    }

    @Test
    @DisplayName("Deserializing with missing controller throws exception.")
    fun deserialization_missingController(){
        val testJson = "{\"id\":\"testId\",\"type\":\"testType\",\"publicKeyBase58\":\"testKey\"}"
        assertThrows<Exception> {Json.decodeFromString<DidDocKey>(testJson)}
    }

    @Test
    @DisplayName("Deserializing with missing publicKeyBase58 throws exception.")
    fun deserialization_missingKey(){
        val testJson = "{\"id\":\"testId\",\"type\":\"testType\",\"controller\":\"testController\"}"
        assertThrows<Exception> {Json.decodeFromString<DidDocKey>(testJson)}
    }
}