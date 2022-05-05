package com.digital_enabling.android_aries_sdk.credential.records

import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Schema record.
 */
@Serializable
class SchemaRecord(override val id: String) : RecordBase() {
    /**
     * The type name.
     */
    override var typeName: String = RecordType.SCHEMA_RECORD.typeName
    /**
     * The name.
     */
    var name: String? = null

    /**
     * The version.
     */
    var version: String? = null

    /**
     * The attribute names.
     */
    var attributeNames: Iterable<String?>? = null

    override fun toString() =
        "${this::class.simpleName}: " +
                "Name=${name}, " +
                "Version=${version}, " +
                "AttributeNames=${attributeNames?.joinToString(separator = ",")}, " +
                super.toString()

    override fun toJson(): String =
        Json.encodeToString(this)
}