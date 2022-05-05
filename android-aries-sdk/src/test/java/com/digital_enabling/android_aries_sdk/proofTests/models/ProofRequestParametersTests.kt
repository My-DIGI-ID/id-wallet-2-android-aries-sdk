package com.digital_enabling.android_aries_sdk.proofTests.models

import com.digital_enabling.android_aries_sdk.proof.models.ProofRequestParameters
import com.digital_enabling.android_aries_sdk.proof.models.RevocationInterval
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProofRequestParametersTests {
    @Test
    @DisplayName("Serialized ProofRequestParameters has all keys if the values are not null.")
    fun serialization_works() {
        //Arrange
        val testObject = ProofRequestParameters()
        testObject.name = "testName"
        testObject.version = "testVersion"

        //region add nonRevoked
        val testRevocationInterval = RevocationInterval(5u, 8u)

        testObject.nonRevoked = testRevocationInterval
        //endregion

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<ProofRequestParameters>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.name, actual.name)
        Assertions.assertEquals(testObject.version, actual.version)
        Assertions.assertEquals(testObject.nonRevoked!!.from, actual.nonRevoked!!.from)
        Assertions.assertEquals(testObject.nonRevoked!!.to, actual.nonRevoked!!.to)
    }

    @Test
    @DisplayName("Serialized ProofRequestParameters works even if some properties do not exist.")
    fun serialization_missingProperties() {
        //Arrange
        val testObject = ProofRequestParameters()
        testObject.name = "testName"

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<ProofRequestParameters>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.name, actual.name)
        Assertions.assertEquals(testObject.version, actual.version)
        Assertions.assertEquals(testObject.nonRevoked, actual.nonRevoked)
    }

    @Test
    @DisplayName("Serialized ProofRequestParameters works even if no properties exist.")
    fun serialization_noProperties() {
        //Arrange
        val testObject = ProofRequestParameters()

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<ProofRequestParameters>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.name, actual.name)
        Assertions.assertEquals(testObject.version, actual.version)
        Assertions.assertEquals(testObject.nonRevoked, actual.nonRevoked)
    }
}