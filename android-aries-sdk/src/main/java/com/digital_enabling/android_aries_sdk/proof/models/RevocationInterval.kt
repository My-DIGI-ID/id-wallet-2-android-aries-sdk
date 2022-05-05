package com.digital_enabling.android_aries_sdk.proof.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Proof revocation interval.
 */
@Serializable
data class RevocationInterval(
    /**
     * Start of interval
     */
    @SerialName("from")
    var from: UInt? = null,
    /**
     * End of interval
     */
    @SerialName("to")
    var to: UInt? = null
)