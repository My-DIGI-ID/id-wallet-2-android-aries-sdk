package com.digital_enabling.android_aries_sdk.credentialTests.models

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.credential.models.CredentialPreviewAttribute
import com.digital_enabling.android_aries_sdk.credential.models.CredentialPreviewMessage
import com.digital_enabling.android_aries_sdk.credential.models.CredentialProposalMessage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CredentialProposalMessageTests {
    @Test
    @DisplayName("Constructor of CredentialProposalMessage works without parameters.")
    fun constructor_without_parameter_works() {
        val testObject = CredentialProposalMessage()

        Assertions.assertTrue(testObject.id != null)
        Assertions.assertEquals(MessageTypes.IssueCredentialNames.PREVIEW_CREDENTIAL, testObject.type)
    }

    @Test
    @DisplayName("Constructor of CredentialProposalMessage works with parameters.")
    fun constructor_with_parameter_works() {
        val testObject = CredentialProposalMessage(true)

        Assertions.assertTrue(testObject.id != null)
        Assertions.assertEquals(
            MessageTypesHttps.IssueCredentialNames.PREVIEW_CREDENTIAL,
            testObject.type
        )
    }

    @Test
    @DisplayName("Serialization of CredentialProposalMessage works.")
    fun serialization_works() {
        val testObject = CredentialProposalMessage()
        testObject.comment = "testComment"
        testObject.schemaId = "testSchemaId"
        testObject.credentialDefinitionId = "testCredentialDefinitionId"

        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        Assertions.assertTrue(serializedObject.containsKey("comment"))
        Assertions.assertTrue(serializedObject.containsKey("schema_id"))
        Assertions.assertTrue(serializedObject.containsKey("cred_def_id"))
    }
}