package com.digital_enabling.android_aries_sdk.decorators

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ServiceDecorator(
    @SerialName("recipientKeys")
    var recipientKeys: MutableList<String>? = mutableListOf(),
    @SerialName("routingKeys")
    var routingKeys: MutableList<String>? = mutableListOf(),
    @SerialName("serviceEndpoint")
    var serviceEndpoint: String? = null
)