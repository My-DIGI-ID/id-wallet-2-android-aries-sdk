package com.digital_enabling.android_aries_sdk.credentialTests.models

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.credential.models.CredentialIssueMessage
import com.digital_enabling.android_aries_sdk.credential.models.CredentialPreviewAttribute
import com.digital_enabling.android_aries_sdk.decorators.attachments.Attachment
import com.digital_enabling.android_aries_sdk.decorators.attachments.AttachmentContent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CredentialPreviewAttributeTests {

    @Test
    @DisplayName("Constructor of CredentialPreviewAttribute works without parameters.")
    fun constructor_without_parameter_works() {
        val testObject = CredentialPreviewAttribute()

        Assertions.assertEquals("text/plain", testObject.mimeType)
    }

    @Test
    @DisplayName("Constructor of CredentialPreviewAttribute works with parameters.")
    fun constructor_with_parameter_works() {
        val testObject = CredentialPreviewAttribute("testName", "testValue")

        Assertions.assertEquals("text/plain", testObject.mimeType)
        Assertions.assertEquals("testName", testObject.name)
        Assertions.assertEquals("testValue", testObject.value)
    }

    @Test
    @DisplayName("Serialization of CredentialPreviewAttribute works.")
    fun serialization_works() {
        val testObject = CredentialPreviewAttribute("testName", "testValue")

        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        Assertions.assertTrue(serializedObject.containsKey("name"))
        Assertions.assertTrue(serializedObject.containsKey("mime-type"))
        Assertions.assertTrue(serializedObject.containsKey("value"))
    }
}