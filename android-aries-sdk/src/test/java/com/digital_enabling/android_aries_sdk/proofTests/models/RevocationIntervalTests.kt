package com.digital_enabling.android_aries_sdk.proofTests.models

import com.digital_enabling.android_aries_sdk.credential.models.Credential
import com.digital_enabling.android_aries_sdk.credential.models.CredentialInfo
import com.digital_enabling.android_aries_sdk.proof.models.RevocationInterval
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RevocationIntervalTests {
    @Test
    @DisplayName("Serialized Credential has all keys.")
    fun serialization_works() {
        //Arrange
        val testObject = RevocationInterval(1u, 1u)

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("from"))
        Assertions.assertTrue(serializedObject.containsKey("to"))
    }
}