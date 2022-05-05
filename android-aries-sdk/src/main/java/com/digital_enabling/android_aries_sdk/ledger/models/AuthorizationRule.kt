package com.digital_enabling.android_aries_sdk.ledger.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Authorization rule
 */
@Serializable
data class AuthorizationRule (
    /**
     * The type of the transaction.
     */
    @SerialName("auth_type")
    var transactionType: String,
    /**
     *  The new value.
     */
    @SerialName("new_value")
    var newValue: String,
    /**
     * The old value.
     */
    @SerialName("old_value")
    var oldValue: String,
    /**
     * The field.
     */
    @SerialName("field")
    var field: String,
    /**
     * The action.
     */
    @SerialName("auth_action")
    var action: String,
    /**
     * The constraint.
     */
    @SerialName("constraint")
    var constraint: AuthorizationConstraint
)