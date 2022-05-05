package com.digital_enabling.android_aries_sdk.wallet.records.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Search record result.
 */
@Serializable
data class SearchResult(
    /**
     * The resulting records.
     */
    @SerialName("records")
    var records: List<SearchItem>
)
