package com.digital_enabling.android_aries_sdk.payments.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class IndyPaymentInputSourceTests {

    @Test
    @DisplayName("Constructor works properly with all arguments set.")
    fun constructor_works(){
        val testObject = IndyPaymentInputSource("testAddress", "testSource", 1337u, "testExtra")

        assertEquals("testAddress", testObject.paymentAddress)
        assertEquals("testSource", testObject.source)
        assertEquals(1337, testObject.amount.toLong())
        assertEquals("testExtra", testObject.extra)
    }

}