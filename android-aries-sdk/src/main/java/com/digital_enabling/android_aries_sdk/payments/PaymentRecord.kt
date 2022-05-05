package com.digital_enabling.android_aries_sdk.payments

import com.digital_enabling.android_aries_sdk.payments.models.PaymentDetails
import com.digital_enabling.android_aries_sdk.payments.models.PaymentState
import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Payment record representing an abstraction of payment workflow as described in aries-rfc
 * https://github.com/hyperledger/aries-rfcs/tree/master/features/0075-payment-decorators
 * Initializes a new instance of the [PaymentRecord] class.
 * @see RecordBase
 */
@Serializable
class PaymentRecord(override val id: String): RecordBase() {
    /**
     * The name of the type.
     */
    override var typeName: String = RecordType.PAYMENT_RECORD.typeName

    /**
     * The state of this record.
     */
    private var _state: PaymentState = PaymentState.NONE

    /**
     * The payment amount.
     */
    var amount: ULong = 0u

    /**
     * The payment details for this payment
     */
    var details: PaymentDetails? = null

    /**
     * The record associated with this payment.
     * Ex: CredentialRecord, SchemaRecord, etc.
     */
    @Transient
    var recordId: String? = null

    /**
     * The receipt for this payment.
     * Receipt can be represented as UTXO source.
     */
    @Transient
    var receiptId: String? = null

    /**
     * The method.
     */
    @Transient
    var method: String? = null

    /**
     * The payment address or Payee Id.
     */
    @Transient
    var address: String? = null

    /**
     * The connection identifier.
     */
    @Transient
    var connectionId: String? = null

    /**
     * The reference identifier.
     */
    @Transient
    var referenceId: String? = null

    var state: PaymentState
        get() = _state
        set(value) {
            _state = value
        }

    // TODO Statemachine
    override fun toJson(): String =
        Json.encodeToString(this)
}