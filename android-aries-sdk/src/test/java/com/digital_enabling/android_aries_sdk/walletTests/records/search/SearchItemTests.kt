package com.digital_enabling.android_aries_sdk.walletTests.records.search

import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchItem
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SearchItemTests {

    @Test
    @DisplayName("Serialized SearchItem has keys id, type, value, tags.")
    fun serialization_works(): Unit = runBlocking {
        //Arrange
        val testId = "testId"
        val testType = "testType"
        val testValue = "testValue"
        val testMap = mutableMapOf(Pair("testItem", "testValue"))
        val testObject = SearchItem(testId, testType, testValue, testMap)

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("id"))
        Assertions.assertTrue(serializedObject.containsKey("type"))
        Assertions.assertTrue(serializedObject.containsKey("value"))
        Assertions.assertTrue(serializedObject.containsKey("tags"))
    }

    @Test
    @DisplayName("toString Method works properly with all arguments in constructor specified")
    fun toString_works(): Unit = runBlocking {
        //Arrange
        val testId = "testId"
        val testType = "testType"
        val testValue = "testValue"
        val testMap = mutableMapOf(Pair("testItem", "testValue"))
        val testObject = SearchItem(testId, testType, testValue, testMap)

        //Act
        val actual = testObject.toString()

        //Assert
        Assertions.assertEquals(
            "${testObject::class.simpleName}: Id=$testId, Type=$testType, Value=$testValue, Tags=${testObject.tags}.toString()",
            actual
        )
    }

    @Test
    @DisplayName("toString Method works properly when tags argument in constructor has an emptyMap")
    fun toString_emptyMap_works(): Unit = runBlocking {
        //Arrange
        val testId = "testId"
        val testType = "testType"
        val testValue = "testValue"
        val testObject = SearchItem(testId, testType, testValue)//,testMap)

        //Act
        val actual = testObject.toString()

        //Assert
        Assertions.assertEquals(
            "${testObject::class.simpleName}: Id=$testId, Type=$testType, Value=$testValue, Tags=empty",
            actual
        )
    }
}