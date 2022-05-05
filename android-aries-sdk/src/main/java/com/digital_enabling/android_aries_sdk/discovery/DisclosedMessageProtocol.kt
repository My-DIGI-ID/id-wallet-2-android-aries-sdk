package com.digital_enabling.android_aries_sdk.discovery

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a disclosed message protocol object.
 */
@Serializable
data class DisclosedMessageProtocol(
    /**
     * Protocol Identifier.
     */
    @SerialName("pid")
    var protocolId: String,
    /**
     * Roles for the subject protocol.
     */
    @SerialName("roles")
    var roles: String = ""
)
