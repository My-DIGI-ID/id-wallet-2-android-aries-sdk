package com.digital_enabling.android_aries_sdk.walletTests.records

import com.digital_enabling.android_aries_sdk.configuration.ProvisioningRecord
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.wallet.DefaultWalletRecordService
import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchOptions
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperledger.indy.sdk.non_secrets.WalletRecord
import org.hyperledger.indy.sdk.wallet.Wallet
import org.junit.jupiter.api.*
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecordBaseTests {

    //TODO: finish tests and fix getDateTime in RecordBase

    private val walletRecordService: DefaultWalletRecordService = DefaultWalletRecordService()
    private val config: String = "{\"id\":\"" + UUID.randomUUID() + "\"}";
    private val walletCredentials: String = "{\"key\":\"test_wallet_key\"}";
    private lateinit var wallet: Wallet

    @BeforeAll
    fun setup(): Unit = runBlocking {
        Wallet.createWallet(config, walletCredentials).get()
        wallet = Wallet.openWallet(config, walletCredentials).get()
    }

    @AfterAll
    fun dispose(): Unit = runBlocking {
        wallet.closeWallet()
        Wallet.deleteWallet(config, walletCredentials)
    }

    @Test
    @DisplayName("Test with normal Tag.")
    fun test_normalTag(): Unit = runBlocking {
        //Arrange
        val testObject = ProvisioningRecord()
        testObject.setTag("testTag", "testValue")

        walletRecordService.add(wallet, testObject)

        //Act
        val expected = "testValue"
        val getRecord = walletRecordService.get<ProvisioningRecord>(
            wallet,
            "SingleRecord",
            RecordType.PROVISIONING_RECORD
        )
        val actual = getRecord!!.getTag("testTag")

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Test with ~Tag.")
    fun test_waveTag(): Unit = runBlocking {
        //Arrange
        val testObject = ProvisioningRecord()
        testObject.setTag("~testTag", "testValue")

        //Act
        val expected = "testValue"
        val actual = testObject.getTag("testTag")

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Test with null.")
    fun test_null(): Unit = runBlocking {
        //Arrange
        val testObject = ProvisioningRecord()
        testObject.setTag("testTag", "testValue")

        //Act
        val expected = null
        val actual = testObject.getTag("anotherTestTag")

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Test with exception in setTag.")
    fun test_exception(): Unit = runBlocking {
        //Arrange
        val testObject = ProvisioningRecord()

        //Act

        //Assert
        assertThrows<IllegalArgumentException> { testObject.setTag("", "testValue") }
    }

    @Test
    @DisplayName("Test with remove Tag.")
    fun test_removeTag(): Unit = runBlocking {
        //Arrange
        val testObject = ProvisioningRecord()
        testObject.setTag("testTag", "testValue")

        //Act
        val expected = null
        testObject.removeTag("testTag")

        //Assert
        Assertions.assertEquals(expected, testObject.getTag("testTag"))
    }

    @Test
    @DisplayName("Test toString.")
    fun test_toString(): Unit = runBlocking {
        //Arrange
        val testObject = ProvisioningRecord()

        //Act
        val actual = testObject.toString()

        //Assert
        Assertions.assertEquals(
            "${testObject::class.simpleName}: Id=${testObject.id}, TypeName=${testObject.typeName}, CreatedAtUtc=${testObject.createdAtUtc}, UpdatedAtUtc=${testObject.updatedAtUtc}",
            actual
        )
    }

    @Test
    @DisplayName("Test toJson.")
    fun test_toJson(): Unit = runBlocking {
        //Arrange
        val testObject = ConnectionRecord("testId")

        //Act
        val actual = testObject.toJson()

        //Assert
        Assertions.assertEquals(
            "{\"Id\":\"testId\"}",
            actual
        )
    }

    @Test
    @DisplayName("Test toBool with return true.")
    fun test_toBool_true(): Unit = runBlocking {
        //Arrange
        val testObject = ProvisioningRecord()
        testObject.setTag("testTag", "tRue")
        //Act
        val actual = testObject.getBool("testTag")

        //Assert
        Assertions.assertTrue(actual)
    }

    @Test
    @DisplayName("Test toBool with return false.")
    fun test_toBool_false(): Unit = runBlocking {
        //Arrange
        val testObject = ProvisioningRecord()
        testObject.setTag("testTag", "testValue")
        //Act
        val actual = testObject.getBool("testTag")

        //Assert
        Assertions.assertFalse(actual)
    }
}