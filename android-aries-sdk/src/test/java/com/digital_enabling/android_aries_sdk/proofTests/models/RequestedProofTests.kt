package com.digital_enabling.android_aries_sdk.proofTests.models

import com.digital_enabling.android_aries_sdk.proof.models.ProofAttribute
import com.digital_enabling.android_aries_sdk.proof.models.RequestedProof
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RequestedProofTests {
    @Test
    @DisplayName("RequestedProof IndyTaa has keys revealed_attrs, self_attested_attrs.")
    fun serialization_works(): Unit = runBlocking {
        //Arrange
        val testRevealeadAttributes = HashMap<String, ProofAttribute>()
        val testSelfAttested = HashMap<String, String>()

        testRevealeadAttributes["testId1"] = ProofAttribute(1, "testRaw", "testEncoded")
        testSelfAttested["testId2"] = "testValue"
        val testObject = RequestedProof(testRevealeadAttributes, testSelfAttested)

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject
        val actual = Json.decodeFromJsonElement<RequestedProof>(serializedObject)

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("revealed_attrs"))
        Assertions.assertEquals(testRevealeadAttributes.size, actual.revealedAttributes?.size)
        Assertions.assertTrue(serializedObject.containsKey("self_attested_attrs"))
        Assertions.assertEquals(testSelfAttested.size, actual.selfAttestedAttributes?.size)
    }
}