package com.digital_enabling.android_aries_sdk.ledger.models

import kotlinx.serialization.Serializable

/**
 * Transaction Author Agreement Acceptance Model
 */
@Serializable
data class IndyTaaAcceptance (
    /**
     * The digest of the accepted text and version calculated as hex(sha256("version" + "text"))
     */
    var digest: String,
    /**
     * The taa acceptance mechanism
     */
    var acceptanceMechanism: String,
    /**
     * The acceptance timestap in Unix Epoch time format
     */
    var acceptanceDate: Long = 0,
    private val _text: String = "", // TODO: find a better way to extend a parent data class.
    private val _version: String = ""
): IndyTaa(_text, _version)
