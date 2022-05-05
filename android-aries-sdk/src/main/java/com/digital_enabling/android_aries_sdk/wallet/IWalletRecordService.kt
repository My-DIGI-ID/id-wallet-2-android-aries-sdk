package com.digital_enabling.android_aries_sdk.wallet

import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.wallet.records.IRecordBase
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchOptions
import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery
import org.hyperledger.indy.sdk.wallet.Wallet

/**
 * Wallet record service.
 */
interface IWalletRecordService {

    /**
     * Adds the record.
     * @param wallet The wallet.
     * @param record The record.
     * @param T The 1st type parameter.
     * @return The record.
     */
    suspend fun <T> add(wallet: Wallet, record: T) where T : RecordBase

    /**
     * Search the record.
     * @param wallet The wallet.
     * @param query The search query.
     * @param options The options.
     * @param count The number of items to return
     * @param skip The number of items to skip.
     * @param recordBaseType The record base type.
     * @param T The 1st type parameter.
     * @return The record.
     */
    suspend fun <T> search(
        wallet: Wallet,
        query: ISearchQuery? = null,
        options: SearchOptions? = null,
        count: Int = 10,
        skip: Int = 0,
        recordBaseType: RecordType
    ): List<T> where T : RecordBase

    /**
     * Updates the record.
     * @param wallet The wallet.
     * @param record The credential record.
     */
    suspend fun update(wallet: Wallet, record: RecordBase)

    /**
     * Gets the record.
     * @param wallet The wallet.
     * @param id The identifier.
     * @param T The 1st type parameter.
     * @return The record.
     */
    suspend fun <T> get(
        wallet: Wallet,
        id: String,
        recordBaseType: RecordType
    ): T? where T : RecordBase

    /**
     * Delete the record.
     * @param wallet The wallet.
     * @param id The identifier.
     * @param recordBaseType The record base type.
     * @param T The 1st type parameter.
     * @return Boolean status indicating if the removal succeed.
     */
    suspend fun <T> delete(
        wallet: Wallet,
        id: String,
        recordBaseType: RecordType
    ): Boolean where T : RecordBase
}