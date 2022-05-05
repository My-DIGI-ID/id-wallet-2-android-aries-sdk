package com.digital_enabling.android_aries_sdk.proofTests.models

import com.digital_enabling.android_aries_sdk.proof.models.AttributeFilter
import com.digital_enabling.android_aries_sdk.proof.models.AttributeValue
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AttributeFilterTests {
    @Test
    @DisplayName("Serialized AttributeFilter has all keys if the values are not null.")
    fun serialization_works() {
        //Arrange
        val testObject = AttributeFilter()
        testObject.schemaId = "testSchemaId"
        testObject.schemaIssuerDid = "testSchemaIssuerId"
        testObject.schemaName = "testSchemaName"
        testObject.schemaVersion = "testSchemaVersion"
        testObject.credentialDefintionId = "testCredentialDefinitionId"
        testObject.issuerDid = "testIssuerId"

        val testAttributeValue = AttributeValue("testAttributeName")
        testAttributeValue.value = "testAttributeValue"

        testObject.attributeValue = testAttributeValue

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<AttributeFilter>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.schemaId, actual.schemaId)
        Assertions.assertEquals(testObject.schemaIssuerDid, actual.schemaIssuerDid)
        Assertions.assertEquals(testObject.schemaName, actual.schemaName)
        Assertions.assertEquals(testObject.schemaVersion, actual.schemaVersion)
        Assertions.assertEquals(testObject.issuerDid, actual.issuerDid)
        Assertions.assertEquals(testObject.credentialDefintionId, actual.credentialDefintionId)
        Assertions.assertEquals(testObject.attributeValue!!.name, actual.attributeValue!!.name)
        Assertions.assertEquals(testObject.attributeValue!!.value, actual.attributeValue!!.value)
    }

    @Test
    @DisplayName("Serialized AttributeFilter has always the key attributeValue.")
    fun serialization_requiredWorks() {
        //Arrange
        val testObject = AttributeFilter()

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("attributeValue"))
    }

    @Test
    @DisplayName("Serialized AttributeFilter works even if some properties do not exist.")
    fun serialization_missingProperties() {
        //Arrange
        val testObject = AttributeFilter()
        testObject.schemaId = "testSchemaId"
        testObject.schemaIssuerDid = "testSchemaIssuerId"
        testObject.schemaName = "testSchemaName"

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<AttributeFilter>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.schemaId, actual.schemaId)
        Assertions.assertEquals(testObject.schemaIssuerDid, actual.schemaIssuerDid)
        Assertions.assertEquals(testObject.schemaName, actual.schemaName)
        Assertions.assertEquals(testObject.schemaVersion, actual.schemaVersion)
        Assertions.assertEquals(testObject.issuerDid, actual.issuerDid)
        Assertions.assertEquals(testObject.credentialDefintionId, actual.credentialDefintionId)
        Assertions.assertEquals(testObject.attributeValue, actual.attributeValue)
    }
}