package com.digital_enabling.android_aries_sdk.credentialTests.models

import com.digital_enabling.android_aries_sdk.credential.models.CredentialInfo
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CredentialInfoTests {
    @Test
    @DisplayName("Serialized CredentialInfo has all keys.")
    fun serialization_works() {
        //Arrange
        val testObject = CredentialInfo(
            "testReferent",
            HashMap<String, String>(),
            "testSchemaId",
            "testCredentialDefinitionId",
            "testRevocationRegistryId",
            "testCredentialRevocationId"
        )

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("referent"))
        Assertions.assertTrue(serializedObject.containsKey("attrs"))
        Assertions.assertTrue(serializedObject.containsKey("schema_id"))
        Assertions.assertTrue(serializedObject.containsKey("cred_def_id"))
        Assertions.assertTrue(serializedObject.containsKey("rev_reg_id"))
        Assertions.assertTrue(serializedObject.containsKey("cred_rev_id"))
    }
}