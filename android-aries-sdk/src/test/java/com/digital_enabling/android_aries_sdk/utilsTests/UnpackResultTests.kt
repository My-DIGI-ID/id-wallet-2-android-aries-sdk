package com.digital_enabling.android_aries_sdk.utilsTests

import com.digital_enabling.android_aries_sdk.utils.DecodedMessageType
import com.digital_enabling.android_aries_sdk.utils.UnpackResult
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class UnpackResultTests {

    @Test
    @DisplayName("Serialized UnpackResult has keys message, sender_verkey, recipient_verkey.")
    fun serialization_works() : Unit = runBlocking {
        //Arrange
        val testMessage = "testMessage"
        val testSenderVerkey = "testSenderVerkey"
        val testRecipientVerkey = "testRecipientVerkey"
        val testObject = UnpackResult(testMessage, testSenderVerkey, testRecipientVerkey)

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("message"))
        Assertions.assertTrue(serializedObject.containsKey("sender_verkey"))
        Assertions.assertTrue(serializedObject.containsKey("recipient_verkey"))
    }
}