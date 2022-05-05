package com.digital_enabling.android_aries_sdk.walletTests.records.search.searchExpression.subqueries


import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery.SearchExpression.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class GteSubqueryTests {

    @Test
    @DisplayName("test constructor with arguments name: String, value: String.")
    fun constructor_works(): Unit = runBlocking {
        //Arrange
        val testObject = GteSubquery("testName", "testVal")

        //Act
        val expectedKey = "~testName"
        val expectedKeyGteSubquery = "\$gte"
        val expectedValGteSubquery = "testVal"

        //Assert
        Assertions.assertTrue(testObject.containsKey(expectedKey))
        Assertions.assertTrue(testObject.getValue(expectedKey).containsKey(expectedKeyGteSubquery))
        Assertions.assertTrue(testObject.getValue(expectedKey).containsValue(expectedValGteSubquery))
    }
}