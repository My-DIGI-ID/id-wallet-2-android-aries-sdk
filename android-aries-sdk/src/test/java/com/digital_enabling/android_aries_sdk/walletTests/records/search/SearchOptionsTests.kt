package com.digital_enabling.android_aries_sdk.walletTests.records.search

import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchItem
import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchOptions
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any

class SearchOptionsTests {

    @Test
    @DisplayName("Serialized SearchOption has keys id, type, value, tags.")
    fun serialization_works(): Unit = runBlocking {
        //Arrange
        val testRetrieveRecords = true
        val testRetrieveTotalCount = true
        val testRetrieveType = false
        val testRetrieveValue = false
        val testRetrieveTags = false
        val testObject = SearchOptions(testRetrieveRecords, testRetrieveTotalCount, testRetrieveType, testRetrieveValue, testRetrieveTags)

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("retrieveRecords"))
        Assertions.assertTrue(serializedObject.containsKey("retrieveTotalCount"))
        Assertions.assertTrue(serializedObject.containsKey("retrieveType"))
        Assertions.assertTrue(serializedObject.containsKey("retrieveValue"))
        Assertions.assertTrue(serializedObject.containsKey("retrieveTags"))
    }

    @Test
    @DisplayName("toString Method works properly with all arguments in constructor specified")
    fun toString_works(): Unit = runBlocking {
        //Arrange
        val testRetrieveRecords = true
        val testRetrieveTotalCount = true
        val testRetrieveType = false
        val testRetrieveValue = false
        val testRetrieveTags = false
        val testObject = SearchOptions(testRetrieveRecords, testRetrieveTotalCount, testRetrieveType, testRetrieveValue, testRetrieveTags)

        //Act
        val actual = testObject.toString()

        //Assert
        Assertions.assertEquals(
            "${testObject::class.simpleName}: RetrieveRecords=$testRetrieveRecords, RetrieveTotalCount=$testRetrieveTotalCount, RetrieveType=$testRetrieveType, RetrieveValue=$testRetrieveValue, RetrieveTags=$testRetrieveTags",
            actual
        )
    }
}