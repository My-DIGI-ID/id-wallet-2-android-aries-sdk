package com.digital_enabling.android_aries_sdk.payments.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a payment source
 */
@Serializable
data class IndyPaymentInputSource (
    /**
     * The payment address.
     */
    @SerialName("paymentAddress")
    var paymentAddress: String,
    /**
     * The source.
     */
    @SerialName("source")
    var source: String,
    /**
     * The amount.
     */
    @SerialName("amount")
    var amount: ULong = 0u,
    /**
     * The extra.
     */
    @SerialName("extra")
    var extra: String
)