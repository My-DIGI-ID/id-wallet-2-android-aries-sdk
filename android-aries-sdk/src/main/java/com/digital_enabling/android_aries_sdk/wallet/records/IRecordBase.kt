package com.digital_enabling.android_aries_sdk.wallet.records

import java.time.LocalDateTime

interface IRecordBase {
    /**
     * The identifier.
     */
    val id: String
    /**
     * The created datetime of the record.
     */
    var createdAtUtc: LocalDateTime?
    /**
     * The last updated datetime of the record.
     */
    var updatedAtUtc: LocalDateTime?
    /**
     * The type name.
     */
    var typeName: String
    /**
     * The tags.
     */
    var tags: MutableMap<String, String>
}