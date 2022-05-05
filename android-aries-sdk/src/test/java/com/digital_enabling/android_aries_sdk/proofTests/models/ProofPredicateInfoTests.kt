package com.digital_enabling.android_aries_sdk.proofTests.models

import com.digital_enabling.android_aries_sdk.proof.models.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProofPredicateInfoTests {
    @Test
    @DisplayName("Serialized ProofPredicateInfo has all keys if the values are not null.")
    fun serialization_works() {
        //Arrange
        val testObject = ProofPredicateInfo()
        testObject.name = "testName"
        testObject.restrictions = mutableListOf()

        val testFilter = AttributeFilter()
        testFilter.schemaId = "testSchemaId"
        testFilter.schemaIssuerDid = "testSchemaIssuerId"
        testFilter.schemaName = "testSchemaName"
        testFilter.schemaVersion = "testSchemaVersion"
        testFilter.credentialDefintionId = "testCredentialDefinitionId"
        testFilter.issuerDid = "testIssuerId"

        val testAttributeValue = AttributeValue("testAttributeName")
        testAttributeValue.value = "testAttributeValue"

        testFilter.attributeValue = testAttributeValue

        testObject.restrictions!!.plus(testFilter)

        val testRevocationInterval = RevocationInterval(5u, 8u)

        testObject.nonRevoked = testRevocationInterval

        testObject.predicateType = "testPredicateType"
        testObject.predicateValue = 5

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<ProofPredicateInfo>(serializedObject)


        //Assert
        Assertions.assertEquals(testObject.name, actual.name)
        Assertions.assertEquals(testObject.names, actual.names)
        Assertions.assertEquals(testObject.predicateType, actual.predicateType)
        Assertions.assertEquals(testObject.predicateValue, actual.predicateValue)
        Assertions.assertArrayEquals(arrayOf(testObject.restrictions), arrayOf(actual.restrictions))
        Assertions.assertEquals(testObject.nonRevoked!!.from, actual.nonRevoked!!.from)
        Assertions.assertEquals(testObject.nonRevoked!!.to, actual.nonRevoked!!.to)
    }

    @Test
    @DisplayName("toString works correctly.")
    fun toString_works() {
        //Arrange
        val testObject = ProofPredicateInfo()
        testObject.name = "testName"
        testObject.restrictions = mutableListOf()

        val testFilter = AttributeFilter()
        testFilter.schemaId = "testSchemaId"
        testFilter.schemaIssuerDid = "testSchemaIssuerId"
        testFilter.schemaName = "testSchemaName"
        testFilter.schemaVersion = "testSchemaVersion"
        testFilter.credentialDefintionId = "testCredentialDefinitionId"
        testFilter.issuerDid = "testIssuerId"

        val testAttributeValue = AttributeValue("testAttributeName")
        testAttributeValue.value = "testAttributeValue"

        testFilter.attributeValue = testAttributeValue

        testObject.restrictions!!.plus(testFilter)

        val testRevocationInterval = RevocationInterval(5u, 8u)

        testObject.nonRevoked = testRevocationInterval

        testObject.predicateType = "testPredicateType"
        testObject.predicateValue = 5

        //Act
        val actual = testObject.toString()
        val expected = "ProofPredicateInfo: PredicateType=testPredicateType, PredicateValue=5"

        //Assert
        Assertions.assertEquals(expected, actual)
    }
}