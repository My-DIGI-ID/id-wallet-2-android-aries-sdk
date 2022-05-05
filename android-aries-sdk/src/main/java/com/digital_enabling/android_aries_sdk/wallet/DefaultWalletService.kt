package com.digital_enabling.android_aries_sdk.wallet

import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.utils.IIndyWrapper
import com.digital_enabling.android_aries_sdk.utils.IndyWrapper
import com.digital_enabling.android_aries_sdk.wallet.models.WalletConfiguration
import com.digital_enabling.android_aries_sdk.wallet.models.WalletCredentials
import kotlinx.coroutines.sync.Semaphore
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperledger.indy.sdk.wallet.Wallet

/**
 * @see IWalletService
 */
open class DefaultWalletService(val indyWrapper: IIndyWrapper = IndyWrapper()) : IWalletService {

    companion object {

        /**
         * Map of open wallets
         */
        @Volatile
        var wallets: MutableMap<String, Wallet> = mutableMapOf()

        /**
         * Mutex semaphore for opening a new (not cached) wallet
         */
        private val openWalletSemaphore = Semaphore(1, 0)
    }

    /**
     * @see IWalletService
     */
    override suspend fun getWallet(
        walletConfiguration: WalletConfiguration,
        walletCredentials: WalletCredentials
    ): Wallet {
        var wallet = getWalletFromCache(walletConfiguration, walletCredentials)

        if (wallet == null) {
            wallet = openWalletWithMutex(walletConfiguration, walletCredentials)
        }
        return wallet
    }

    private suspend fun openWalletWithMutex(
        walletConfiguration: WalletConfiguration,
        walletCredentials: WalletCredentials
    ): Wallet {
        openWalletSemaphore.acquire()
        try {
            var wallet = getWalletFromCache(walletConfiguration, walletCredentials)

            if (wallet != null) {
                return wallet
            } else {
                wallet = indyWrapper.openWallet(
                    Json.encodeToString(walletConfiguration),
                    Json.encodeToString(walletCredentials)
                )

                if (wallet == null) {
                    throw AriesFrameworkException(ErrorCode.valueOf("Could not open wallet."))
                }
                wallets[walletConfiguration.id] = wallet
                return wallet
            }
        } finally {
            openWalletSemaphore.release()
        }
    }

    private fun getWalletFromCache(
        walletConfiguration: WalletConfiguration,
        walletCredentials: WalletCredentials
    ): Wallet? {
        if (wallets.containsKey(walletConfiguration.id)) {
            return try {
                indyWrapper.openWallet(
                    Json.encodeToString(walletConfiguration),
                    Json.encodeToString(walletCredentials)
                )
            } catch (e: Exception) {
                wallets[walletConfiguration.id]
            }
        }
        return null
    }

    /**
     * @see IWalletService
     */
    override suspend fun createWallet(
        configuration: WalletConfiguration,
        credentials: WalletCredentials
    ) {
        indyWrapper.createWallet(Json.encodeToString(configuration), Json.encodeToString(credentials))
    }

    /**
     * @see IWalletService
     */
    override suspend fun deleteWallet(
        configuration: WalletConfiguration,
        credentials: WalletCredentials
    ) {
        if (wallets.containsKey(configuration.id)) {
            try {
                indyWrapper.openWallet(
                    Json.encodeToString(configuration),
                    Json.encodeToString(credentials)
                )
            } catch (e: Exception) {

            }
            wallets.remove(configuration.id)
        }
        try {
            indyWrapper.deleteWallet(Json.encodeToString(configuration), Json.encodeToString(credentials))
        } catch (e: Exception) {
            throw AriesFrameworkException(ErrorCode.valueOf("Could not delete wallet."))
        }
    }
}