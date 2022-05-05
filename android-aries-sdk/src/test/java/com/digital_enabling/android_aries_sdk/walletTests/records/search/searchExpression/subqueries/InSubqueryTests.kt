package com.digital_enabling.android_aries_sdk.walletTests.records.search.searchExpression.subqueries

import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery.SearchExpression.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InSubqueryTests {

    @Test
    @DisplayName("test constructor with arguments name: String, value: List<String>.")
    fun constructor_works(): Unit = runBlocking {
        //Arrange
        val testList = listOf<String>("testObj1", "testObj2", "testObj3")
        val testObject = InSubquery("testName", testList)

        //Act
        val expectedKey = "testName"
        val expectedKeyInSubquery = "\$in"
        val expectedValInSubquery = testList

        //Assert
        Assertions.assertTrue(testObject.containsKey(expectedKey))
        Assertions.assertTrue(testObject.getValue(expectedKey).containsKey(expectedKeyInSubquery))
        Assertions.assertTrue(testObject.getValue(expectedKey).containsValue(expectedValInSubquery))
    }

    @Test
    @DisplayName("test constructor with null value argument for throwing an exception ")
    fun constructor_throwException_null(): Unit = runBlocking {
        //Arrange
        val testList = null

        //Act

        //Assert
        assertThrows<Exception> { InSubquery("testName", testList) }
    }

    @Test
    @DisplayName("test constructor with empty value argument for throwing an exception ")
    fun constructor_throwException(): Unit = runBlocking {
        //Arrange
        val testList = listOf<String>()

        //Act

        //Assert
        assertThrows<Exception> { InSubquery("testName", testList) }
    }
}