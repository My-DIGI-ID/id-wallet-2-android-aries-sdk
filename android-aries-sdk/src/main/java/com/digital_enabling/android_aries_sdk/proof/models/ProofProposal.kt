package com.digital_enabling.android_aries_sdk.proof.models

import com.digital_enabling.android_aries_sdk.proof.messages.ProposedAttribute
import com.digital_enabling.android_aries_sdk.proof.messages.ProposedPredicate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Proof proposal
 */
@Serializable
class ProofProposal(
    /**
     * The comment.
     */
    @SerialName("comment")
    var comment: String? = null,
    /**
     * The proposed attributes.
     */
    @SerialName("attributes")
    var proposedAttributes: MutableList<ProposedAttribute>? = mutableListOf(),
    /**
     * The proposed predicates.
     */
    @SerialName("predicates")
    var proposedPredicates: MutableList<ProposedPredicate>? = mutableListOf()
)