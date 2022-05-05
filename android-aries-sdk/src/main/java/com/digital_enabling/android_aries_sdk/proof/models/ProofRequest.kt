package com.digital_enabling.android_aries_sdk.proof.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Proof request.
 */
@Serializable
class ProofRequest(
    /**
     * The name.
     */
    @SerialName("name")
    var name: String? = null,
    /**
     * The version.
     */
    @SerialName("version")
    var version: String? = null,
    /**
     * The nonce.
     */
    @SerialName("nonce")
    var nonce: String? = null,
    /**
     * The requested attributes.
     */
    @SerialName("requested_attributes")
    var requestedAttributes: HashMap<String, ProofAttributeInfo>? = HashMap(),
    /**
     * The requested predicates.
     */
    @SerialName("requested_predicates")
    var requestedPredicates: HashMap<String, ProofPredicateInfo>? = HashMap(),
    /**
     * The non revoked.
     */
    @SerialName("non_revoked")
    var nonRevoked: RevocationInterval? = null
)