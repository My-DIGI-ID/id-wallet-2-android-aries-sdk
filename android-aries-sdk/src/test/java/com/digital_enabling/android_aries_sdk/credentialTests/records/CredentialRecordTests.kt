package com.digital_enabling.android_aries_sdk.credentialTests.records

import com.digital_enabling.android_aries_sdk.credential.models.CredentialPreviewAttribute
import com.digital_enabling.android_aries_sdk.credential.records.CredentialRecord
import com.digital_enabling.android_aries_sdk.credential.records.CredentialState
import com.digital_enabling.android_aries_sdk.credential.records.CredentialTrigger
import com.digital_enabling.android_aries_sdk.utils.RecordType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CredentialRecordTests {
    @Test
    @DisplayName("Constructor of CredentialRecord works.")
    fun constructor_works() {
        val testObject = CredentialRecord("testId")
        testObject.schemaId = "testSchemaId"
        testObject.connectionId = "testConnectionId"
        testObject.credentialRequestMetadataJson = "testJson"
        testObject.credentialId = "testCredentialId"
        val testCredentialAttributeValues =
            listOf<CredentialPreviewAttribute>(CredentialPreviewAttribute("name", "value"))
        testObject.credentialAttributesValues = testCredentialAttributeValues
        testObject.revocationRegistryId = "testRevocationRegistryId"
        testObject.requestJson = "testJson"
        testObject.offerJson = "testOfferJson"
        testObject.credentialRevocationId = "testCredentialRevocationId"


        Assertions.assertEquals("testId", testObject.id)
        Assertions.assertEquals(RecordType.CREDENTIAL_RECORD.typeName, testObject.typeName)
        Assertions.assertEquals(CredentialState.OFFERED, testObject.state)
        Assertions.assertEquals("testSchemaId", testObject.schemaId)
        Assertions.assertEquals("testConnectionId", testObject.connectionId)
        Assertions.assertEquals("testJson", testObject.credentialRequestMetadataJson)
        Assertions.assertEquals("testCredentialId", testObject.credentialId)
        Assertions.assertEquals(
            testCredentialAttributeValues,
            testObject.credentialAttributesValues
        )
        Assertions.assertEquals("testRevocationRegistryId", testObject.revocationRegistryId)
        Assertions.assertEquals("testJson", testObject.requestJson)
        Assertions.assertEquals("testCredentialRevocationId", testObject.credentialRevocationId)

    }

    @Test
    @DisplayName("test State OFFERED with Trigger REQUEST.")
    fun test_sOffered_tRequest() {
        //Arrange
        val testObject = CredentialRecord("testID")

        //Act
        testObject.trigger(CredentialTrigger.REQUEST)
        val actual = testObject.state
        val expected = CredentialState.REQUESTED

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test State OFFERED with Trigger REJECT.")
    fun test_sOffered_tReject() {
        //Arrange
        val testObject = CredentialRecord("testID")

        //Act
        testObject.trigger(CredentialTrigger.REJECT)
        val actual = testObject.state
        val expected = CredentialState.REJECTED

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test State OFFERED with Trigger ISSUE.")
    fun test_sOffered_tIssue() {
        //Arrange
        val testObject = CredentialRecord("testID")

        //Act

        //Assert
        assertThrows<Exception> { testObject.trigger(CredentialTrigger.ISSUE) }
    }

    @Test
    @DisplayName("test State OFFERED with Trigger REVOKE.")
    fun test_sOffered_tRevoke() {
        //Arrange
        val testObject = CredentialRecord("testID")

        //Act
        //Assert
        assertThrows<Exception> { testObject.trigger(CredentialTrigger.REVOKE) }
    }

    @Test
    @DisplayName("test State REQUESTED with Trigger REQUEST.")
    fun test_sRequested_tRequest() {
        //Arrange
        val testObject = CredentialRecord("testID")
        testObject.trigger(CredentialTrigger.REQUEST)
        val initialTestState = testObject.state

        //Act

        //Assert
        Assertions.assertEquals(CredentialState.REQUESTED, initialTestState)
        assertThrows<Exception> { testObject.trigger(CredentialTrigger.REQUEST) }
    }

    @Test
    @DisplayName("test State REQUESTED with Trigger REJECT.")
    fun test_sRequested_tReject() {
        //Arrange
        val testObject = CredentialRecord("testID")
        testObject.trigger(CredentialTrigger.REQUEST)
        val initialTestState = testObject.state

        //Act
        testObject.trigger(CredentialTrigger.REJECT)
        val actual = testObject.state
        val expected = CredentialState.REJECTED

        //Assert
        Assertions.assertEquals(CredentialState.REQUESTED, initialTestState)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test State REQUESTED with Trigger ISSUE.")
    fun test_sRequested_tIssue() {
        //Arrange
        val testObject = CredentialRecord("testID")
        testObject.trigger(CredentialTrigger.REQUEST)
        val initialTestState = testObject.state

        //Act
        testObject.trigger(CredentialTrigger.ISSUE)
        val actual = testObject.state
        val expected = CredentialState.ISSUED

        //Assert
        Assertions.assertEquals(CredentialState.REQUESTED, initialTestState)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test State REQUESTED with Trigger REVOKE.")
    fun test_sRequested_tRevoke() {
        //Arrange
        val testObject = CredentialRecord("testID")
        testObject.trigger(CredentialTrigger.REQUEST)
        val initialTestState = testObject.state

        //Act

        //Assert
        Assertions.assertEquals(CredentialState.REQUESTED, initialTestState)
        assertThrows<Exception> { testObject.trigger(CredentialTrigger.REVOKE) }
    }

    @Test
    @DisplayName("test State REJECTED with Trigger REQUEST.")
    fun test_sRejected_tRequest() {
        //Arrange
        val testObject = CredentialRecord("testID")
        testObject.trigger(CredentialTrigger.REQUEST)
        testObject.trigger(CredentialTrigger.REJECT)
        val initialTestState = testObject.state

        //Act

        //Assert
        Assertions.assertEquals(CredentialState.REJECTED, initialTestState)
        assertThrows<Exception> { testObject.trigger(CredentialTrigger.REQUEST) }
    }

    @Test
    @DisplayName("test State REJECTED with Trigger REJECT.")
    fun test_sRejected_tReject() {
        //Arrange
        val testObject = CredentialRecord("testID")
        testObject.trigger(CredentialTrigger.REQUEST)
        testObject.trigger(CredentialTrigger.REJECT)
        val initialTestState = testObject.state

        //Act

        //Assert
        Assertions.assertEquals(CredentialState.REJECTED, initialTestState)
        assertThrows<Exception> { testObject.trigger(CredentialTrigger.REJECT) }
    }

    @Test
    @DisplayName("test State REJECTED with Trigger ISSUE.")
    fun test_sRejected_tIssue() {
        //Arrange
        val testObject = CredentialRecord("testID")
        testObject.trigger(CredentialTrigger.REQUEST)
        testObject.trigger(CredentialTrigger.REJECT)
        val initialTestState = testObject.state

        //Act

        //Assert
        Assertions.assertEquals(CredentialState.REJECTED, initialTestState)
        assertThrows<Exception> { testObject.trigger(CredentialTrigger.ISSUE) }
    }

    @Test
    @DisplayName("test State REJECTED with Trigger REVOKE.")
    fun test_sRejected_tRevoke() {
        //Arrange
        val testObject = CredentialRecord("testID")
        testObject.trigger(CredentialTrigger.REQUEST)
        testObject.trigger(CredentialTrigger.REJECT)
        val initialTestState = testObject.state

        //Act

        //Assert
        Assertions.assertEquals(CredentialState.REJECTED, initialTestState)
        assertThrows<Exception> { testObject.trigger(CredentialTrigger.REVOKE) }
    }

    @Test
    @DisplayName("test State ISSUED with Trigger REQUEST.")
    fun test_sIssued_tRequest() {
        //Arrange
        val testObject = CredentialRecord("testID")
        testObject.trigger(CredentialTrigger.REQUEST)
        testObject.trigger(CredentialTrigger.ISSUE)
        val initialTestState = testObject.state

        //Act

        //Assert
        Assertions.assertEquals(CredentialState.ISSUED, initialTestState)
        assertThrows<Exception> { testObject.trigger(CredentialTrigger.REQUEST) }
    }

    @Test
    @DisplayName("test State ISSUED with Trigger REJECT.")
    fun test_sIssued_tReject() {
        //Arrange
        val testObject = CredentialRecord("testID")
        testObject.trigger(CredentialTrigger.REQUEST)
        testObject.trigger(CredentialTrigger.ISSUE)
        val initialTestState = testObject.state

        //Act

        //Assert
        Assertions.assertEquals(CredentialState.ISSUED, initialTestState)
        assertThrows<Exception> { testObject.trigger(CredentialTrigger.REJECT) }
    }

    @Test
    @DisplayName("test State ISSUED with Trigger ISSUE.")
    fun test_sIssued_tIssue() {
        //Arrange
        val testObject = CredentialRecord("testID")
        testObject.trigger(CredentialTrigger.REQUEST)
        testObject.trigger(CredentialTrigger.ISSUE)
        val initialTestState = testObject.state

        //Act

        //Assert
        Assertions.assertEquals(CredentialState.ISSUED, initialTestState)
        assertThrows<Exception> { testObject.trigger(CredentialTrigger.ISSUE) }
    }

    @Test
    @DisplayName("test State ISSUED with Trigger REVOKE.")
    fun test_sIssued_tRevoke() {
        //Arrange
        val testObject = CredentialRecord("testID")
        testObject.trigger(CredentialTrigger.REQUEST)
        testObject.trigger(CredentialTrigger.ISSUE)
        val initialTestState = testObject.state

        //Act
        testObject.trigger(CredentialTrigger.REVOKE)
        val actual = testObject.state
        val expected = CredentialState.REVOKED

        //Assert
        Assertions.assertEquals(CredentialState.ISSUED, initialTestState)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test State REVOKED with Trigger REQUEST.")
    fun test_sRevoked_tRequest() {
        //Arrange
        val testObject = CredentialRecord("testID")
        testObject.trigger(CredentialTrigger.REQUEST)
        testObject.trigger(CredentialTrigger.ISSUE)
        testObject.trigger(CredentialTrigger.REVOKE)
        val initialTestState = testObject.state

        //Act

        //Assert
        Assertions.assertEquals(CredentialState.REVOKED, initialTestState)
        assertThrows<Exception> { testObject.trigger(CredentialTrigger.REQUEST) }
    }

    @Test
    @DisplayName("test State REVOKED with Trigger REJECT.")
    fun test_sRevoked_tReject() {
        //Arrange
        val testObject = CredentialRecord("testID")
        testObject.trigger(CredentialTrigger.REQUEST)
        testObject.trigger(CredentialTrigger.ISSUE)
        testObject.trigger(CredentialTrigger.REVOKE)
        val initialTestState = testObject.state

        //Act

        //Assert
        Assertions.assertEquals(CredentialState.REVOKED, initialTestState)
        assertThrows<Exception> { testObject.trigger(CredentialTrigger.REJECT) }
    }

    @Test
    @DisplayName("test State REVOKED with Trigger ISSUE.")
    fun test_sRevoked_tIssue() {
        //Arrange
        val testObject = CredentialRecord("testID")
        testObject.trigger(CredentialTrigger.REQUEST)
        testObject.trigger(CredentialTrigger.ISSUE)
        testObject.trigger(CredentialTrigger.REVOKE)
        val initialTestState = testObject.state

        //Act

        //Assert
        Assertions.assertEquals(CredentialState.REVOKED, initialTestState)
        assertThrows<Exception> { testObject.trigger(CredentialTrigger.ISSUE) }
    }

    @Test
    @DisplayName("test State REVOKED with Trigger REVOKE.")
    fun test_sRevoked_tRevoke() {
        //Arrange
        val testObject = CredentialRecord("testID")
        testObject.trigger(CredentialTrigger.REQUEST)
        testObject.trigger(CredentialTrigger.ISSUE)
        testObject.trigger(CredentialTrigger.REVOKE)
        val initialTestState = testObject.state

        //Act

        //Assert
        Assertions.assertEquals(CredentialState.REVOKED, initialTestState)
        assertThrows<Exception> { testObject.trigger(CredentialTrigger.REVOKE) }
    }
}