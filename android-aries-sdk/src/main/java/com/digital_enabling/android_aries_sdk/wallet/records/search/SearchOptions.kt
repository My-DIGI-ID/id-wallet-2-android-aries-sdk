package com.digital_enabling.android_aries_sdk.wallet.records.search

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
/**
 * Search record options.
 */
@Serializable
data class SearchOptions(
    /**
     * The value indicating whether this [SearchOptions] retrieve records.
     * true if retrieve records; otherwise, false.
     */
    @Required
    @SerialName("retrieveRecords")
    var retrieveRecords: Boolean? = true,
    /**
     * The value indicating whether this [SearchOptions] retrieve total count.
     * true if retrieve total count; otherwise, false.
     */
    @SerialName("retrieveTotalCount")
    var retrieveTotalCount: Boolean? = null,
    /**
     * The value indicating whether this [SearchOptions] retrieve type.
     * true if retrieve type; otherwise, false.
     */
    @SerialName("retrieveType")
    var retrieveType: Boolean? = null,
    /**
     * The value indicating whether this [SearchOptions] retrieve value.
     * true if retrieve value; otherwise, false.
     */
    @Required
    @SerialName("retrieveValue")
    var retrieveValue: Boolean = true,
    /**
     * The value indicating whether this [SearchOptions] retrieve tags.
     * true if retrieve tags; otherwise, false.
     */
    @Required
    @SerialName("retrieveTags")
    var retrieveTags: Boolean = true
) {
    override fun toString(): String =
        "${this::class.simpleName}: " +
                "RetrieveRecords=${retrieveRecords}, " +
                "RetrieveTotalCount=${retrieveTotalCount}, " +
                "RetrieveType=${retrieveType}, " +
                "RetrieveValue=${retrieveValue}, " +
                "RetrieveTags=${retrieveTags}"


}