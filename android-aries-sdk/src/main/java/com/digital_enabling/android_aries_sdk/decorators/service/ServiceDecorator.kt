package com.digital_enabling.android_aries_sdk.decorators.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The Service Decorator
 * Based on specification Aries RFC 0056: Service Decorator
 * <a href="https://github.com/hyperledger/aries-rfcs/tree/master/features/0056-service-decorator">Specification Service Decorator</a>
 */
@Serializable
class ServiceDecorator {

    /**
     * Recipient Keys
     */
    @SerialName("recipientKeys")
    var recipientKeys: List<String>? = null

    /**
     * Routing Keys
     */
    @SerialName("routingKeys")
    var routingKeys: List<String>? = null

    /**
     * Service endpoint URL
     */
    @SerialName("serviceEndpoint")
    var serviceEndpoint: String? = null
}

