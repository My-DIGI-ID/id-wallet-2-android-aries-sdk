package com.digital_enabling.android_aries_sdk.trustpingTests

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.routing.ForwardMessage
import com.digital_enabling.android_aries_sdk.trustping.TrustPingMessage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TrustPingMessageTests {
    @Test
    @DisplayName("Serialization of TrustPingMessage works.")
    fun serialization_works(){
        //Arrange
        val testObject = TrustPingMessage()
        testObject.comment = "testComment"
        testObject.responseRequested = false

        testObject.id = "testId"
        testObject.type = "testType"

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("comment"))
        Assertions.assertTrue(serializedObject.containsKey("response_requested"))
    }

    @Test
    @DisplayName("TrustPingMessage constructor without parameters sets fields correctly.")
    fun constructor_without_parameters(){

        val testObject = TrustPingMessage()

        Assertions.assertNotNull(testObject.id)
        Assertions.assertEquals(MessageTypes.TRUST_PING_MESSAGE_TYPE, testObject.type)
    }

    @Test
    @DisplayName("TrustPingMessage constructor with parameters sets fields correctly.")
    fun constructor_with_parameters(){

        val testObject = TrustPingMessage(true)
        val testObject2 = TrustPingMessage(false)

        Assertions.assertNotNull(testObject.id)
        Assertions.assertEquals(MessageTypesHttps.TRUST_PING_MESSAGE_TYPE, testObject.type)
        Assertions.assertEquals(MessageTypes.TRUST_PING_MESSAGE_TYPE, testObject2.type)
    }

}