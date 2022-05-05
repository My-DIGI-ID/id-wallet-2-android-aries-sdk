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

class CredentialPreviewMessageTests {
    @Test
    @DisplayName("Constructor of CredentialPreviewMessage works without parameters.")
    fun constructor_without_parameter_works() {
        val testObject = CredentialPreviewMessage()

        Assertions.assertTrue(testObject.id != null)
        Assertions.assertEquals(MessageTypes.IssueCredentialNames.PREVIEW_CREDENTIAL, testObject.type)
    }

    @Test
    @DisplayName("Constructor of CredentialPreviewMessage works with parameters.")
    fun constructor_with_parameter_works() {
        val testObject = CredentialPreviewMessage(true)

        Assertions.assertTrue(testObject.id != null)
        Assertions.assertEquals(
            MessageTypesHttps.IssueCredentialNames.PREVIEW_CREDENTIAL,
            testObject.type
        )
    }

    @Test
    @DisplayName("Serialization of CredentialPreviewMessage works.")
    fun serialization_works() {
        val testObject = CredentialPreviewMessage()
        val testCredentialPreviewAttributes = CredentialPreviewAttribute("testName", "testValue")
        testObject.attributes = arrayOf(testCredentialPreviewAttributes)

        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        Assertions.assertTrue(serializedObject.containsKey("attributes"))
    }
}