package com.digital_enabling.android_aries_sdk.credential.records

import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Schema definition record.
 */
@Serializable
class DefinitionRecord(override val id: String): RecordBase() {

    /**
     *  The type name.
     */
    override var typeName: String = RecordType.CREDENTIAL_DEFINITION_RECORD.typeName

    /**
     * true if this definition supports credential revocation; otherwise false.
     */
    var supportsRevocation: Boolean = false

    /**
     * true if [require approval]; otherwise, false
     */
    var requireApproval: Boolean = false

    /**
     * The maximum credential count.
     */
    var maxCredentialCount: Int = 0

    /**
     * true if [revocation automatic scale]; otherwise, false.
     */
    var revocationAutoScale: Boolean = false

    /**
     * The current revocation registry identifier.
     */
    var currentRevocationRegistryId: String? = null

    /**
     * The issuer did.
     */
    var issuerDid: String? = null

    /**
     * The schema identifier.
     */
    var schemaId: String? = null

    override fun toJson(): String =
        Json.encodeToString(this)
}