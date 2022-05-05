package com.digital_enabling.android_aries_sdk.payments

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.payments.models.AddressOptions
import com.digital_enabling.android_aries_sdk.payments.models.PaymentDetails
import com.digital_enabling.android_aries_sdk.payments.models.TransactionCost

/**
 * Payment Service Interface
 */
interface IPaymentService {
    /**
     * Gets the default payment address for the given agent context.
     * @param agentContext The Agent context.
     */
    fun getDefaultPaymentAddress(agentContext: IAgentContext): PaymentAddressRecord

    /**
     * Sets the given address as default payment address.
     * @param agentContext The Agent context.
     * @param addressRecord
     */
    fun setDefaultPaymentAddress(
        agentContext: IAgentContext,
        addressRecord: PaymentAddressRecord
    )

    /**
     * Creates a new payment address record.
     * @param agentContext The Agent context.
     * @param configuration
     */
    fun createPaymentAddress(
        agentContext: IAgentContext,
        configuration: AddressOptions? = null
    ): PaymentAddressRecord

    /**
     * Attaches a payment request to the given agent message.
     * @param context The Agent context.
     * @param agentMessage The message.
     * @param details The details of the payment
     * @param paymentAddressRecord The address this payment will be processed to
     * @return Payment record that can be used to reference this payment.
     */
    fun attachPaymentRequest(
        context: IAgentContext,
        agentMessage: AgentMessage,
        details: PaymentDetails,
        paymentAddressRecord: PaymentAddressRecord? = null
    ): PaymentRecord

    /**
     * Attach a payment receipt to the given agent message based on the payment record.
     * @param context The Agent context.
     * @param agentMessage The message.
     * @param paymentRecord
     */
    fun attachPaymentReceipt(
        context: IAgentContext,
        agentMessage: AgentMessage,
        paymentRecord: PaymentRecord
    )

    /**
     * Makes a payment for the given payment record.
     * @param agentContext The Agent context.
     * @param paymentRecord The payment record
     * @param addressRecord The address to use to make this payment from. If null,
     * the default payment address will be used
     */
    fun makePayment(
        agentContext: IAgentContext,
        paymentRecord: PaymentRecord,
        addressRecord: PaymentAddressRecord? = null
    )

    /**
     * Refresh the payment address sources from the ledger.
     * @param agentContext The Agent context.
     * @param paymentAddressRecord The address to refresh. If null, the default payment
     * address will be used
     */
    fun refreshBalance(
        agentContext: IAgentContext,
        paymentAddressRecord: PaymentAddressRecord? = null
    )

    /**
     * Creates a payment info object for the given transaction type. The payment info
     * makes auto discovery of the fees required by querying the ledger.
     * @param context The Agent context.
     * @param transactionType
     * @param paymentAddressRecord
     * @return
     */
    fun getTransactionCost(
        context: IAgentContext,
        transactionType: String,
        paymentAddressRecord: PaymentAddressRecord? = null
    ): TransactionCost

    /**
     * Gets the fees associated with a given transaction type
     * @param context The Agent context.
     * @param transactionType
     * @return
     */
    fun getTransactionFee(context: IAgentContext, transactionType: String): ULong

    /**
     * Verifies the payment record on the ledger by comparing the receipts and amounts
     * @param context The Agent context.
     * @param paymentRecord The payment record to verify
     * @return
     */
    fun verifyPayment(context: IAgentContext, paymentRecord: PaymentRecord): Boolean
}