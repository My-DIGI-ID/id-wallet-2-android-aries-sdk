package com.digital_enabling.android_aries_sdk.utilsTests

import com.digital_enabling.android_aries_sdk.utils.DecodedMessageType
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DecodedMessageTypeTests {

    @Test
    @DisplayName("Serialized DecodedMessageType has keys uri, messageFamilyName, messageVersion, messageName.")
    fun serialization_works() : Unit = runBlocking {
        //Arrange
        val testUri = "testUri"
        val testMessageFamilyName = "testMessageFamilyName"
        val testMessageVersion = "testMessageVersion"
        val testMessageName = "testMessageName"
        val testObject = DecodedMessageType(testUri, testMessageFamilyName, testMessageVersion, testMessageName)

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("uri"))
        Assertions.assertTrue(serializedObject.containsKey("messageFamilyName"))
        Assertions.assertTrue(serializedObject.containsKey("messageVersion"))
        Assertions.assertTrue(serializedObject.containsKey("messageName"))
    }
}