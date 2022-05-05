package com.digital_enabling.android_aries_sdk.ledger.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Authorization constraint
 */
@Serializable
data class AuthorizationConstraint (
    /**
     * The role.
     */
    @SerialName("role")
    var role: String,
    /**
     * true if must be owner; otherwise, false.
     */
    @SerialName("need_to_be_owner")
    var mustBeOwner: Boolean = false,
    /**
     * The signature count.
     */
    @SerialName("sig_count")
    var signatureCount: Int = 0,
    /**
     * The metadata.
     */
    @SerialName("metadata")
    var metadata: ConstraintMetadata,
    /**
     * The constraint identifier.
     */
    @SerialName("constraint_id")
    var constraintId: String,
    /**
     * The constraints.
     */
    @SerialName("auth_constraints")
    var constraints: List<AuthorizationConstraint>
)