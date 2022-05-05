package com.digital_enabling.android_aries_sdk.didexchange.models.dids

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Indy Agent Did Doc Service.
 */
@Serializable
data class IndyAgentDidDocService(
    /**
     * @see IDidDocServiceEndpoint
     */
    @SerialName("id")
    override var id: String,
    /**
     * @see IDidDocServiceEndpoint
     */
    @Required
    @SerialName("type")
    override var type: String = DidDocServiceEndpointTypes.INDY_AGENT,
    /**
     * Array of recipient key references.
     */
    @SerialName("recipientKeys")
    var recipientKeys: List<String>,
    /**
     * Array or routing key references.
     */
    @SerialName("routingKeys")
    var routingKeys: List<String>,
    /**
     * Service endpoint.
     */
    @SerialName("serviceEndpoint")
    override var serviceEndpoint: String
) : IDidDocServiceEndpoint