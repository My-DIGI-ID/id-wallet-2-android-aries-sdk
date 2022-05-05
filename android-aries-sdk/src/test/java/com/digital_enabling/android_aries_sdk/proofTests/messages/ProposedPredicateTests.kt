package com.digital_enabling.android_aries_sdk.proofTests.messages

import com.digital_enabling.android_aries_sdk.proof.messages.ProposedPredicate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProposedPredicateTests {
    @Test
    @DisplayName("Serialized ProposedPredicate has all keys if the values are not null.")
    fun serialization_works() {
        //Arrange
        val testObject = ProposedPredicate()
        testObject.schemaId = "testSchemaId"
        testObject.threshold = "testThreshold"
        testObject.name = "testName"
        testObject.predicate = "testPredicate"
        testObject.credentialDefintionId = "testCredentialDefinitionId"
        testObject.issuerDid = "testIssuerId"
        testObject.referent = "testReferent"

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<ProposedPredicate>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.schemaId, actual.schemaId)
        Assertions.assertEquals(testObject.threshold, actual.threshold)
        Assertions.assertEquals(testObject.name, actual.name)
        Assertions.assertEquals(testObject.predicate, actual.predicate)
        Assertions.assertEquals(testObject.issuerDid, actual.issuerDid)
        Assertions.assertEquals(testObject.credentialDefintionId, actual.credentialDefintionId)
        Assertions.assertEquals(testObject.referent, actual.referent)
    }

    @Test
    @DisplayName("Serialized ProposedPredicate works even if some properties do not exist.")
    fun serialization_missingProperties() {
        //Arrange
        val testObject = ProposedPredicate()
        testObject.schemaId = "testSchemaId"
        testObject.threshold = "testThreshold"
        testObject.predicate = "testPredicate"
        testObject.issuerDid = "testIssuerId"

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<ProposedPredicate>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.schemaId, actual.schemaId)
        Assertions.assertEquals(testObject.threshold, actual.threshold)
        Assertions.assertEquals(testObject.name, actual.name)
        Assertions.assertEquals(testObject.predicate, actual.predicate)
        Assertions.assertEquals(testObject.issuerDid, actual.issuerDid)
        Assertions.assertEquals(testObject.credentialDefintionId, actual.credentialDefintionId)
        Assertions.assertEquals(testObject.referent, actual.referent)
    }
}