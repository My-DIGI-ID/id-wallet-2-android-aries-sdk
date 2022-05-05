package com.digital_enabling.android_aries_sdk.credential.models

/**
 * Credential definition configuration
 */
data class CredentialDefinitionConfiguration(
    var schemaId: String,
    var issuerDid: String,
    var tag: String = "default",
    var enableRevocation : Boolean,
    var revocationRegistrySize: Int = 1024,
    var revocationRegistryAutoScale: Boolean = true,
    var revocationRegistryBaseUri: String
) {
}