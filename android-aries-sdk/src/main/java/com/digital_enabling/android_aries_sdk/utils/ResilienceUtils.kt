package com.digital_enabling.android_aries_sdk.utils

object ResilienceUtils {

    fun <T, E> retryPolicy(action: () -> T, exceptionPredicate: (() -> Pair<E?, Boolean>) = { Pair<E?, Boolean>(null, false) }): T where E : Exception{
        TODO("Find a good resilience framework")
    }
}