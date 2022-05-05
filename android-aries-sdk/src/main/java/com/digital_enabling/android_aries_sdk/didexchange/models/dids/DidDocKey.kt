package com.digital_enabling.android_aries_sdk.didexchange.models.dids

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Strongly type DID doc key model.
 */
@Serializable
data class DidDocKey(
    /**
     * The id of the key.
     */
    @SerialName("id")
    var id: String,
    /**
     *  The type of the key.
     */
    @SerialName("type")
    var type: String,
    /**
     * The controller key.
     */
    @SerialName("controller")
    var controller: String,
    /**
     * The PEM representation of the key.
     */
    @SerialName("publicKeyBase58")
    var publicKeyBase58: String
) {}