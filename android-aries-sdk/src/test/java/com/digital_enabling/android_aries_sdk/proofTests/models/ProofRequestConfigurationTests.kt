package com.digital_enabling.android_aries_sdk.proofTests.models

import com.digital_enabling.android_aries_sdk.proof.models.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProofRequestConfigurationTests {
    @Test
    @DisplayName("Serialized ProofRequestConfiguration has all keys if the values are not null.")
    fun serialization_works() {
        //Arrange
        val testObject = ProofRequestConfiguration()

        //region add nonRevoked
        val testRevocationInterval = RevocationInterval(5u, 8u)

        testObject.nonRevoked = testRevocationInterval
        //endregion

        //region add requestedAttributes
        val testRequestedAttributes = HashMap<String, ProofAttributeInfo>()
        val testProofAttributeInfo = ProofAttributeInfo()
        testProofAttributeInfo.name = "testAttributeName"
        testProofAttributeInfo.restrictions = mutableListOf()

        val testFilter = AttributeFilter()
        testFilter.schemaId = "testAttributeSchemaId"
        testFilter.schemaIssuerDid = "testAttributeSchemaIssuerId"
        testFilter.schemaName = "testAttributeSchemaName"
        testFilter.schemaVersion = "testAttributeSchemaVersion"
        testFilter.credentialDefintionId = "testAttributeCredentialDefinitionId"
        testFilter.issuerDid = "testAttributeIssuerId"

        val testAttributeValue = AttributeValue("testAttributeName")
        testAttributeValue.value = "testAttributeValue"

        testFilter.attributeValue = testAttributeValue

        testProofAttributeInfo.restrictions!!.plus(testFilter)

        testRequestedAttributes["testAttributeKey"] = testProofAttributeInfo

        testObject.requestedAttributes = testRequestedAttributes
        //endregion

        //region add requestedPredicates
        val testRequestedPredicates = HashMap<String, ProofPredicateInfo>()
        val testProofPredicateInfo = ProofPredicateInfo()
        testProofPredicateInfo.name = "testName"
        testProofPredicateInfo.restrictions = mutableListOf()

        val testFilter2 = AttributeFilter()
        testFilter2.schemaId = "testPredicateSchemaId"
        testFilter2.schemaIssuerDid = "testPredicateSchemaIssuerId"
        testFilter2.schemaName = "testPredicateSchemaName"
        testFilter2.schemaVersion = "testPredicateSchemaVersion"
        testFilter2.credentialDefintionId = "testPredicateCredentialDefinitionId"
        testFilter2.issuerDid = "testPredicateIssuerId"

        val testAttributeValue2 = AttributeValue("testPredicateName")
        testAttributeValue2.value = "testPredicateValue"

        testFilter2.attributeValue = testAttributeValue

        testProofPredicateInfo.restrictions!!.plus(testFilter)

        val testRevocationInterval2 = RevocationInterval(15u, 18u)

        testProofPredicateInfo.nonRevoked = testRevocationInterval

        testProofPredicateInfo.predicateType = "testPredicateType"
        testProofPredicateInfo.predicateValue = 25

        testRequestedPredicates["testPredicateKey"] = testProofPredicateInfo

        testObject.requestedPredicates = testRequestedPredicates
        //endregion

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<ProofRequestConfiguration>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.nonRevoked!!.from, actual.nonRevoked!!.from)
        Assertions.assertEquals(testObject.nonRevoked!!.to, actual.nonRevoked!!.to)
        Assertions.assertEquals(testObject.requestedAttributes!!["testAttributeKey"]?.name, actual.requestedAttributes!!["testAttributeKey"]?.name)
        Assertions.assertEquals(testObject.requestedPredicates!!["testAttributeKey"]?.name, actual.requestedPredicates!!["testAttributeKey"]?.name)
    }

    @Test
    @DisplayName("Serialized ProofRequestConfiguration works even if some properties do not exist.")
    fun serialization_missingProperties() {
        //Arrange
        val testObject = ProofRequestConfiguration()

        //region add nonRevoked
        val testRevocationInterval = RevocationInterval(5u, 8u)

        testObject.nonRevoked = testRevocationInterval
        //endregion

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<ProofRequestConfiguration>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.nonRevoked?.from, actual.nonRevoked?.from)
        Assertions.assertEquals(testObject.nonRevoked?.to, actual.nonRevoked?.to)
        Assertions.assertEquals(testObject.requestedAttributes, actual.requestedAttributes)
        Assertions.assertEquals(testObject.requestedPredicates!!["testAttributeKey"]?.name, actual.requestedPredicates!!["testAttributeKey"]?.name)
    }

    @Test
    @DisplayName("Serialized ProofRequestConfiguration works even if no properties exist.")
    fun deserialization_works() {
        //Arrange
        val testObject = ProofRequestConfiguration()

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<ProofRequestConfiguration>(serializedObject)

        //Assert
        Assertions.assertSame(testObject.nonRevoked, actual.nonRevoked)
        Assertions.assertEquals(testObject.requestedAttributes, actual.requestedAttributes)
        Assertions.assertEquals(testObject.requestedPredicates, actual.requestedPredicates)
    }

    @Test
    @DisplayName("toString works correctly if all properties are given")
    fun toString_works() {
        //Arrange
        val testObject = ProofRequestConfiguration()

        //region add nonRevoked
        val testRevocationInterval = RevocationInterval(5u, 8u)

        testObject.nonRevoked = testRevocationInterval
        //endregion

        //region add requestedAttributes
        val testRequestedAttributes = HashMap<String, ProofAttributeInfo>()
        val testProofAttributeInfo = ProofAttributeInfo()
        testProofAttributeInfo.name = "testAttributeName"
        testProofAttributeInfo.restrictions = mutableListOf()

        val testFilter = AttributeFilter()
        testFilter.schemaId = "testAttributeSchemaId"
        testFilter.schemaIssuerDid = "testAttributeSchemaIssuerId"
        testFilter.schemaName = "testAttributeSchemaName"
        testFilter.schemaVersion = "testAttributeSchemaVersion"
        testFilter.credentialDefintionId = "testAttributeCredentialDefinitionId"
        testFilter.issuerDid = "testAttributeIssuerId"

        val testAttributeValue = AttributeValue("testAttributeName")
        testAttributeValue.value = "testAttributeValue"

        testFilter.attributeValue = testAttributeValue

        testProofAttributeInfo.restrictions!!.plus(testFilter)

        testRequestedAttributes["testAttributeKey"] = testProofAttributeInfo

        testObject.requestedAttributes = testRequestedAttributes
        //endregion

        //region add requestedPredicates
        val testRequestedPredicates = HashMap<String, ProofPredicateInfo>()
        val testProofPredicateInfo = ProofPredicateInfo()
        testProofPredicateInfo.name = "testName"
        testProofPredicateInfo.restrictions = mutableListOf()

        val testFilter2 = AttributeFilter()
        testFilter2.schemaId = "testPredicateSchemaId"
        testFilter2.schemaIssuerDid = "testPredicateSchemaIssuerId"
        testFilter2.schemaName = "testPredicateSchemaName"
        testFilter2.schemaVersion = "testPredicateSchemaVersion"
        testFilter2.credentialDefintionId = "testPredicateCredentialDefinitionId"
        testFilter2.issuerDid = "testPredicateIssuerId"

        val testAttributeValue2 = AttributeValue("testPredicateName")
        testAttributeValue2.value = "testPredicateValue"

        testFilter2.attributeValue = testAttributeValue

        testProofPredicateInfo.restrictions!!.plus(testFilter)

        val testRevocationInterval2 = RevocationInterval(15u, 18u)

        testProofPredicateInfo.nonRevoked = testRevocationInterval

        testProofPredicateInfo.predicateType = "testPredicateType"
        testProofPredicateInfo.predicateValue = 25

        testRequestedPredicates["testPredicateKey"] = testProofPredicateInfo

        testObject.requestedPredicates = testRequestedPredicates
        //endregion

        //Act
        val actual = testObject.toString()
        val expected = "ProofRequestConfiguration: " +
                "RequestedAttributes=[testAttributeKey, ${testProofAttributeInfo}], " +
                "RequestedPredicates=[testPredicateKey, ${testProofPredicateInfo}], " +
                "NonRevoked=${testObject.nonRevoked}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("toString works correctly even if no properties exist.")
    fun toString_noProperties() {
        //Arrange
        val testObject = ProofRequestConfiguration()

        //Act
        val actual = testObject.toString()
        val expected = "ProofRequestConfiguration: " +
                "RequestedAttributes=, " +
                "RequestedPredicates=, " +
                "NonRevoked=null"

        //Assert
        Assertions.assertEquals(expected, actual)
    }
}