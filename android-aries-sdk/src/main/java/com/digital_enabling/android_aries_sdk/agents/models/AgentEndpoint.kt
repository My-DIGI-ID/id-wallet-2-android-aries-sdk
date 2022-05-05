package com.digital_enabling.android_aries_sdk.agents.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An object for containing agent endpoint information.
 */
@Serializable
class AgentEndpoint() {
    /**
     * The did of the agent.
     */
    @SerialName("did")
    var did: String? = null

    /**
     * The verkey of the agent.
     */
    @SerialName("verkey")
    var verkey: Array<String>? = null

    /**
     * The uri of the agent.
     */
    @SerialName("uri")
    var uri: String? = null

    /**
     * Initializes a new instance of the AgentEndpoint class.
     */
    constructor(copy: AgentEndpoint) : this() {
        did = copy.did
        uri = copy.uri
        verkey = copy.verkey
    }

    override fun toString(): String {
        val output = "${this::class.java.simpleName}: Did=$did, Verkey=${
            if ((verkey?.count() ?: 0) > 0) {
                "[hidden]"
            } else {
                "null"
            }
        }, Uri=$uri"

        return output
    }
}