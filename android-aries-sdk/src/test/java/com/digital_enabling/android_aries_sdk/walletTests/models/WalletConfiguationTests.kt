package com.digital_enabling.android_aries_sdk.walletTests.models

import com.digital_enabling.android_aries_sdk.wallet.models.WalletConfiguration
import com.digital_enabling.android_aries_sdk.wallet.models.WalletStorageConfiguration
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class WalletConfiguationTests {

    @Test
    @DisplayName("Serialized WalletConfiguration has key id, storage_type, storage_config")
    fun serialization_works() {
        //Arrange
        val testObject = WalletConfiguration(
            "testId",
            "testStorageType",
            WalletStorageConfiguration("testPath", "testUrl", "testWalletScheme", "testName")
        )

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("id"))
        Assertions.assertTrue(serializedObject.containsKey("storage_type"))
        Assertions.assertTrue(serializedObject.containsKey("storage_config"))
    }

    @Test
    @DisplayName("ToString returns correct output")
    fun toString_works(){
        //Arrange
        val testObject = WalletConfiguration(
            "testId",
            "testStorageType",
            WalletStorageConfiguration("testPath", "testUrl", "testWalletScheme", "testName")
        )

        //Act
        val expected = "WalletConfiguration: Id=testId, StorageType=testStorageType, StorageConfiguration=WalletStorageConfiguration: Path=testPath, Url=testUrl, WalletScheme=testWalletScheme, DatabaseName=testName, Tls=off, MaxConnections=null, MinIdleCount=null, ConnectionTimeout=null"
        val actual = testObject.toString()

        //Assert
        Assertions.assertEquals(expected, actual)
    }
}