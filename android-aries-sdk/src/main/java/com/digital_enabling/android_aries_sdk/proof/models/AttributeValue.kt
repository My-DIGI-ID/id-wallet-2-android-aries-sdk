package com.digital_enabling.android_aries_sdk.proof.models

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

/**
 * Attribute Value as restriction
 */
@Serializable
class AttributeValue(
    /**
     * The name.
     */
    var name: String,
    /**
     * The value.
     */
    @Required
    var value: String? = null
)