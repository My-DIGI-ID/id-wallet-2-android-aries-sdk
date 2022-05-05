package com.digital_enabling.android_aries_sdk.didexchange.models.dids

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Strongly type DID doc model.
 */
@Serializable
data class DidDoc(
    /**
     * The DID doc context.
     */
    @Required
    @SerialName("@context")
    var context: String = "https://w3did.org/did/v1",
    /**
     * The ID of the DID doc
     */
    @SerialName("id")
    var id: String,
    /**
     * List of public keys available on the DID doc.
     */
    @SerialName("publicKey")
    var keys: List<DidDocKey>,
    /**
     * List of services available on the did doc.
     */
    @SerialName("service")
    var services: List<IndyAgentDidDocService>
)