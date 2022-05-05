package com.digital_enabling.android_aries_sdk.wallet

import com.digital_enabling.android_aries_sdk.wallet.models.WalletConfiguration
import com.digital_enabling.android_aries_sdk.wallet.models.WalletCredentials
import org.hyperledger.indy.sdk.wallet.Wallet

/**
 *  Wallet service.
 */
interface IWalletService {
    /**
     * Gets the wallet.
     * @param walletConfiguration The wallet configuration.
     * @param walletCredentials The wallet credentials.
     * @return The wallet.
     */
    suspend fun getWallet(walletConfiguration: WalletConfiguration, walletCredentials: WalletCredentials): Wallet

    /**
     * Creates the wallet.
     * @param walletConfiguration The wallet configuration.
     * @param walletCredentials The wallet credentials.
     */
    suspend fun createWallet(configuration: WalletConfiguration, credentials: WalletCredentials)

    /**
     * Deletes the wallet.
     * @param walletConfiguration The wallet configuration.
     * @param walletCredentials The wallet credentials.
     */
    suspend fun deleteWallet(configuration: WalletConfiguration, credentials: WalletCredentials)
}