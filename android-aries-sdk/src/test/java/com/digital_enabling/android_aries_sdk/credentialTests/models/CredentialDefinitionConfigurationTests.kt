package com.digital_enabling.android_aries_sdk.credentialTests.models

import com.digital_enabling.android_aries_sdk.credential.models.CredentialDefinitionConfiguration
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CredentialDefinitionConfigurationTests {

    @Test
    @DisplayName("Constructor of CredentialDefinitionConfiguration works.")
    fun constructor_works() {
        //Arrange
        val testObject = CredentialDefinitionConfiguration(
            "testSchema",
            "testIssuerDid",
            "testTag",
            false,
            revocationRegistryAutoScale = true,
            revocationRegistryBaseUri = "testUri"
        )
        //Act


        //Assert
        Assertions.assertEquals("testSchema", testObject.schemaId)
        Assertions.assertEquals("testIssuerDid", testObject.issuerDid)
        Assertions.assertEquals("testTag", testObject.tag)
        Assertions.assertEquals(false, testObject.enableRevocation)
        Assertions.assertEquals(1024, testObject.revocationRegistrySize)
        Assertions.assertEquals(true, testObject.revocationRegistryAutoScale)
        Assertions.assertEquals("testUri", testObject.revocationRegistryBaseUri)
    }
}