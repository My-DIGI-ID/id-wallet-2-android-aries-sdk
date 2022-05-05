package com.digital_enabling.android_aries_sdk.utils

/**
 * Runtime configuration
 */
object Runtime {
    /**
     * Enables ledger lookup retry policy
     */
    const val LEDGER_LOOKUP_RETRY_FLAG = "EnableLedgerLookupRetryPolicy"

    var usedFlags: List<String> = emptyList()

    /**
     * Sets runtime flags used to configure some services behavior.
     * @param flags
     */
    fun setFlags(flags: List<String> ){
        usedFlags = usedFlags + flags
    }
}