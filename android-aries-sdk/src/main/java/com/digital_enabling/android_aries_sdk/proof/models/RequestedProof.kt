package com.digital_enabling.android_aries_sdk.proof.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a requested proof object
 */
@Serializable
class RequestedProof(
    /**
     * The revealed attributes.
     */
    @SerialName("revealed_attrs")
    var revealedAttributes: Map<String, ProofAttribute?>? = HashMap(),
    /**
     * The self attested attributes.
     */
    @SerialName("self_attested_attrs")
    var selfAttestedAttributes: Map<String, String?>? = HashMap()
)