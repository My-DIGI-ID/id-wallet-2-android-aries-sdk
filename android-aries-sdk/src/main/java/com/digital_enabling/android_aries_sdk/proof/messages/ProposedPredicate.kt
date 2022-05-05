package com.digital_enabling.android_aries_sdk.proof.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The proposed predicates
 */
@Serializable
class ProposedPredicate(
    /**
     * The name of predicate.
     */
    @SerialName("name")
    var name: String? = null,
    /**
     * The schema id of predicate.
     */
    @SerialName("schema_id")
    var schemaId: String? = null,
    /**
     * The issuer did of predicate.
     */
    @SerialName("issuer_did")
    var issuerDid: String? = null,
    /**
     * The credential definition id of predicate.
     */
    @SerialName("cred_def_id")
    var credentialDefintionId: String? = null,
    /**
     * The threshold of predicate.
     */
    @SerialName("threshold")
    var threshold: String? = null,
    /**
     * The predicate operator (>, >=, &lt;, &lt;=).
     */
    @SerialName("predicate")
    var predicate: String? = null,
    /**
     * The reference of predicate.
     */
    @SerialName("referent")
    var referent: String? = null
)