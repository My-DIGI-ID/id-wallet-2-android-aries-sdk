package com.digital_enabling.android_aries_sdk.credential.records

/**
 * Enumeration of possible triggers that change the credentials state
 */
enum class CredentialTrigger {
    REQUEST,
    ISSUE,
    REJECT,
    REVOKE
}