package com.digital_enabling.android_aries_sdk.proof.models

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an individual proof identifier stored in a proof in the wallet.
 */
@Serializable
class ProofIdentifier(
    /**
     * The schema identifier.
     */
    @SerialName("schema_id")
    var schemaId : String? = null,
    /**
     * The credential definition identifier.
     */
    @SerialName("cred_def_id")
    var credentialDefintionId : String? = null,
    /**
     * The revocation registry identifier.
     */
    @SerialName("rev_reg_id")
    var revocationRegistryId  : String? = null,
    /**
     * The timestamp.
     */
    @SerialName("timestamp")
    var timestamp : String? = null
)