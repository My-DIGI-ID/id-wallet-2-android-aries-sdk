package com.digital_enabling.android_aries_sdk.routingTests

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.routing.ForwardMessage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ForwardMessageTests {
    @Test
    @DisplayName("Serialization of ForwardMessage works.")
    fun serialization_works(){
        //Arrange
        val testObject = ForwardMessage()
        testObject.to = "testTo"
        testObject.message = JsonObject(HashMap())

        testObject.id = "testId"
        testObject.type = "testType"

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("to"))
        Assertions.assertTrue(serializedObject.containsKey("msg"))
    }

    @Test
    @DisplayName("ForwardMessage constructor without parameters sets fields correctly.")
    fun constructor_without_parameters(){

        val testObject = ForwardMessage()

        Assertions.assertNotNull(testObject.id)
        Assertions.assertEquals(MessageTypes.FORWARD, testObject.type)
    }

    @Test
    @DisplayName("ForwardMessage constructor with parameters sets fields correctly.")
    fun constructor_with_parameters(){

        val testObject = ForwardMessage(true)
        val testObject2 = ForwardMessage(false)

        Assertions.assertNotNull(testObject.id)
        Assertions.assertEquals(MessageTypesHttps.FORWARD, testObject.type)
        Assertions.assertEquals(MessageTypes.FORWARD, testObject2.type)
    }
}