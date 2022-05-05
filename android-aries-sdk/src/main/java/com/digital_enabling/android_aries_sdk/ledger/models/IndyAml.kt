package com.digital_enabling.android_aries_sdk.ledger.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Acceptance Mechanisms List Data
 */
@Serializable
data class IndyAml (
    /**
     * Aml context.
     */
    @SerialName("amlContext")
    var amlContext : String,
    /**
     * Aml version.
     */
    @SerialName("version")
    var version : String,
    /**
     * Acceptance Mechanisms List
     */
    @SerialName("aml")
    var aml : Map<String, String>
)