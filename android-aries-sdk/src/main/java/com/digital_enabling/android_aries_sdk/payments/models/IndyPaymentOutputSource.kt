package com.digital_enabling.android_aries_sdk.payments.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Payment output source data
 */
@Serializable
data class IndyPaymentOutputSource(
    /**
     * The recipient.
     */
    @SerialName("recipient")
    var recipient: String,
    /**
     * The amount.
     */
    @SerialName("amount")
    var amount: ULong,
    /**
     * The receipt (utxo source).
     */
    @SerialName("receipt")
    var receipt: String,
    /**
     * The extra details.
     */
    @SerialName("extra")
    var extra: String
)