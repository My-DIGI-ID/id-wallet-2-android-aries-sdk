package com.digital_enabling.android_aries_sdk.proof.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *  The proposed attributes
 */
@Serializable
class ProposedAttribute(
    /**
     * The name of attribute.
     */
    @SerialName("name")
    var name: String? = null,
    /**
     * The schema id of attribute.
     */
    @SerialName("schema_id")
    var schemaId: String? = null,
    /**
     * The issuer did of attribute.
     */
    @SerialName("issuer_did")
    var issuerDid: String? = null,
    /**
     * The credential definition of attribute.
     */
    @SerialName("cred_def_id")
    var credentialDefintionId: String? = null,
    /**
     * The mime-type.
     */
    @SerialName("mime-type")
    var mimeType: String? = null,
    /**
     * The attribute value.
     */
    @SerialName("value")
    var value: String? = null,
    /**
     * The reference.
     */
    @SerialName("referent")
    var referent: String? = null
)
