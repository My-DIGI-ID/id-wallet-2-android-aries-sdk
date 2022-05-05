package com.digital_enabling.android_aries_sdk.proof.models

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Proof attribute
 */
@Serializable
class ProofAttribute(
    /**
     * The sub proof index.
     */
    @Required
    @SerialName("sub_proof_index")
    var subProofIndex: Int? = null,
    /**
     * The raw value of the attribute.
     */
    @SerialName("raw")
    var raw: String? = null,
    /**
     * The encoded value of the attribute.
     */
    @SerialName("encoded")
    var encoded: String? = null
)