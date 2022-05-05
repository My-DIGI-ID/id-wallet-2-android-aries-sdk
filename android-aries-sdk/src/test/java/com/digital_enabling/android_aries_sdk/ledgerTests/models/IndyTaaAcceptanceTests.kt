package com.digital_enabling.android_aries_sdk.ledgerTests.models

import com.digital_enabling.android_aries_sdk.ledger.models.IndyTaa
import com.digital_enabling.android_aries_sdk.ledger.models.IndyTaaAcceptance
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class IndyTaaAcceptanceTests {

    @Test
    @DisplayName("Serialized IndyTaaAcceptance has keys digest, acceptanceMechanism ")
    fun serialization_works() : Unit = runBlocking {
        //Arrange
        val testObject = IndyTaaAcceptance("testDigest", "testAcceptanceMechanism")

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("digest"))
        Assertions.assertTrue(serializedObject.containsKey("acceptanceMechanism"))
    }
}