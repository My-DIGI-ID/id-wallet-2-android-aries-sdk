package com.digital_enabling.android_aries_sdk.ledgerTests.models

import com.digital_enabling.android_aries_sdk.ledger.models.IndyAml
import com.digital_enabling.android_aries_sdk.ledger.models.IndyTaa
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class IndyAmlTests {
    @Test
    @DisplayName("Serialized IndyAml has keys amlContext, aml, version.")
    fun serialization_works() : Unit = runBlocking {
        //Arrange
        val testObject = IndyAml("testAmlContext", "testVersion", HashMap())
        (testObject.aml as HashMap<String, String>)["testKey"] = "testValue"

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("amlContext"))
        Assertions.assertTrue(serializedObject.containsKey("version"))
        Assertions.assertTrue(serializedObject.containsKey("aml"))
    }
}