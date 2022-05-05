package com.digital_enabling.android_aries_sdk.ledger.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Indy Transaction Author Agreement Model
 */
@Serializable
open class IndyTaa (
    /**
     * The text of the agreement
     */
    @SerialName("text")
    open var text: String,
    /**
     * The version of the agreement
     */
    @SerialName("version")
    open var version: String
)