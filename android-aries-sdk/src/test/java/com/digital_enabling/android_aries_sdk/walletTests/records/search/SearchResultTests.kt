package com.digital_enabling.android_aries_sdk.walletTests.records.search

import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchItem
import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchOptions
import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchResult
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SearchResultTests {

    @Test
    @DisplayName("Serialized SearchOption has key records.")
    fun serialization_works(): Unit = runBlocking {
        //Arrange
        val testRecords = listOf<SearchItem>()
        val testObject = SearchResult(testRecords)

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("records"))
    }
}