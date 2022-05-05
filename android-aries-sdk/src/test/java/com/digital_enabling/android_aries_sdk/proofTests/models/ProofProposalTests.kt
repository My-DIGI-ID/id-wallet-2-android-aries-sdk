package com.digital_enabling.android_aries_sdk.proofTests.models

import com.digital_enabling.android_aries_sdk.proof.messages.ProposedAttribute
import com.digital_enabling.android_aries_sdk.proof.messages.ProposedPredicate
import com.digital_enabling.android_aries_sdk.proof.models.ProofProposal
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProofProposalTests {
    @Test
    @DisplayName("Serialized ProofProposal has all keys if the values are not null.")
    fun serialization_works() {
        //Arrange
        val testObject = ProofProposal()
        testObject.comment = "testComment"

        val testProposedPredicate = ProposedPredicate()
        testProposedPredicate.schemaId = "testSchemaId"
        testProposedPredicate.threshold = "testThreshold"
        testProposedPredicate.name = "testName"
        testProposedPredicate.predicate = "testPredicate"
        testProposedPredicate.credentialDefintionId = "testCredentialDefinitionId"
        testProposedPredicate.issuerDid = "testIssuerId"
        testProposedPredicate.referent = "testReferent"

        testObject.proposedPredicates = mutableListOf(testProposedPredicate)

        val testProposedAttribute = ProposedAttribute()
        testProposedAttribute.schemaId = "testSchemaId"
        testProposedAttribute.mimeType = "testMimeType"
        testProposedAttribute.name = "testName"
        testProposedAttribute.value = "testValue"
        testProposedAttribute.credentialDefintionId = "testCredentialDefinitionId"
        testProposedAttribute.issuerDid = "testIssuerId"
        testProposedAttribute.referent = "testReferent"

        testObject.proposedAttributes = mutableListOf(testProposedAttribute)

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<ProofProposal>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.comment, actual.comment)
        Assertions.assertEquals(
            testObject.proposedPredicates!!.first().schemaId,
            actual.proposedPredicates!!.first().schemaId
        )
        Assertions.assertEquals(
            testObject.proposedPredicates!!.first().threshold,
            actual.proposedPredicates!!.first().threshold
        )
        Assertions.assertEquals(
            testObject.proposedPredicates!!.first().name,
            actual.proposedPredicates!!.first().name
        )
        Assertions.assertEquals(
            testObject.proposedPredicates!!.first().predicate,
            actual.proposedPredicates!!.first().predicate
        )
        Assertions.assertEquals(
            testObject.proposedPredicates!!.first().issuerDid,
            actual.proposedPredicates!!.first().issuerDid
        )
        Assertions.assertEquals(
            testObject.proposedPredicates!!.first().credentialDefintionId,
            actual.proposedPredicates!!.first().credentialDefintionId
        )
        Assertions.assertEquals(
            testObject.proposedPredicates!!.first().referent,
            actual.proposedPredicates!!.first().referent
        )
        Assertions.assertEquals(
            testObject.proposedAttributes!!.first().schemaId,
            actual.proposedAttributes!!.first().schemaId
        )
        Assertions.assertEquals(
            testObject.proposedAttributes!!.first().mimeType,
            actual.proposedAttributes!!.first().mimeType
        )
        Assertions.assertEquals(
            testObject.proposedAttributes!!.first().name,
            actual.proposedAttributes!!.first().name
        )
        Assertions.assertEquals(
            testObject.proposedAttributes!!.first().value,
            actual.proposedAttributes!!.first().value
        )
        Assertions.assertEquals(
            testObject.proposedAttributes!!.first().issuerDid,
            actual.proposedAttributes!!.first().issuerDid
        )
        Assertions.assertEquals(
            testObject.proposedAttributes!!.first().credentialDefintionId,
            actual.proposedAttributes!!.first().credentialDefintionId
        )
        Assertions.assertEquals(
            testObject.proposedAttributes!!.first().referent,
            actual.proposedAttributes!!.first().referent
        )
    }

    @Test
    @DisplayName("Serialized ProofProposal works even if some properties do not exist.")
    fun serialization_missingProperties() {
        //Arrange
        val testObject = ProofProposal()

        val testProposedPredicate = ProposedPredicate()
        testProposedPredicate.schemaId = "testSchemaId"
        testProposedPredicate.threshold = "testThreshold"
        testProposedPredicate.name = "testName"
        testProposedPredicate.credentialDefintionId = "testCredentialDefinitionId"

        testObject.proposedPredicates = mutableListOf(testProposedPredicate)

        val testProposedAttribute = ProposedAttribute()
        testProposedAttribute.schemaId = "testSchemaId"
        testProposedAttribute.mimeType = "testMimeType"
        testProposedAttribute.issuerDid = "testIssuerId"
        testProposedAttribute.referent = "testReferent"

        testObject.proposedAttributes = mutableListOf(testProposedAttribute)

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<ProofProposal>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.comment, actual.comment)
        Assertions.assertEquals(
            testObject.proposedPredicates!!.first().schemaId,
            actual.proposedPredicates!!.first().schemaId
        )
        Assertions.assertEquals(
            testObject.proposedPredicates!!.first().threshold,
            actual.proposedPredicates!!.first().threshold
        )
        Assertions.assertEquals(
            testObject.proposedPredicates!!.first().name,
            actual.proposedPredicates!!.first().name
        )
        Assertions.assertEquals(
            testObject.proposedPredicates!!.first().predicate,
            actual.proposedPredicates!!.first().predicate
        )
        Assertions.assertEquals(
            testObject.proposedPredicates!!.first().issuerDid,
            actual.proposedPredicates!!.first().issuerDid
        )
        Assertions.assertEquals(
            testObject.proposedPredicates!!.first().credentialDefintionId,
            actual.proposedPredicates!!.first().credentialDefintionId
        )
        Assertions.assertEquals(
            testObject.proposedPredicates!!.first().referent,
            actual.proposedPredicates!!.first().referent
        )
        Assertions.assertEquals(
            testObject.proposedAttributes!!.first().schemaId,
            actual.proposedAttributes!!.first().schemaId
        )
        Assertions.assertEquals(
            testObject.proposedAttributes!!.first().mimeType,
            actual.proposedAttributes!!.first().mimeType
        )
        Assertions.assertEquals(
            testObject.proposedAttributes!!.first().name,
            actual.proposedAttributes!!.first().name
        )
        Assertions.assertEquals(
            testObject.proposedAttributes!!.first().value,
            actual.proposedAttributes!!.first().value
        )
        Assertions.assertEquals(
            testObject.proposedAttributes!!.first().issuerDid,
            actual.proposedAttributes!!.first().issuerDid
        )
        Assertions.assertEquals(
            testObject.proposedAttributes!!.first().credentialDefintionId,
            actual.proposedAttributes!!.first().credentialDefintionId
        )
        Assertions.assertEquals(
            testObject.proposedAttributes!!.first().referent,
            actual.proposedAttributes!!.first().referent
        )
    }

    @Test
    @DisplayName("Serialized ProofProposal works even if no properties exist.")
    fun serialization_noProperties() {
        //Arrange
        val testObject = ProofProposal()

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<ProofProposal>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.comment, actual.comment)
        Assertions.assertEquals(testObject.proposedPredicates, actual.proposedPredicates)
        Assertions.assertEquals(testObject.proposedAttributes, actual.proposedAttributes)
    }
}