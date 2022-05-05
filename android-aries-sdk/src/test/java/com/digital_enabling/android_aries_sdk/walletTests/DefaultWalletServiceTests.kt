package com.digital_enabling.android_aries_sdk.walletTests

import com.digital_enabling.android_aries_sdk.utils.IIndyWrapper
import com.digital_enabling.android_aries_sdk.wallet.DefaultWalletService
import com.digital_enabling.android_aries_sdk.wallet.models.WalletConfiguration
import com.digital_enabling.android_aries_sdk.wallet.models.WalletCredentials
import kotlinx.coroutines.runBlocking
import org.hyperledger.indy.sdk.wallet.Wallet
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.MockitoKotlinException
import org.mockito.kotlin.any
import kotlin.Exception

class DefaultWalletServiceTests {

    private val testWalletConfiguration = WalletConfiguration("testId","testStorageType")
    private val testWalletCredentials = WalletCredentials("testKey","testNewKey","testDerivationMethod","testStorageCredentials")
    private val mockIndy = Mockito.mock(IIndyWrapper::class.java)

    @Test
    @DisplayName("Test getWallet with path openWalletWithMutex.")
    fun test_getWallet_path_openWalletWithMutex() : Unit = runBlocking {
        //Arrange
        val mockWallet = Mockito.mock(Wallet::class.java)
        val testObject = DefaultWalletService(mockIndy)
        Mockito.`when`(
            mockIndy.openWallet(
                any(),
                any()
            )
        ).thenReturn(mockWallet)

        //Act
        val actual = testObject.getWallet(testWalletConfiguration, testWalletCredentials)

        //Assert
        Assertions.assertTrue(DefaultWalletService.wallets.containsKey(testWalletConfiguration.id))
        Assertions.assertEquals(mockWallet, DefaultWalletService.wallets.getValue(testWalletConfiguration.id))
        Assertions.assertEquals(mockWallet, actual)

    }

    @Test
    @DisplayName("Test getWallet with path getWalletFromCache try .")
    fun test_getWallet_path_getWalletFromCache() : Unit = runBlocking {
        //Arrange
        val mockWallet = Mockito.mock(Wallet::class.java)
        val testObject = DefaultWalletService(mockIndy)
        DefaultWalletService.wallets[testWalletConfiguration.id] = mockWallet

        Mockito.`when`(
            mockIndy.openWallet(
                any(),
                any()
            )
        ).thenReturn(mockWallet)

        //Act
        val actual = testObject.getWallet(testWalletConfiguration, testWalletCredentials)

        //Assert
        Assertions.assertEquals(mockWallet, actual)
    }

    @Test
    @DisplayName("Test getWallet with path getWalletFromCache catch.")
    fun test_getWallet_path_getWalletFromCache_excep() : Unit = runBlocking {
        //Arrange
        val mockWallet = Mockito.mock(Wallet::class.java)
        val testObject = DefaultWalletService(mockIndy)
        DefaultWalletService.wallets[testWalletConfiguration.id] = mockWallet

        Mockito.`when`(
            mockIndy.openWallet(
                any(),
                any()
            )
        ).thenThrow(MockitoKotlinException("",Exception()))

        //Act
        val actual = testObject.getWallet(testWalletConfiguration, testWalletCredentials)

        //Assert
        Assertions.assertEquals(mockWallet, actual)
    }

    @Test
    @DisplayName("Test deleteWallet works.")
    fun test_deleteWallet() : Unit = runBlocking {
        //Arrange
        val mockWallet = Mockito.mock(Wallet::class.java)
        val testObject = DefaultWalletService(mockIndy)
        DefaultWalletService.wallets[testWalletConfiguration.id] = mockWallet

        Mockito.`when`(
            mockIndy.openWallet(
                any(),
                any()
            )
        ).thenReturn(mockWallet)

        //Act
        val actual = testObject.deleteWallet(testWalletConfiguration, testWalletCredentials)

        //Assert
        Assertions.assertEquals(actual, Unit)
    }

    @Test
    @DisplayName("Test deleteWallet with throwing an exception.")
    fun test_deleteWallet_throwException() : Unit = runBlocking {
        //Arrange
        val mockWallet = Mockito.mock(Wallet::class.java)
        val testObject = DefaultWalletService(mockIndy)
        DefaultWalletService.wallets[testWalletConfiguration.id] = mockWallet

        Mockito.`when`(
            mockIndy.openWallet(
                any(),
                any()
            )
        ).thenReturn(mockWallet)

        Mockito.`when`(
            mockIndy.deleteWallet(
                any(),
                any()
            )
        ).thenThrow(MockitoKotlinException("", Exception()))

        //Act

        //Assert
        assertThrows<Exception> { testObject.deleteWallet(testWalletConfiguration, testWalletCredentials) }
    }

    @Test
    @DisplayName("Test createWallet.")
    fun test_createWallet() : Unit = runBlocking {
        //Arrange
        val testObject = DefaultWalletService(mockIndy)

        //Act
        val actual = testObject.createWallet(testWalletConfiguration, testWalletCredentials)

        //Assert
        Assertions.assertEquals(actual,Unit)
    }
}