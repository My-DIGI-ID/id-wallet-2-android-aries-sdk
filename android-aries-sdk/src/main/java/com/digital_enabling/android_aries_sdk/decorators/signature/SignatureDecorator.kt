package com.digital_enabling.android_aries_sdk.decorators.signature

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Signature decorator model.
 */
@Serializable
class SignatureDecorator {
    /**
     * Signature type.
     */
    @SerialName("@type")
    var signatureType: String? = null

    /**
     *  Signature data.
     */
    @SerialName("sig_data")
    var signatureData: String? = null

    /**
     * Signer public key.
     */
    @SerialName("signer")
    var signer: String? = null

    /**
     * Signature
     */
    @SerialName("signature")
    var signature: String? = null
}