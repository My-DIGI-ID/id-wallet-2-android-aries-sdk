package com.digital_enabling.android_aries_sdk.payments

import com.digital_enabling.android_aries_sdk.payments.models.IndyPaymentInputSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PaymentAddressRecordTests {

    @Test
    @DisplayName("Balance returns 0 when sources list is empty.")
    fun balance_withEmptyList() {
        val testObject = PaymentAddressRecord()

        val expected: Long = 0
        val actual = testObject.balance

        assertEquals(expected, actual.toLong())
    }

    @Test
    @DisplayName("Balance returns sum of source amounts.")
    fun balance_withFilledList() {
        val testSource1 = IndyPaymentInputSource("", "", 1u, "")
        val testSource2 = IndyPaymentInputSource("", "", 1u, "")
        val testSource3 = IndyPaymentInputSource("", "", 1u, "")


        val testObject = PaymentAddressRecord()
        testObject.sources = listOf(testSource1, testSource2, testSource3)

        val expected: Long = 3
        val actual = testObject.balance

        assertEquals(expected, actual.toLong())
    }

}