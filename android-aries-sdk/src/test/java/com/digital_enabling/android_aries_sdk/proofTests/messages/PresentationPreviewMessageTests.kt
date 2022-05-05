package com.digital_enabling.android_aries_sdk.proofTests.messages

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.proof.messages.PresentationPreviewMessage
import com.digital_enabling.android_aries_sdk.proof.messages.ProposedAttribute
import com.digital_enabling.android_aries_sdk.proof.messages.ProposedPredicate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PresentationPreviewMessageTests {
    @Test
    @DisplayName("Serialized PresentationPreviewMessage has all keys if the values are not null.")
    fun serialization_works() {
        //Arrange
        val testObject = PresentationPreviewMessage()

        //region add proposed attributes
        val testProposedAttribute = ProposedAttribute()
        testProposedAttribute.schemaId = "testAttributeSchemaId"
        testProposedAttribute.mimeType = "testAttributeMimeType"
        testProposedAttribute.name = "testAttributeName"
        testProposedAttribute.value = "testAttributeValue"
        testProposedAttribute.credentialDefintionId = "testAttributeCredentialDefinitionId"
        testProposedAttribute.issuerDid = "testAttributeIssuerId"
        testProposedAttribute.referent = "testAttributeReferent"

        testObject.proposedAttributes = arrayOf(testProposedAttribute)
        //endregion

        //region add proposed predicates
        val testProposedPredicate = ProposedPredicate()
        testProposedPredicate.schemaId = "testPredicateSchemaId"
        testProposedPredicate.threshold = "testPredicateThreshold"
        testProposedPredicate.name = "testPredicateName"
        testProposedPredicate.predicate = "testPredicatePredicate"
        testProposedPredicate.credentialDefintionId = "testPredicateCredentialDefinitionId"
        testProposedPredicate.issuerDid = "testPredicateIssuerId"
        testProposedPredicate.referent = "testPredicateReferent"

        testObject.proposedPredicates = arrayOf(testProposedPredicate)
        //endregion

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<PresentationPreviewMessage>(serializedObject)

        //Assert
        Assertions.assertEquals("testAttributeName", actual.proposedAttributes?.first()?.name)
        Assertions.assertEquals(
            "testPredicateCredentialDefinitionId",
            actual.proposedPredicates?.first()?.credentialDefintionId
        )
        Assertions.assertEquals(testObject.id, actual.id)
        Assertions.assertEquals(testObject.type, actual.type)
    }

    @Test
    @DisplayName("PresentationPreviewMessage has an id when using the primary constructor")
    fun primary_const_has_id() {
        //Arrange
        val testObject = PresentationPreviewMessage()

        //Act
        val actual = testObject.id

        //Assert
        Assertions.assertFalse(actual.isNullOrEmpty())
    }

    @Test
    @DisplayName("PresentationPreviewMessage has a type when using the primary constructor")
    fun primary_const_has_type() {
        //Arrange
        val testObject = PresentationPreviewMessage()

        //Act
        val actual = testObject.type
        val expected = MessageTypes.PresentProofNames.PRESENTATION_PREVIEW

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("PresentationPreviewMessage has an id when using the secondary constructor")
    fun secondary_const_has_id() {
        //Arrange
        val testObject = PresentationPreviewMessage(false)

        //Act
        val actual = testObject.id

        //Assert
        Assertions.assertFalse(actual.isNullOrEmpty())
    }

    @Test
    @DisplayName("PresentationPreviewMessage has a type when using the secondary constructor")
    fun secondary_const_has_type() {
        //Arrange
        val testObject = PresentationPreviewMessage(false)

        //Act
        val actual = testObject.type
        val expected = MessageTypes.PresentProofNames.PRESENTATION_PREVIEW

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("PresentationPreviewMessage uses MessageTypesHttps when using the secondary constructor with true")
    fun secondary_const_true() {
        //Arrange
        val testObject = PresentationPreviewMessage(true)

        //Act
        val actual = testObject.type
        val expected = MessageTypesHttps.PresentProofNames.PRESENTATION_PREVIEW

        //Assert
        Assertions.assertEquals(expected, actual)
    }
}