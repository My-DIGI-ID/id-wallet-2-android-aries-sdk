package com.digital_enabling.android_aries_sdk.walletTests.records.search.searchExpression.subqueries

import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery.SearchExpression.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any

class NotSubqueryTests {

    @Test
    @DisplayName("test constructor with argument query: ISearchQuery.")
    fun constructor_works(): Unit = runBlocking {

        //Arrange
        val testQuery = MapExpression("testName", "testVal")
        val testObject = NotSubquery(testQuery)

        //Act
        val expectedKey = "\$not"
        val expectedVal = testQuery

        //Assert
        Assertions.assertTrue(testObject.containsKey(expectedKey))
        Assertions.assertTrue(testObject.containsValue(expectedVal))
    }
}