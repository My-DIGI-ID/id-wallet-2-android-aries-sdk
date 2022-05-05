package com.digital_enabling.android_aries_sdk.credentialTests.models

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.credential.models.CredentialIssueMessage
import com.digital_enabling.android_aries_sdk.decorators.attachments.Attachment
import com.digital_enabling.android_aries_sdk.decorators.attachments.AttachmentContent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CredentialIssueMessageTests {
    @Test
    @DisplayName("Constructor of CredentialIssueMessage works without parameters.")
    fun constructor_without_parameter_works() {
        val testObject = CredentialIssueMessage()

        Assertions.assertTrue(testObject.id != null)
        Assertions.assertEquals(MessageTypes.IssueCredentialNames.ISSUE_CREDENTIAL, testObject.type)
    }

    @Test
    @DisplayName("Constructor of CredentialIssueMessage works with parameters.")
    fun constructor_with_parameter_works() {
        val testObject = CredentialIssueMessage(true)

        Assertions.assertTrue(testObject.id != null)
        Assertions.assertEquals(MessageTypesHttps.IssueCredentialNames.ISSUE_CREDENTIAL, testObject.type)
    }

    @Test
    @DisplayName("Serialization of CredentialIssueMessage works.")
    fun serialization_works() {
        val testObject = CredentialIssueMessage()
        testObject.comment = "testComment"
        val attachment = Attachment("testId")
        attachment.data = AttachmentContent()
        testObject.credentials = listOf(attachment)

        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        Assertions.assertTrue(serializedObject.containsKey("comment"))
        Assertions.assertTrue(serializedObject.containsKey("credentials~attach"))
    }
}