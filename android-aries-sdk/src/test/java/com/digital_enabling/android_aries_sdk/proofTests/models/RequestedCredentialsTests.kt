package com.digital_enabling.android_aries_sdk.proofTests.models

import com.digital_enabling.android_aries_sdk.proof.models.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RequestedCredentialsTests {
    @Test
    @DisplayName("Serialized RequestedCredentials has all keys if the values are not null.")
    fun serialization_works() {
        //Arrange
        val testObject = RequestedCredentials()

        //region add requestedAttributes
        val testRequestedAttributes = HashMap<String, RequestedAttribute>()

        val testRequestedAttribute = RequestedAttribute()
        testRequestedAttribute.credentialId = "testCredentialId"
        testRequestedAttribute.timestamp = 15
        testRequestedAttribute.revealed = true

        testRequestedAttributes["testAttributeKey"] = testRequestedAttribute

        testObject.requestedAttributes = testRequestedAttributes
        //endregion

        //region add requestedPredicates
        val testRequestedPredicates = HashMap<String, RequestedAttribute>()

        val testRequestedPredicate = RequestedAttribute()
        testRequestedAttribute.credentialId = "testPredicateCredentialId"
        testRequestedAttribute.timestamp = 25
        testRequestedAttribute.revealed = false

        testRequestedPredicates["testPredicateKey"] = testRequestedPredicate

        testObject.requestedPredicates = testRequestedPredicates
        //endregion

        //region add selfAttestedAttributes
        val testSelfAttested = HashMap<String, String>()

        testSelfAttested["testSelfKey"] = "testSelfValue"

        testObject.selfAttestedAttributes = testSelfAttested
        //endregion

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<RequestedCredentials>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.requestedAttributes!!["testAttributeKey"]?.credentialId, actual.requestedAttributes!!["testAttributeKey"]?.credentialId)
        Assertions.assertEquals(testObject.requestedPredicates!!["testPredicateKey"]?.credentialId, actual.requestedPredicates!!["testPredicateKey"]?.credentialId)
        Assertions.assertEquals(testObject.selfAttestedAttributes!!["testSelfKey"], actual.selfAttestedAttributes!!["testSelfKey"])
    }

    @Test
    @DisplayName("Serialized RequestedCredentials works even if some properties do not exist.")
    fun serialization_missingProperties() {
        //Arrange
        val testObject = RequestedCredentials()

        //region add requestedAttributes
        val testRequestedAttributes = HashMap<String, RequestedAttribute>()

        val testRequestedAttribute = RequestedAttribute()
        testRequestedAttribute.credentialId = "testCredentialId"
        testRequestedAttribute.timestamp = 15
        testRequestedAttribute.revealed = true

        testRequestedAttributes["testAttributeKey"] = testRequestedAttribute

        testObject.requestedAttributes = testRequestedAttributes
        //endregion

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<RequestedCredentials>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.requestedAttributes!!["testAttributeKey"]?.credentialId, actual.requestedAttributes!!["testAttributeKey"]?.credentialId)
        Assertions.assertEquals(testObject.requestedPredicates, actual.requestedPredicates)
        Assertions.assertEquals(testObject.selfAttestedAttributes!!["testSelfKey"], actual.selfAttestedAttributes!!["testSelfKey"])
    }

    @Test
    @DisplayName("Serialized RequestedCredentials works even if no properties exist.")
    fun serialization_noProperties() {
        //Arrange
        val testObject = RequestedCredentials()

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<RequestedCredentials>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.requestedAttributes!!["testAttributeKey"], actual.requestedAttributes!!["testAttributeKey"])
        Assertions.assertEquals(testObject.requestedAttributes!!["testPredicateKey"], actual.requestedAttributes!!["testPredicateKey"])
        Assertions.assertEquals(testObject.selfAttestedAttributes!!["testSelfKey"], actual.selfAttestedAttributes!!["testSelfKey"])
    }

    @Test
    @DisplayName("getCredentialIdentifiers gets all credentialIds")
    fun getCredentialIdentifiers_getsAll() {
        //Arrange
        val testObject = RequestedCredentials()

        //region add requestedAttributes
        val testRequestedAttributes = HashMap<String, RequestedAttribute>()

        val testRequestedAttribute = RequestedAttribute()
        testRequestedAttribute.credentialId = "testCredentialId"
        testRequestedAttribute.timestamp = 15
        testRequestedAttribute.revealed = true

        testRequestedAttributes["testAttributeKey"] = testRequestedAttribute

        testObject.requestedAttributes = testRequestedAttributes
        //endregion

        //region add requestedPredicates
        val testRequestedPredicates = HashMap<String, RequestedAttribute>()

        val testRequestedPredicate = RequestedAttribute()
        testRequestedPredicate.credentialId = "testPredicateCredentialId"
        testRequestedPredicate.timestamp = 25
        testRequestedPredicate.revealed = false

        testRequestedPredicates["testPredicateKey"] = testRequestedPredicate

        testObject.requestedPredicates = testRequestedPredicates
        //endregion

        //Act
        val actual = testObject.getCredentialIdentifiers()

        //Assert
        Assertions.assertEquals(2, actual.size)
        Assertions.assertTrue(actual.contains("testCredentialId"))
        Assertions.assertTrue(actual.contains("testPredicateCredentialId"))
    }

    @Test
    @DisplayName("getCredentialIdentifiers only gets distinct credentialIds")
    fun getCredentialIdentifiers_distinctAll() {
        //Arrange
        val testObject = RequestedCredentials()

        //region add requestedAttributes
        val testRequestedAttributes = HashMap<String, RequestedAttribute>()

        val testRequestedAttribute = RequestedAttribute()
        testRequestedAttribute.credentialId = "testCredentialId"
        testRequestedAttribute.timestamp = 15
        testRequestedAttribute.revealed = true

        testRequestedAttributes["testAttributeKey"] = testRequestedAttribute

        val testRequestedAttribute2 = RequestedAttribute()
        testRequestedAttribute2.credentialId = "testCredentialId"
        testRequestedAttribute2.timestamp = 35
        testRequestedAttribute2.revealed = true

        testRequestedAttributes["testAttributeKey2"] = testRequestedAttribute2

        testObject.requestedAttributes = testRequestedAttributes
        //endregion

        //region add requestedPredicates
        val testRequestedPredicates = HashMap<String, RequestedAttribute>()

        val testRequestedPredicate = RequestedAttribute()
        testRequestedPredicate.credentialId = "testPredicateCredentialId"
        testRequestedPredicate.timestamp = 25
        testRequestedPredicate.revealed = false

        testRequestedPredicates["testPredicateKey"] = testRequestedPredicate

        testObject.requestedPredicates = testRequestedPredicates
        //endregion

        //Act
        val actual = testObject.getCredentialIdentifiers()

        //Assert
        Assertions.assertEquals(2, actual.size)
        Assertions.assertEquals(3, (testObject.requestedAttributes as HashMap<String, RequestedAttribute>).size + (testObject.requestedPredicates as HashMap<String, RequestedAttribute>).size)
        Assertions.assertTrue(actual.contains("testCredentialId"))
        Assertions.assertTrue(actual.contains("testPredicateCredentialId"))
    }

    @Test
    @DisplayName("getCredentialIdentifiers works if requestedAttributes is null")
    fun getCredentialIdentifiers_attributesNull() {
        //Arrange
        val testObject = RequestedCredentials()

        testObject.requestedAttributes = null

        //region add requestedPredicates
        val testRequestedPredicates = HashMap<String, RequestedAttribute>()

        val testRequestedPredicate = RequestedAttribute()
        testRequestedPredicate.credentialId = "testPredicateCredentialId"
        testRequestedPredicate.timestamp = 25
        testRequestedPredicate.revealed = false

        testRequestedPredicates["testPredicateKey"] = testRequestedPredicate

        testObject.requestedPredicates = testRequestedPredicates
        //endregion

        //Act
        val actual = testObject.getCredentialIdentifiers()

        //Assert
        Assertions.assertEquals(1, actual.size)
        Assertions.assertTrue(actual.contains("testPredicateCredentialId"))
    }

    @Test
    @DisplayName("getCredentialIdentifiers works if requestedPredicates is null")
    fun getCredentialIdentifiers_predicatesNull() {
        //Arrange
        val testObject = RequestedCredentials()

        //region add requestedAttributes
        val testRequestedAttributes = HashMap<String, RequestedAttribute>()

        val testRequestedAttribute = RequestedAttribute()
        testRequestedAttribute.credentialId = "testCredentialId"
        testRequestedAttribute.timestamp = 15
        testRequestedAttribute.revealed = true

        testRequestedAttributes["testAttributeKey"] = testRequestedAttribute

        testObject.requestedAttributes = testRequestedAttributes
        //endregion

        //Act
        val actual = testObject.getCredentialIdentifiers()

        //Assert
        Assertions.assertEquals(1, actual.size)
        Assertions.assertTrue(actual.contains("testCredentialId"))
    }

    @Test
    @DisplayName("getCredentialIdentifiers works if a requestedAttribute has no credentialId")
    fun getCredentialIdentifiers_missingCredId() {
        //Arrange
        val testObject = RequestedCredentials()

        //region add requestedAttributes
        val testRequestedAttributes = HashMap<String, RequestedAttribute>()

        val testRequestedAttribute = RequestedAttribute()
        testRequestedAttribute.credentialId = null
        testRequestedAttribute.timestamp = 15
        testRequestedAttribute.revealed = true

        testRequestedAttributes["testAttributeKey"] = testRequestedAttribute

        testObject.requestedAttributes = testRequestedAttributes
        //endregion

        //region add requestedPredicates
        val testRequestedPredicates = HashMap<String, RequestedAttribute>()

        val testRequestedPredicate = RequestedAttribute()
        testRequestedPredicate.credentialId = "testPredicateCredentialId"
        testRequestedPredicate.timestamp = 25
        testRequestedPredicate.revealed = false

        testRequestedPredicates["testPredicateKey"] = testRequestedPredicate

        testObject.requestedPredicates = testRequestedPredicates
        //endregion

        //Act
        val actual = testObject.getCredentialIdentifiers()

        //Assert
        Assertions.assertEquals(1, actual.size)
        Assertions.assertTrue(actual.contains("testPredicateCredentialId"))
    }
}