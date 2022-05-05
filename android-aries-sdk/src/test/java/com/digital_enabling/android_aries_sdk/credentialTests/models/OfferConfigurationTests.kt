package com.digital_enabling.android_aries_sdk.credentialTests.models

import com.digital_enabling.android_aries_sdk.credential.models.CredentialPreviewAttribute
import com.digital_enabling.android_aries_sdk.credential.models.OfferConfiguration
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.HashMap

class OfferConfigurationTests {
    @Test
    @DisplayName("Constructor of CredentialPreviewAttribute works without parameters.")
    fun constructor_without_parameter_works() {
        val testCredentialPreviewAttribute = CredentialPreviewAttribute("testName", "testValue")
        val testIterable = listOf(testCredentialPreviewAttribute)
        val testObject = OfferConfiguration("testCredentialDefinitionId", "testIssuerDid", testIterable, HashMap<String,String>())

        Assertions.assertEquals("testCredentialDefinitionId", testObject.credentialDefinitionId)
        Assertions.assertEquals("testIssuerDid", testObject.issuerDid)
        Assertions.assertEquals(testIterable, testObject.credentialAttributeValues)
    }
}