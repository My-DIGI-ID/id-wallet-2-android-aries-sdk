package com.digital_enabling.android_aries_sdk.agents.models

import kotlinx.serialization.Serializable

/**
 * An object for containing agent ownership information.
 */
@Serializable
data class AgentOwner(var name: String? = "", var imageUrl: String? = "") {
    override fun toString(): String {
        return ("${this::class.java.typeName}: Name=$name, ImageUrl=$imageUrl")
    }
}