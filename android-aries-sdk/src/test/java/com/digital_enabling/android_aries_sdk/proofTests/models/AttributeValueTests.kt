package com.digital_enabling.android_aries_sdk.proofTests.models

import com.digital_enabling.android_aries_sdk.proof.models.AttributeFilter
import com.digital_enabling.android_aries_sdk.proof.models.AttributeValue
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AttributeValueTests {
    @Test
    @DisplayName("Serialized AttributeValue has all keys if the values are not null.")
    fun serialization_works() {
        //Arrange
        val testObject = AttributeValue("testAttributeName")
        testObject.value = "testAttributeValue"

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<AttributeValue>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.name, actual.name)
        Assertions.assertEquals(testObject.value, actual.value)
    }

    @Test
    @DisplayName("Serialized AttributeValue contains the key \"value\" even if it is null.")
    fun serialization_nullValue() {
        //Arrange
        val testObject = AttributeValue("testAttributeName")

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("value"))
    }
}