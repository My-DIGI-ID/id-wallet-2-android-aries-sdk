package com.digital_enabling.android_aries_sdk.proof.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Requested attribute dto.
 */
@Serializable
data class RequestedAttribute(
    /**
     * The credential identifier.
     */
    @SerialName("cred_id")
    var credentialId: String? = null,
    /**
     * The timestamp.
     */
    @SerialName("timestamp")
    var timestamp: Long? = null,
    /**
     * The revealed.
     */
    @SerialName("revealed")
    var revealed: Boolean? = null
)