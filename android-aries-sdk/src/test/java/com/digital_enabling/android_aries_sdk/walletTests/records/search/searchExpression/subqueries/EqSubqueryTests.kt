package com.digital_enabling.android_aries_sdk.walletTests.records.search.searchExpression.subqueries

import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery.SearchExpression.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class EqSubqueryTests {

    @Test
    @DisplayName("test constructor with arguments name: String, value: String.")
    fun constructor_works(): Unit = runBlocking {
        //Arrange
        val testObject = EqSubquery("testName", "testVal")

        //Act
        val expectedKey = "testName"
        val expectedVal = "testVal"

        //Assert
        Assertions.assertTrue(testObject.containsKey(expectedKey))
        Assertions.assertTrue(testObject.containsValue(expectedVal))
    }
}