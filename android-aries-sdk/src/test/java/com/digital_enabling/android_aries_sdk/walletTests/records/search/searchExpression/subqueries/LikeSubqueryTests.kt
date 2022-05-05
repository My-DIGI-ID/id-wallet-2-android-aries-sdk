package com.digital_enabling.android_aries_sdk.walletTests.records.search.searchExpression.subqueries

import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery.SearchExpression.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class LikeSubqueryTests {

    @Test
    @DisplayName("test constructor with arguments name: String, value: String.")
    fun constructor_works(): Unit = runBlocking {
        //Arrange
        val testVal = "testVal"
        val testName = "testName"
        val testObject = LikeSubquery(testName, testVal)

        //Act
        val expectedKey = testName
        val expectedKeyLikeSubquery = "\$like"
        val expectedValLikeSubquery = testVal

        //Assert
        Assertions.assertTrue(testObject.containsKey(expectedKey))
        Assertions.assertTrue(testObject.getValue(expectedKey).containsKey(expectedKeyLikeSubquery))
        Assertions.assertTrue(testObject.getValue(expectedKey).containsValue(expectedValLikeSubquery))
    }
}