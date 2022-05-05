package com.digital_enabling.android_aries_sdk.proofTests.messages

import com.digital_enabling.android_aries_sdk.proof.messages.ProposedAttribute
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProposedAttributeTests {
    @Test
    @DisplayName("Serialized ProposedAttribute has all keys if the values are not null.")
    fun serialization_works() {
        //Arrange
        val testObject = ProposedAttribute()
        testObject.schemaId = "testSchemaId"
        testObject.mimeType = "testMimeType"
        testObject.name = "testName"
        testObject.value = "testValue"
        testObject.credentialDefintionId = "testCredentialDefinitionId"
        testObject.issuerDid = "testIssuerId"
        testObject.referent = "testReferent"

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<ProposedAttribute>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.schemaId, actual.schemaId)
        Assertions.assertEquals(testObject.mimeType, actual.mimeType)
        Assertions.assertEquals(testObject.name, actual.name)
        Assertions.assertEquals(testObject.value, actual.value)
        Assertions.assertEquals(testObject.issuerDid, actual.issuerDid)
        Assertions.assertEquals(testObject.credentialDefintionId, actual.credentialDefintionId)
        Assertions.assertEquals(testObject.referent, actual.referent)
    }

    @Test
    @DisplayName("Serialized ProposedPredicate works even if some properties do not exist.")
    fun serialization_missingProperties() {
        //Arrange
        val testObject = ProposedAttribute()
        testObject.mimeType = "testMimeType"
        testObject.name = "testName"
        testObject.value = "testValue"
        testObject.credentialDefintionId = "testCredentialDefinitionId"

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<ProposedAttribute>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.schemaId, actual.schemaId)
        Assertions.assertEquals(testObject.mimeType, actual.mimeType)
        Assertions.assertEquals(testObject.name, actual.name)
        Assertions.assertEquals(testObject.value, actual.value)
        Assertions.assertEquals(testObject.issuerDid, actual.issuerDid)
        Assertions.assertEquals(testObject.credentialDefintionId, actual.credentialDefintionId)
        Assertions.assertEquals(testObject.referent, actual.referent)
    }
}