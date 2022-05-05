package com.digital_enabling.android_aries_sdk.proof.models

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Attribute filter
 */
@Serializable
class AttributeFilter(
    /**
     * The schema identifier.
     */
    @SerialName("schema_id")
    var schemaId: String? = null,
    /**
     * The schema issuer did.
     */
    @SerialName("schema_issuer_did")
    var schemaIssuerDid: String? = null,
    /**
     * The schema name.
     */
    @SerialName("schema_name")
    var schemaName: String? = null,
    /**
     * The schema version.
     */
    @SerialName("schema_version")
    var schemaVersion: String? = null,
    /**
     * The issuer did.
     */
    @SerialName("issuer_did")
    var issuerDid: String? = null,
    /**
     * The credential definition identifier.
     */
    @SerialName("cred_def_id")
    var credentialDefintionId: String? = null,
    /**
     * The attribute name and value to add as restriction.
     */
    @Required
    @SerialName("attributeValue")
    var attributeValue: AttributeValue? = null
)