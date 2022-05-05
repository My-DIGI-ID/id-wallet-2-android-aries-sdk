package com.digital_enabling.android_aries_sdk.proof.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Proof attribute info.
 */
@Serializable
open class ProofAttributeInfo {
    /**
     * The name.
     * You can only specify value for 'name' or 'names', but not both.
     */
    @SerialName("name")
    var name: String? = null
        set(value) {
            if (value != null) {
                names = null
            }
            field = value
        }

    /**
     * Collection of names.
     * You can only specify value for 'name' or 'names', but not both.
     */
    @SerialName("names")
    var names: Array<String>? = null
        set(value) {
            if (value != null && value.any()) {
                name = null
            }
            field = value
        }

    /**
     * The restrictions.
     */
    @SerialName("restrictions")
    var restrictions: MutableList<AttributeFilter>? = mutableListOf()

    /**
     * The non revoked.
     */
    @SerialName("non_revoked")
    var nonRevoked: RevocationInterval? = null
}