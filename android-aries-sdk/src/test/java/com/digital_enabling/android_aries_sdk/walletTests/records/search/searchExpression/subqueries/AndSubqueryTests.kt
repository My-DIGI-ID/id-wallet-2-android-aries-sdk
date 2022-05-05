package com.digital_enabling.android_aries_sdk.walletTests.records.search.searchExpression.subqueries

import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery.SearchExpression.*
import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any

class AndSubqueryTests {

    @Test
    @DisplayName("test constructor with argument Collection: List<ISearchQuery> with 2 ISearchQueries.")
    fun constructor_works(): Unit = runBlocking {
        //Arrange
        val testList = listOf<ISearchQuery>(any(), any())
        val testObject = AndSubquery(testList)

        //Act
        val expectedKey = "\$and"

        //Assert
        Assertions.assertTrue(testObject.containsKey(expectedKey))
        Assertions.assertTrue(testObject.getValue(expectedKey).containsAll(testList))
    }

    @Test
    @DisplayName("test constructor with argument Collection: List<ISearchQuery> with only 1 ISearchQueries. Expect throwing an exception.")
    fun constructor_throwException(): Unit = runBlocking {
        //Arrange
        val testList = listOf<ISearchQuery>(any())

        //Act

        //Assert
        assertThrows<Exception> { AndSubquery(testList) }
    }

    @Test
    @DisplayName("test constructor with empty value argument for throwing an exception ")
    fun constructor_throwException_empty(): Unit = runBlocking {
        //Arrange
        val testList = listOf<ISearchQuery>()

        //Act

        //Assert
        assertThrows<Exception> { AndSubquery(testList) }
    }
}