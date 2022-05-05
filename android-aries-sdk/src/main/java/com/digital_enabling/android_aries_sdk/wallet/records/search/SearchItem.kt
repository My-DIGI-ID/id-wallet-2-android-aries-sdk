package com.digital_enabling.android_aries_sdk.wallet.records.search

import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * Search record item.
 */
@Serializable
data class SearchItem(
    /**
     * The record identifier
     */
    @SerialName("id")
    var id: String? = null,
    /**
     * The record type
     */
    @SerialName("type")
    /**
     * The record value.
     */
    var type: String? = null,
    @SerialName("value")
    /**
     * The record tags.
     */
    var value: String? = null,
    @SerialName("tags")
    var tags: Map<String, String>? = mutableMapOf()
) {
    override fun toString(): String =
        "${this::class.simpleName}: " +
                "Id=$id, " +
                "Type=$type, " +
                "Value=$value, " +
                "Tags=${if (tags?.isEmpty() == true) "empty" else "$tags.toString()"}"

}