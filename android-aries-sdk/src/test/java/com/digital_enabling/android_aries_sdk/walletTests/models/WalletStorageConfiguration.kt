package com.digital_enabling.android_aries_sdk.walletTests.models
import com.digital_enabling.android_aries_sdk.wallet.models.WalletStorageConfiguration
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class WalletStorageConfiguration {
    @Test
    @DisplayName("Serialized WalletCredentials has all keys, when all parameters a given.")
    fun serialization_works() {
        //Arrange
        val testObject = WalletStorageConfiguration(
            "testPath",
            "testUrl",
            "testWalletScheme",
            "testDatabase",
            "testTls",
            1,
            1,
            1
        )

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("path"))
        Assertions.assertTrue(serializedObject.containsKey("url"))
        Assertions.assertTrue(serializedObject.containsKey("wallet_scheme"))
        Assertions.assertTrue(serializedObject.containsKey("database_name"))
        Assertions.assertTrue(serializedObject.containsKey("tls"))
        Assertions.assertTrue(serializedObject.containsKey("max_connections"))
        Assertions.assertTrue(serializedObject.containsKey("min_idle_count"))
        Assertions.assertTrue(serializedObject.containsKey("connection_timeout"))
    }

    @Test
    @DisplayName("ToString returns correct output with default values")
    fun toString_works_default_values() {
        //Arrange
        val testObject = WalletStorageConfiguration(
            "testPath",
            "testUrl",
            "testWalletScheme",
            "testDatabase"
        )

        //Act
        val expected = "WalletStorageConfiguration: Path=testPath, Url=testUrl, WalletScheme=testWalletScheme, DatabaseName=testDatabase, Tls=off, MaxConnections=null, MinIdleCount=null, ConnectionTimeout=null"
        val actual = testObject.toString()

        //Assert
        Assertions.assertEquals(expected, actual)
    }
    @Test
    @DisplayName("ToString returns correct output with default values")
    fun toString_works() {
        //Arrange
        val testObject = WalletStorageConfiguration(
            "testPath",
            "testUrl",
            "testWalletScheme",
            "testDatabase",
            "testTls",
            1,
            1,
            1
        )

        //Act
        val expected = "WalletStorageConfiguration: Path=testPath, Url=testUrl, WalletScheme=testWalletScheme, DatabaseName=testDatabase, Tls=testTls, MaxConnections=1, MinIdleCount=1, ConnectionTimeout=1"
        val actual = testObject.toString()

        //Assert
        Assertions.assertEquals(expected, actual)
    }
}