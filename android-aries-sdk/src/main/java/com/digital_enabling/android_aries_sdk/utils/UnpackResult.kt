package com.digital_enabling.android_aries_sdk.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnpackResult(
    @SerialName("message")
    val message: String? = null,
    @SerialName("sender_verkey")
    val senderVerkey: String? = null,
    @SerialName("recipient_verkey")
    val recipientVerkey: String? = null
)