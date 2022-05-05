package com.digital_enabling.android_aries_sdk.proofTests.models

import com.digital_enabling.android_aries_sdk.proof.models.ProofIdentifier
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProofIdentifierTests {
    @Test
    @DisplayName("Serialized ProofIdentifier has all keys")
    fun serialization_works(): Unit = runBlocking {
        //Arrange
        val testObject =
            ProofIdentifier("testSchemaId", "testCredDefId", "testRevRegId", "testTimeStamp")

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject
        val actual = Json.decodeFromJsonElement<ProofIdentifier>(serializedObject)

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("schema_id"))
        Assertions.assertEquals("testSchemaId", actual.schemaId)
        Assertions.assertTrue(serializedObject.containsKey("cred_def_id"))
        Assertions.assertEquals("testCredDefId", actual.credentialDefintionId)
        Assertions.assertTrue(serializedObject.containsKey("rev_reg_id"))
        Assertions.assertEquals("testRevRegId", actual.revocationRegistryId)
        Assertions.assertTrue(serializedObject.containsKey("timestamp"))
        Assertions.assertEquals("testTimeStamp", actual.timestamp)
    }
}