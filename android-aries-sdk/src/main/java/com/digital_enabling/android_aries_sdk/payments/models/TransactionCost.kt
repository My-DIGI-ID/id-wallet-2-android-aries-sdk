package com.digital_enabling.android_aries_sdk.payments.models

import com.digital_enabling.android_aries_sdk.payments.PaymentAddressRecord

/**
 * Transaction cost data
 */
data class TransactionCost (
    /**
     * The payment address used for paying for this cost.
     */
    var paymentAddress: PaymentAddressRecord,
    /**
     * The amount.
     */
    var amount: ULong = 0u,
    /**
     * The payment method.
     */
    var paymentMethod: String
)