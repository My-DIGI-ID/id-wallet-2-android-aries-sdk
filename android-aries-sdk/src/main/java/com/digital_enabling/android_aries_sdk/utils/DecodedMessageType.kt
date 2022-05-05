package com.digital_enabling.android_aries_sdk.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DecodedMessageType(
    @SerialName("uri")
    var uri: String? = null,
    @SerialName("messageFamilyName")
    var messageFamilyName: String? = null,
    @SerialName("messageVersion")
    var messageVersion: String? = null,
    @SerialName("messageName")
    var messageName: String? = null
)