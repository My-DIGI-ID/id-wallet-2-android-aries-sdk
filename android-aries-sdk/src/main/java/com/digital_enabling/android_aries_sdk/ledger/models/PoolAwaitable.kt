package com.digital_enabling.android_aries_sdk.ledger.models

import org.hyperledger.indy.sdk.pool.Pool

/**
 * Awaitable pool handle.
 * Initializes a new instance of the [PoolAwaitable].
 * @param initializer Initializer.
 */
class PoolAwaitable(private val initializer: () -> Pool) {

    /**
     * The awaiter.
     */
    fun getAwaiter(): Pool{
        return initializer.invoke()
    }

    companion object {
        /**
         * Create new [PoolAwaitable] instance from existing [Pool] handle
         * @return The pool awatable.
         * @param pool Pool.
         */
        fun fromPool(pool: () -> Pool): PoolAwaitable{
            return PoolAwaitable(pool)
        }
    }
}