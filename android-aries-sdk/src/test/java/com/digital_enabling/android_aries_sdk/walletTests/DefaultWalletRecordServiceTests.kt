package com.digital_enabling.android_aries_sdk.walletTests

import com.digital_enabling.android_aries_sdk.utils.IIndyWrapper
import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.wallet.DefaultWalletRecordService
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchOptions
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperledger.indy.sdk.wallet.Wallet
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito


class DefaultWalletRecordServiceTests {

    //TODO: finish Tests
    //TODO: add tests when there is a wallet to be used

    private val mockIndy = Mockito.mock(IIndyWrapper::class.java)
    private val mockBaseRecord = Mockito.mock(RecordBase::class.java)
    private val mockWallet = Mockito.mock(Wallet::class.java)
    private val testSearchOption = SearchOptions(false, false, true, false, false)

    @Test
    @DisplayName("test delete works with record.")
    fun test_delete_record(): Unit = runBlocking {
        //TODO: Fix test
        /*//Arrange
        val testObject = DefaultWalletRecordService(mockIndy)
        val testId = "testId"
        val testType = "testType"
        val testValue = "testValue"
        val testMap = mutableMapOf(Pair("testItem", "testValue"))
        val testRecord = SearchItem(testId, testType, testValue, testMap)
      //test
        val a = Json.encodeToString(testRecord)
        val b = Json.decodeFromString<SearchItem>(a)
        val c = b
        val d = a

        Mockito.`when`(
            mockIndy.get(
                mockWallet,
                "Af.ConnectionRecord",
                testId,
                Json.encodeToString(testSearchOption)
            )
        ).thenReturn(Json.encodeToString(testRecord))

        //Act
        val actual = testObject.get<RecordBase>(mockWallet, testId, RecordType.CONNECTION_RECORD)
        val expected = testRecord

        //Assert
        Assertions.assertEquals(expected, actual)*/
    }

    @Test
    @DisplayName("test delete with null record and return null.")
    fun test_delete_nullRecord(): Unit = runBlocking {
        //Arrange
        val testObject = DefaultWalletRecordService(mockIndy)
        val testId = "testId"
        val testRecord = null

        Mockito.`when`(
            mockIndy.get(
                mockWallet,
                "Af.ConnectionRecord",
                testId,
                Json.encodeToString(testSearchOption)
            )
        ).thenReturn(testRecord)

        //Act
        val actual = testObject.get<RecordBase>(mockWallet, testId, RecordType.CONNECTION_RECORD)
        val expected = null

        //Assert
        Assertions.assertEquals(expected, actual)
    }

}