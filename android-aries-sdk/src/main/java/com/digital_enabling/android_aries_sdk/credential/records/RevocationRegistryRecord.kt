package com.digital_enabling.android_aries_sdk.credential.records

import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 *  Represents revocation registry record.
 */
@Serializable
class RevocationRegistryRecord(override val id: String): RecordBase() {
    /**
     * The type name.
     */
    override var typeName: String = RecordType.REVOCATION_REGISTRY_RECORD.typeName

    /**
     * The revocation registry identifier.
     */
    var credentialDefinitionId: String? = null

    /**
     * The tails file.
     */
    var tailsFile: String? = null

    /**
     * The tails location.
     */
    var tailsLocation: String? = null

    override fun toJson(): String =
        Json.encodeToString(this)
}