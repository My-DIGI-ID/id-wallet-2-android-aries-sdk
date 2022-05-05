package com.digital_enabling.android_aries_sdk.ledgerTests.models

import com.digital_enabling.android_aries_sdk.ledger.models.PoolAwaitable
import org.hyperledger.indy.sdk.pool.Pool
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.verify

class PoolAwaitableTests {

    private val mockPool = Mockito.mock(Pool::class.java)

    //region Tests for getAwaiter()
    @Test
    @DisplayName("GetAwaiter method invokes the initializer given in constructor.")
    fun getAwaiter_works(){
        val testInitializer = { mockPool }
        val testObject = PoolAwaitable(testInitializer)

        val actual = testObject.getAwaiter()

        assertEquals(mockPool, actual)
    }
    //endregion

    //region Tests for fromPool()
    @Test
    @DisplayName("FromPool method creates a proper PoolAwaitable object with given initializer.")
    fun fromPool_works(){
        val actual = PoolAwaitable.fromPool { mockPool }
        assertTrue(actual is PoolAwaitable)
    }
    //endregion
}