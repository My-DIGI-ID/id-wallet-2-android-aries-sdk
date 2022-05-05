package com.digital_enabling.android_aries_sdk.proof.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Proof request configuration.
 */
@Serializable
class ProofRequestConfiguration(
    /**
     * The requested attributes.
     */
    @SerialName("requested_attributes")
    var requestedAttributes: Map<String, ProofAttributeInfo?>? = HashMap(),
    /**
     * The requested predicates.
     */
    @SerialName("requested_predicates")
    var requestedPredicates: Map<String, ProofPredicateInfo?>? = HashMap(),
    /**
     * The non revoked.
     */
    @SerialName("non_revoked")
    var nonRevoked: RevocationInterval? = null
) {
    override fun toString(): String {
        var result = "${this::class.java.simpleName}: RequestedAttributes="
        val attributesList = requestedAttributes?.toList()?.map { "[${it.first}, ${it.second}]" }
            ?: HashMap<String, ProofAttributeInfo>().toList().map { "[${it.first}, ${it.second}]" }
        val predicatesList = requestedPredicates?.toList()?.map { "[${it.first}, ${it.second}]" }
            ?: HashMap<String, ProofPredicateInfo>().toList().map { "[${it.first}, ${it.second}]" }
        attributesList.forEach { x -> result += x }
        result += ", RequestedPredicates="
        predicatesList.forEach { x -> result += x }
        result += ", NonRevoked=$nonRevoked"
        return result
    }
}