package com.digital_enabling.android_aries_sdk.walletTests.records.search

import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchQuery
import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery.SearchExpression
import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import java.time.LocalDateTime
import java.time.ZoneOffset

class SearchQueryTests {



    @Test
    @DisplayName("test empty with no arguments")
    fun empty_works(): Unit = runBlocking {
        //Arrange
        val testObject = SearchExpression.EmptyExpression()

        //Act
        val actual = testObject.toString()
        val expected = "{\"\":\"\"}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test and with input arguments List<ISearchQuery>.")
    fun and_works(): Unit = runBlocking {

        //Arrange
        val ExpA = SearchExpression.MapExpression("testName_ExpA","testVal_ExpA")
        //val ExpA = SearchExpression.LtSubquery("testName_ExpA","testVal_ExpA")
        val ExpB = SearchExpression.MapExpression("testName_ExpB","testVal_ExpB")
        val testList = listOf<ISearchQuery>(ExpA, ExpB)
        val testObject = SearchQuery.and(testList)

        //Act
        val actual = testObject.toString()
        val expected = "{\"\$and\":[{\"testName_ExpA\":\"testVal_ExpA\"},{\"testName_ExpB\":\"testVal_ExpB\"}]}"

        //Assert
        Assertions.assertEquals(expected,actual)
    }

    @Test
    @DisplayName("test or with input arguments List<ISearchQuery>.")
    fun or_works(): Unit = runBlocking {
        //Arrange
        //val ExpA = SearchExpression.MapExpressionListISQ("testName_ExpA",listOf<ISearchQuery>())
        //val ExpB = SearchExpression.MapExpressionListISQ("testName_ExpB",listOf<ISearchQuery>())
        val ExpA = SearchExpression.MapExpression("testName_ExpA","testVal_ExpA")
        val ExpB = SearchExpression.MapExpression("testName_ExpB","testVal_ExpB")
        val testList = listOf<ISearchQuery>(ExpA, ExpB)


        val testObject = SearchQuery.or(testList)

        //Act
        val actual = testObject.toString()
        val expected = "{\"\$or\":[{\"testName_ExpA\":\"testVal_ExpA\"},{\"testName_ExpB\":\"testVal_ExpB\"}]}"

        //Assert
        Assertions.assertEquals(expected,actual)
    }

    @Test
    @DisplayName("test not with input arguments ISearchQuery.")
    fun not_works(): Unit = runBlocking {
        //Arrange
        val testName = "testName"
        val testQuery = "testQuery"
        val expA = SearchExpression.MapExpression(testName, "testVal")
        val testObject = SearchQuery.not(expA)

        //Act
        val actual = testObject.toString()
        val expected = "{\"\$not\":{\"$testName\":\"${expA.getValue(testName)}\"}}"

        //Assert
        Assertions.assertEquals(expected,actual)
    }

