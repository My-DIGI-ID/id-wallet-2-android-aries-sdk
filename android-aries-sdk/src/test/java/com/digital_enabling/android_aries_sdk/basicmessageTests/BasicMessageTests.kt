package com.digital_enabling.android_aries_sdk.basicmessageTests

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.basicmessage.BasicMessage
import com.digital_enabling.android_aries_sdk.trustping.TrustPingMessage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class BasicMessageTests {
    @Test
    @DisplayName("Serialization of BasicMessage works.")
    fun serialization_works(){
        //Arrange
        val testObject = BasicMessage()
        testObject.content = "testContent"
        testObject.sentTime = "testTime"

        testObject.id = "testId"
        testObject.type = "testType"

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("content"))
        Assertions.assertTrue(serializedObject.containsKey("sent_time"))
    }

    @Test
    @DisplayName("BasicMessage constructor without parameters sets fields correctly.")
    fun constructor_without_parameters(){

        val testObject = BasicMessage()

        Assertions.assertNotNull(testObject.id)
        Assertions.assertEquals(MessageTypes.BASIC_MESSAGE_TYPE, testObject.type)
    }

    @Test
    @DisplayName("BasicMessage constructor with parameters sets fields correctly.")
    fun constructor_with_parameters(){

        val testObject = BasicMessage(true)
        val testObject2 = BasicMessage(false)

        Assertions.assertNotNull(testObject.id)
        Assertions.assertEquals(MessageTypesHttps.BASIC_MESSAGE_TYPE, testObject.type)
        Assertions.assertEquals(MessageTypes.BASIC_MESSAGE_TYPE, testObject2.type)
    }
}