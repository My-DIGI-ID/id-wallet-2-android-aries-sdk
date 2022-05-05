package com.digital_enabling.android_aries_sdk.credentialTests.models

import com.digital_enabling.android_aries_sdk.credential.models.Credential
import com.digital_enabling.android_aries_sdk.credential.models.CredentialInfo
import com.digital_enabling.android_aries_sdk.proof.models.RevocationInterval
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CredentialTests {
    @Test
    @DisplayName("Serialized Credential has all keys.")
    fun serialization_works() {
        //Arrange
        val testCredentialInfo = CredentialInfo(
            "testReferent",
            HashMap<String, String>(),
            "testSchemaId",
            "testCredentialDefinitionId",
            "testRevocationRegistryId",
            "testCredentialRevocationId"
        )
        val testRevocationInterval = RevocationInterval(1u, 1u)
        val testObject = Credential(testCredentialInfo, testRevocationInterval)

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("cred_info"))
        Assertions.assertEquals(testCredentialInfo, testObject.credentialInfo)
        Assertions.assertEquals(testRevocationInterval, testObject.nonRevocationInterval)
        Assertions.assertTrue(serializedObject.containsKey("interval"))
    }
}