    @Test
    @DisplayName("test contains with input arguments name:String and value:String.")
    fun contains_works(): Unit = runBlocking {
        //Arrange
        val testName = "testName"
        val testValue = "testValue"
        val testObject = SearchQuery.contains(testName, testValue)

        //Act
        val actual = testObject.toString()
        val expected = "{\"$testName\":{\"\$like\":\"%$testValue%\"}}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test startWith with input arguments name:String and value:String.")
    fun startWith_works(): Unit = runBlocking {
        //Arrange
        val testName = "testName"
        val testValue = "testValue"
        val testObject = SearchQuery.startsWith(testName, testValue)

        //Act
        val actual = testObject.toString()
        val expected = "{\"$testName\":{\"\$like\":\"%$testValue%\"}}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test endsWith with input arguments name:String and value:String.")
    fun endsWith_works(): Unit = runBlocking {
        //Arrange
        val testName = "testName"
        val testValue = "testValue"
        val testObject = SearchQuery.endsWith(testName, testValue)

        //Act
        val actual = testObject.toString()
        val expected = "{\"$testName\":{\"\$like\":\"%$testValue%\"}}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test equal with input arguments name:String and value:String.")
    fun equal_works(): Unit = runBlocking {
        //Arrange
        val testName = "testName"
        val testValue = "testValue"
        val testObject = SearchQuery.equal(testName, testValue)

        //Act
        val actual = testObject.toString()
        val expected = "{\"$testName\":\"$testValue\"}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test notEqual with input arguments name:String and value: String.")
    fun notEqual_works(): Unit = runBlocking {
        //Arrange
        val testName = "testName"
        val testValue = "testValue1"
        val testObject = SearchQuery.notEqual(testName, testValue)

        //Act
        val actual = testObject.toString()
        val expected = "{\"$testName\":{\"\$neq\":\"$testValue\"}}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test notEqual with input arguments name:String and values: LocalDateTime.")
    fun notEqual_DateTime_works(): Unit = runBlocking {
        //Arrange
        val testName = "testName"
        val testValue = LocalDateTime.now()
        val testObject = SearchQuery.notEqual(testName, testValue)

        //Act
        val actual = testObject.toString()
        val expected = "{\"$testName\":{\"\$neq\":\"${testValue.toEpochSecond(ZoneOffset.UTC)}\"}}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test inSubquery with input arguments name:String and values: List<String>.")
    fun inSubquery_works(): Unit = runBlocking {
        //Arrange
        val testName = "testName"
        val testValues = listOf("testValue1", "testValue2", "testValue3")
        val testObject = SearchQuery.inSubquery(testName, testValues)

        //Act
        val actual = testObject.toString()
        val expected = "{\"$testName\":{\"\$in\":[\"${testValues[0]}\",\"${testValues[1]}\",\"${testValues[2]}\"]}}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test less with input arguments name:String and value: String.")
    fun less_works(): Unit = runBlocking {
        //Arrange
        val testName = "testName"
        val testValue = "testValue1"
        val testObject = SearchQuery.less(testName, testValue)

        //Act
        val actual = testObject.toString()
        val expected = "{\"~$testName\":{\"\$lt\":\"$testValue\"}}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test less with input arguments name:String and values: LocalDateTime.")
    fun less_DateTime_works(): Unit = runBlocking {
        //Arrange
        val testName = "testName"
        val testValue = LocalDateTime.now()
        val testObject = SearchQuery.less(testName, testValue)

        //Act
        val actual = testObject.toString()
        val expected = "{\"~$testName\":{\"\$lt\":\"${testValue.toEpochSecond(ZoneOffset.UTC)}\"}}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test lessOrEqual with input arguments name:String and value: String.")
    fun lessOrEqual_works(): Unit = runBlocking {
        //Arrange
        val testName = "testName"
        val testValue = "testValue1"
        val testObject = SearchQuery.lessOrEqual(testName, testValue)

        //Act
        val actual = testObject.toString()
        val expected = "{\"~$testName\":{\"\$lte\":\"$testValue\"}}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test lessOrEqual with input arguments name:String and values: LocalDateTime.")
    fun lessOrEqual_DateTime_works(): Unit = runBlocking {
        //Arrange
        val testName = "testName"
        val testValue = LocalDateTime.now()
        val testObject = SearchQuery.lessOrEqual(testName, testValue)

        //Act
        val actual = testObject.toString()
        val expected = "{\"~$testName\":{\"\$lte\":\"${testValue.toEpochSecond(ZoneOffset.UTC)}\"}}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test greater with input arguments name:String and value: String.")
    fun greater_works(): Unit = runBlocking {
        //Arrange
        val testName = "testName"
        val testValue = "testValue1"
        val testObject = SearchQuery.greater(testName, testValue)

        //Act
        val actual = testObject.toString()
        val expected = "{\"~$testName\":{\"\$gt\":\"$testValue\"}}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test greater with input arguments name:String and values: LocalDateTime.")
    fun greater_DateTime_works(): Unit = runBlocking {
        //Arrange
        val testName = "testName"
        val testValue = LocalDateTime.now()
        val testObject = SearchQuery.greater(testName, testValue)

        //Act
        val actual = testObject.toString()
        val expected = "{\"~$testName\":{\"\$gt\":\"${testValue.toEpochSecond(ZoneOffset.UTC)}\"}}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test greaterOrEqual with input arguments name:String and value: String.")
    fun greaterOrEqual_works(): Unit = runBlocking {
        //Arrange
        val testName = "testName"
        val testValue = "testValue1"
        val testObject = SearchQuery.greaterOrEqual(testName, testValue)

        //Act
        val actual = testObject.toString()
        val expected = "{\"~$testName\":{\"\$gte\":\"$testValue\"}}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test greaterOrEqual with input arguments name:String and values: LocalDateTime.")
    fun greaterOrEqual_DateTime_works(): Unit = runBlocking {
        //Arrange
        val testName = "testName"
        val testValue = LocalDateTime.now()
        val testObject = SearchQuery.greaterOrEqual(testName, testValue)

        //Act
        val actual = testObject.toString()
        val expected = "{\"~$testName\":{\"\$gte\":\"${testValue.toEpochSecond(ZoneOffset.UTC)}\"}}"

        //Assert
        Assertions.assertEquals(expected, actual)
    }
}