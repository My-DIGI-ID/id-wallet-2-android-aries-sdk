package com.digital_enabling.android_aries_sdk.payments.models

import com.digital_enabling.android_aries_sdk.payments.PaymentAddressRecord
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class TransactionCostTests {

    @Test
    @DisplayName("Constructor works properly with all arguments set.")
    fun constructor_works(){
        val mockAddress = PaymentAddressRecord()
        val testObject = TransactionCost(mockAddress, 1337u, "testMethod")

        assertEquals(mockAddress, testObject.paymentAddress)
        assertEquals(1337, testObject.amount.toLong())
        assertEquals("testMethod", testObject.paymentMethod)
    }
}