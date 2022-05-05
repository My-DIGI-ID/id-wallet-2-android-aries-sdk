package com.digital_enabling.android_aries_sdk.credentialTests.models

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.credential.models.CredentialOfferMessage
import com.digital_enabling.android_aries_sdk.credential.models.CredentialPreviewAttribute
import com.digital_enabling.android_aries_sdk.credential.models.CredentialPreviewMessage
import com.digital_enabling.android_aries_sdk.decorators.attachments.Attachment
import com.digital_enabling.android_aries_sdk.decorators.attachments.AttachmentContent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CredentialOfferMessageTests {

    @Test
    @DisplayName("Constructor of CredentialOfferMessage works without parameters.")
    fun constructor_without_parameter_works() {
        val testObject = CredentialOfferMessage()

        Assertions.assertTrue(testObject.id != null)
        Assertions.assertEquals(MessageTypes.IssueCredentialNames.OFFER_CREDENTIAL, testObject.type)
    }

    @Test
    @DisplayName("Constructor of CredentialOfferMessage works with parameters.")
    fun constructor_with_parameter_works() {
        val testObject = CredentialOfferMessage(true)

        Assertions.assertTrue(testObject.id != null)
        Assertions.assertEquals(
            MessageTypesHttps.IssueCredentialNames.OFFER_CREDENTIAL,
            testObject.type
        )
    }

    @Test
    @DisplayName("Serialization of CredentialOfferMessage works.")
    fun serialization_works() {
        val testObject = CredentialOfferMessage()
        testObject.comment = "testComment"
        val testCredentialPreviewMessage = CredentialPreviewMessage()
        val testCredentialPreviewAttribute = CredentialPreviewAttribute("testName", "testValue")
        testCredentialPreviewMessage.attributes = arrayOf(testCredentialPreviewAttribute)
        testObject.credentialPreview = testCredentialPreviewMessage
        val testAttachment = Attachment("testId")
        testAttachment.data = AttachmentContent()
        testObject.offers = arrayOf(testAttachment)

        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        Assertions.assertTrue(serializedObject.containsKey("comment"))
        Assertions.assertTrue(serializedObject.containsKey("credential_preview"))
        Assertions.assertTrue(serializedObject.containsKey("offers~attach"))
    }
}