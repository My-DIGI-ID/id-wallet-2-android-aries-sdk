package com.digital_enabling.android_aries_sdk.proof.models

/**
 * Enumeration of possible proof states
 */
enum class ProofState(index: Int) {
    PROPOSED(0),
    REQUESTED(1),
    ACCEPTED(2),
    REJECTED(3)
}