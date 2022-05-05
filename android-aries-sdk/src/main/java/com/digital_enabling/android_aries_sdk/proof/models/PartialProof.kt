package com.digital_enabling.android_aries_sdk.proof.models

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a partial proof stored in the wallet.
 */
@Serializable
data class PartialProof(
    /**
     * The proof identifiers.
     */
    @SerialName("identifiers")
    var identifiers: MutableList<ProofIdentifier>? = mutableListOf(),
    /**
     * The requested proof.
     */
    @Required
    @SerialName("requested_proof")
    var requestedProof: RequestedProof
)