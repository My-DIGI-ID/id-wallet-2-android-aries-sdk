package com.digital_enabling.android_aries_sdk.proofTests.models

import com.digital_enabling.android_aries_sdk.proof.models.PartialProof
import com.digital_enabling.android_aries_sdk.proof.models.ProofAttribute
import com.digital_enabling.android_aries_sdk.proof.models.ProofIdentifier
import com.digital_enabling.android_aries_sdk.proof.models.RequestedProof
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PartialProofTests {
    @Test
    @DisplayName("PartialProof constructor works.")
    fun constructor_works(): Unit = runBlocking {
        //Arrange
        val testRevealeadAttributes = HashMap<String, ProofAttribute>()
        val testSelfAttested = HashMap<String, String>()

        testRevealeadAttributes["testId1"] = ProofAttribute(1, "testRaw", "testEncoded")
        testSelfAttested["testId2"] = "testValue"
        val testRequestedProof = RequestedProof(testRevealeadAttributes, testSelfAttested)

        val testProofIdentifiers = mutableListOf(
            ProofIdentifier(
                "testSchemaId",
                "testCredDefId",
                "testRevRegId",
                "testTimeStamp"
            )
        )

        val testObject = PartialProof(testProofIdentifiers, testRequestedProof)

        //Act

        //Assert
        Assertions.assertEquals(testRevealeadAttributes["testId1"]?.subProofIndex, testObject.requestedProof.revealedAttributes?.get("testId1")?.subProofIndex)
        Assertions.assertEquals(testRevealeadAttributes["testId1"]?.raw, testObject.requestedProof.revealedAttributes?.get("testId1")?.raw)
        Assertions.assertEquals(testRevealeadAttributes["testId1"]?.encoded, testObject.requestedProof.revealedAttributes?.get("testId1")?.encoded)
        Assertions.assertEquals(testSelfAttested["testId2"],
            testObject.requestedProof.selfAttestedAttributes?.get("testId2")
        )
        Assertions.assertArrayEquals(arrayOf(testProofIdentifiers), arrayOf(testObject.identifiers))
    }

    @Test
    @DisplayName("Serialized PartialProof has keys identifiers, requested_proof.")
    fun serialization_works(): Unit = runBlocking {
        //Arrange
        val testRevealeadAttributes = HashMap<String, ProofAttribute>()
        val testSelfAttested = HashMap<String, String>()

        testRevealeadAttributes["testId1"] = ProofAttribute(1, "testRaw", "testEncoded")
        testSelfAttested["testId2"] = "testValue"
        val testRequestedProof = RequestedProof(testRevealeadAttributes, testSelfAttested)

        val testProofIdentifiers = mutableListOf(
            ProofIdentifier(
                "testSchemaId",
                "testCredDefId",
                "testRevRegId",
                "testTimeStamp"
            )
        )

        val testObject = PartialProof(testProofIdentifiers, testRequestedProof)

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("identifiers"))
        Assertions.assertTrue(serializedObject.containsKey("requested_proof"))
    }
}