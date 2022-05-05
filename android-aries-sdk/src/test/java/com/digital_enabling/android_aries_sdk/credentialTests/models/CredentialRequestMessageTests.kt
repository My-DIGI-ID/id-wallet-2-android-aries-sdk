package com.digital_enabling.android_aries_sdk.credentialTests.models

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.credential.models.CredentialPreviewAttribute
import com.digital_enabling.android_aries_sdk.credential.models.CredentialPreviewMessage
import com.digital_enabling.android_aries_sdk.credential.models.CredentialRequestMessage
import com.digital_enabling.android_aries_sdk.decorators.attachments.Attachment
import com.digital_enabling.android_aries_sdk.decorators.attachments.AttachmentContent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CredentialRequestMessageTests {
    @Test
    @DisplayName("Constructor of CredentialRequestMessage works without parameters.")
    fun constructor_without_parameter_works() {
        val testObject = CredentialRequestMessage()

        Assertions.assertTrue(testObject.id != null)
        Assertions.assertEquals(MessageTypes.IssueCredentialNames.REQUEST_CREDENTIAL, testObject.type)
    }

    @Test
    @DisplayName("Constructor of CredentialRequestMessage works with parameters.")
    fun constructor_with_parameter_works() {
        val testObject = CredentialRequestMessage(true)

        Assertions.assertTrue(testObject.id != null)
        Assertions.assertEquals(
            MessageTypesHttps.IssueCredentialNames.REQUEST_CREDENTIAL,
            testObject.type
        )
    }

    @Test
    @DisplayName("Serialization of CredentialPreviewMessage works.")
    fun serialization_works() {
        val testObject = CredentialRequestMessage()
        val testAttachment = Attachment("testId")
        val testAttachmentContent = AttachmentContent()
        testAttachment.data = testAttachmentContent
        testObject.comment = "testComment"
        testObject.request = arrayOf(testAttachment)

        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        Assertions.assertTrue(serializedObject.containsKey("comment"))
        Assertions.assertTrue(serializedObject.containsKey("requests~attach"))
    }
}