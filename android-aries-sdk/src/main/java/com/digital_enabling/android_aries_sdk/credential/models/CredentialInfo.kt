package com.digital_enabling.android_aries_sdk.credential.models

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a credential stored in the wallet.
 */
@Serializable
data class CredentialInfo(
    /**
     * The referent
     */
    @SerialName("referent")
    var referent: String,
    /**
     * The attributes.
     */
    @Required
    @SerialName("attrs")
    var attributes: Map<String, String>,
    /**
     * The schema identifier.
     */
    @Required
    @SerialName("schema_id")
    var schemaId: String,
    /**
     * The credential definition identifier.
     */
    @Required
    @SerialName("cred_def_id")
    var credentialDefinitionId: String,
    /**
     * The revocation registry identifier.
     */
    @Required
    @SerialName("rev_reg_id")
    var revocationRegistryId: String,
    /**
     * The credential revocation identifier.
     */
    @Required
    @SerialName("cred_rev_id")
    var credentialRevocationId: String
) {
}