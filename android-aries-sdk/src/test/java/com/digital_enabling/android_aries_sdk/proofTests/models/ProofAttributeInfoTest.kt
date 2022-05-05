package com.digital_enabling.android_aries_sdk.proofTests.models

import com.digital_enabling.android_aries_sdk.proof.models.AttributeFilter
import com.digital_enabling.android_aries_sdk.proof.models.AttributeValue
import com.digital_enabling.android_aries_sdk.proof.models.ProofAttributeInfo
import com.digital_enabling.android_aries_sdk.proof.models.RevocationInterval
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProofAttributeInfoTest {
    @Test
    @DisplayName("Serialized ProofAttributeInfo has all keys if the values are not null.")
    fun serialization_works() {
        //Arrange
        val testObject = ProofAttributeInfo()
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

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<ProofAttributeInfo>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.name, actual.name)
        Assertions.assertEquals(testObject.names, actual.names)
        Assertions.assertArrayEquals(arrayOf(testObject.restrictions), arrayOf(actual.restrictions))
        Assertions.assertEquals(testObject.nonRevoked!!.from, actual.nonRevoked!!.from)
        Assertions.assertEquals(testObject.nonRevoked!!.to, actual.nonRevoked!!.to)
    }

    @Test
    @DisplayName("ProofAttributeInfo can be deserialized from Json with missing properties.")
    fun deserialization_works() {
        //Arrange
        val testObject = ProofAttributeInfo()

        //Act
        val serializedObject = Json.encodeToString(testObject)

        //Assert
        Assertions.assertDoesNotThrow { Json.decodeFromString<ProofAttributeInfo>(serializedObject) }
    }

    @Test
    @DisplayName("ProofAttributeInfo names is null after name was set.")
    fun property_namesNull() {
        //Arrange
        val testObject = ProofAttributeInfo()
        val testNames = arrayOf("testName1", "testName2")
        val testName = "testName"
        testObject.names = testNames
        Assertions.assertArrayEquals(testNames, testObject.names)
        Assertions.assertTrue(testObject.name == null)

        //Act
        testObject.name = testName

        //Assert
        Assertions.assertTrue(testObject.names == null)
        Assertions.assertEquals(testName, testObject.name)
    }

    @Test
    @DisplayName("ProofAttributeInfo name is null after names was set.")
    fun property_nameNull() {
        //Arrange
        val testObject = ProofAttributeInfo()
        val testNames = arrayOf("testName1", "testName2")
        val testName = "testName"
        testObject.name = testName
        Assertions.assertTrue(testObject.names == null)
        Assertions.assertEquals(testName, testObject.name)

        //Act
        testObject.names = testNames

        //Assert
        Assertions.assertArrayEquals(testNames, testObject.names)
        Assertions.assertTrue(testObject.name == null)
    }
}