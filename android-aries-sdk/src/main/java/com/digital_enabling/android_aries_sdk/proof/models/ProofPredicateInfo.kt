package com.digital_enabling.android_aries_sdk.proof.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ProofPredicateInfo(
    /**
     * The type of the predicate.
     */
    @SerialName("p_type")
    var predicateType: String? = null,
    /**
     * The predicate value
     */
    @SerialName("p_value")
    var predicateValue: Int? = null
) : ProofAttributeInfo() {
    override fun toString(): String {
        return "${this::class.java.simpleName}: PredicateType=$predicateType, PredicateValue=$predicateValue"
    }
}