package com.digital_enabling.android_aries_sdk.proof.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ProofRequestParameters(
    /**
     * The name.
     */
    @SerialName("name")
    var name: String? = null,
    /**
     * The version.
     */
    @SerialName("version")
    var version: String? = null,
    /**
     * The non revoked.
     */
    @SerialName("non_revoked")
    var nonRevoked: RevocationInterval? = null
)