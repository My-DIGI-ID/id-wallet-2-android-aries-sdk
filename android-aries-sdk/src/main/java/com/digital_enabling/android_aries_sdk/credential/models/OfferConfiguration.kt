package com.digital_enabling.android_aries_sdk.credential.models

import kotlinx.serialization.Serializable
/**
 * Config for controlling credential offer creation.
 */
@Serializable
data class OfferConfiguration(
    /**
     * Id of the credential definition used to create the credential offer.
     */
    var credentialDefinitionId: String,
    /**
     * Did of the issuer generating the offer.
     */
    var issuerDid: String,
    /**
     * [Optional] For setting the credential values at the offer stage.
     * Note these attributes are not disclosed in the offer.
     */
    var credentialAttributeValues: List<CredentialPreviewAttribute>,
    /**
     * Controls the tags that are persisted against the offer record.
     */
    var tags: Map<String, String>
)