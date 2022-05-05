package com.digital_enabling.android_aries_sdk.didexchange.models.dids

import kotlinx.serialization.SerialName

/**
 * DID doc service interface.
 */
interface IDidDocServiceEndpoint {
    /**
     * Id of the service.
     */
    @SerialName("id")
    var id: String

    /**
     * Type of the service.
     */
    @SerialName("type")
    var type: String

    /**
     * Endpoint of the service.
     */
    @SerialName("serviceEndpoint")
    var serviceEndpoint: String
}