package com.digital_enabling.android_aries_sdk.proofTests.models

import com.digital_enabling.android_aries_sdk.proof.models.RequestedAttribute
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RequestedAttributeTests {
    @Test
    @DisplayName("Serialized RequestedAttribute has all keys if the values are not null.")
    fun serialization_works() {
        //Arrange
        val testObject = RequestedAttribute()
        testObject.credentialId = "testCredentialId"
        testObject.timestamp = 15
        testObject.revealed = true

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<RequestedAttribute>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.credentialId, actual.credentialId)
        Assertions.assertEquals(testObject.timestamp, actual.timestamp)
        Assertions.assertEquals(testObject.revealed, actual.revealed)
    }

    @Test
    @DisplayName("Serialized RequestedAttribute works even if some properties do not exist.")
    fun serialization_missingProperties() {
        //Arrange
        val testObject = RequestedAttribute()
        testObject.credentialId = "testCredentialId"
        testObject.revealed = false

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<RequestedAttribute>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.credentialId, actual.credentialId)
        Assertions.assertEquals(testObject.timestamp, actual.timestamp)
        Assertions.assertEquals(testObject.revealed, actual.revealed)
    }

    @Test
    @DisplayName("Serialized RequestedAttribute works even if no properties exist.")
    fun serialization_noProperties() {
        //Arrange
        val testObject = RequestedAttribute()

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<RequestedAttribute>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.credentialId, actual.credentialId)
        Assertions.assertEquals(testObject.timestamp, actual.timestamp)
        Assertions.assertEquals(testObject.revealed, actual.revealed)
    }
}