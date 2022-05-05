package com.digital_enabling.android_aries_sdk.payments

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.payments.models.AddressOptions
import com.digital_enabling.android_aries_sdk.payments.models.PaymentDetails
import com.digital_enabling.android_aries_sdk.payments.models.TransactionCost

/**
 * @see IPaymentService
 */
class DefaultPaymentService:IPaymentService {
    override fun getDefaultPaymentAddress(agentContext: IAgentContext): PaymentAddressRecord {
        TODO("Not yet implemented")
    }

    override fun setDefaultPaymentAddress(
        agentContext: IAgentContext,
        addressRecord: PaymentAddressRecord
    ) {
        TODO("Not yet implemented")
    }

    override fun createPaymentAddress(
        agentContext: IAgentContext,
        configuration: AddressOptions?
    ): PaymentAddressRecord {
        TODO("Not yet implemented")
    }

    override fun attachPaymentRequest(
        context: IAgentContext,
        agentMessage: AgentMessage,
        details: PaymentDetails,
        paymentAddressRecord: PaymentAddressRecord?
    ): PaymentRecord {
        TODO("Not yet implemented")
    }

    override fun attachPaymentReceipt(
        context: IAgentContext,
        agentMessage: AgentMessage,
        paymentRecord: PaymentRecord
    ) {
        TODO("Not yet implemented")
    }

    override fun makePayment(
        agentContext: IAgentContext,
        paymentRecord: PaymentRecord,
        addressRecord: PaymentAddressRecord?
    ) {
        TODO("Not yet implemented")
    }

    override fun refreshBalance(
        agentContext: IAgentContext,
        paymentAddressRecord: PaymentAddressRecord?
    ) {
        TODO("Not yet implemented")
    }

    override fun getTransactionCost(
        context: IAgentContext,
        transactionType: String,
        paymentAddressRecord: PaymentAddressRecord?
    ): TransactionCost {
        TODO("Not yet implemented")
    }

    override fun getTransactionFee(context: IAgentContext, transactionType: String): ULong {
        TODO("Not yet implemented")
    }

    override fun verifyPayment(context: IAgentContext, paymentRecord: PaymentRecord): Boolean {
        TODO("Not yet implemented")
    }

}
