package com.digital_enabling.android_aries_sdk.ledger.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Constraint metadata
 */
@Serializable
data class ConstraintMetadata(
    /**
     * The fee.
     */
    @SerialName("fees")
    var fee: String
)
