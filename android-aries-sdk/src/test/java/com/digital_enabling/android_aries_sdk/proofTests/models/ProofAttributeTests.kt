package com.digital_enabling.android_aries_sdk.proofTests.models

import com.digital_enabling.android_aries_sdk.proof.models.ProofAttribute
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProofAttributeTests {
    @Test
    @DisplayName("Serialized ProofIdentifier has all keys")
    fun serialization_works(): Unit = runBlocking {
        //Arrange
        val testObject = ProofAttribute(0, "testRaw", "testEncoded")

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject
        val actual = Json.decodeFromJsonElement<ProofAttribute>(serializedObject)

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("sub_proof_index"))
        Assertions.assertEquals(0, actual.subProofIndex)
        Assertions.assertTrue(serializedObject.containsKey("raw"))
        Assertions.assertEquals("testRaw", actual.raw)
        Assertions.assertTrue(serializedObject.containsKey("encoded"))
        Assertions.assertEquals("testEncoded", actual.encoded)
    }
}