package com.digital_enabling.android_aries_sdk.payments.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class IndyPaymentOutputSourceTests {

    @Test
    @DisplayName("Constructor works properly with all arguments set.")
    fun constructor_works(){
        val testObject = IndyPaymentOutputSource("testRecipient", 1337u, "testReceipt", "testExtra")

        assertEquals("testRecipient", testObject.recipient)
        assertEquals(1337, testObject.amount.toLong())
        assertEquals("testReceipt", testObject.receipt)
        assertEquals("testExtra", testObject.extra)
    }
}