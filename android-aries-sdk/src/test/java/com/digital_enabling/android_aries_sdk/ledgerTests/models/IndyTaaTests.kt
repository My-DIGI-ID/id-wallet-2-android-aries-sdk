package com.digital_enabling.android_aries_sdk.ledgerTests.models

import com.digital_enabling.android_aries_sdk.ledger.models.IndyTaa
import com.digital_enabling.android_aries_sdk.utils.UnpackResult
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class IndyTaaTests {

    @Test
    @DisplayName("Serialized IndyTaa has keys text, version.")
    fun serialization_works() : Unit = runBlocking {
        //Arrange
        val testObject = IndyTaa("testText", "testVersion")

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("text"))
        Assertions.assertTrue(serializedObject.containsKey("version"))
    }
}