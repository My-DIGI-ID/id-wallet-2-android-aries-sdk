package com.digital_enabling.android_aries_sdk.walletTests.models
import com.digital_enabling.android_aries_sdk.wallet.models.WalletCredentials
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class WallteCredentailsTests {
    @Test
    @DisplayName("Serialized WalletCredentials has all keys")
    fun serialization_works() {
        //Arrange
        val testObject = WalletCredentials(
            "testKey",
            "testNewKey",
            "testkeyDerivationMethod",
            "teststorageCredentials"
        )

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("key"))
        Assertions.assertTrue(serializedObject.containsKey("rekey"))
        Assertions.assertTrue(serializedObject.containsKey("key_derivation_method"))
        Assertions.assertTrue(serializedObject.containsKey("storage_credentials"))
    }

    @Test
    @DisplayName("ToString returns correct output")
    fun toString_works() {
        //Arrange
        val testObject = WalletCredentials(
            "testKey",
            "testNewKey",
            "testkeyDerivationMethod",
            "teststorageCredentials"
        )

        //Act
        val expected = "WalletCredentials: Key=[hidden], NewKey=[hidden], KeyDerivationmethod=testkeyDerivationMethod, StorageCredentials=[hidden]"
        val actual = testObject.toString()

        //Assert
        Assertions.assertEquals(expected, actual)
    }
}