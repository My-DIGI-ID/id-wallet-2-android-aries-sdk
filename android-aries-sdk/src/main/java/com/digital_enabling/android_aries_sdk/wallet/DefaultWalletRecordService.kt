package com.digital_enabling.android_aries_sdk.wallet

import com.digital_enabling.android_aries_sdk.basicmessage.BasicMessageRecord
import com.digital_enabling.android_aries_sdk.configuration.ProvisioningRecord
import com.digital_enabling.android_aries_sdk.credential.records.CredentialRecord
import com.digital_enabling.android_aries_sdk.credential.records.DefinitionRecord
import com.digital_enabling.android_aries_sdk.credential.records.RevocationRegistryRecord
import com.digital_enabling.android_aries_sdk.credential.records.SchemaRecord
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.payments.PaymentRecord
import com.digital_enabling.android_aries_sdk.proof.models.ProofRecord
import com.digital_enabling.android_aries_sdk.transaction.models.TransactionRecord
import com.digital_enabling.android_aries_sdk.utils.IIndyWrapper
import com.digital_enabling.android_aries_sdk.utils.IndyWrapper
import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchItem
import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchOptions
import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchQuery
import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchResult
import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperledger.indy.sdk.wallet.Wallet
import org.hyperledger.indy.sdk.wallet.WalletItemNotFoundException
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Initializes a new instance of the [DefaultWalletRecordService] class.
 * @see IWalletRecordService
 */
class DefaultWalletRecordService(val indyWrapper: IIndyWrapper = IndyWrapper()
    ) : IWalletRecordService {

    //constructor() {
    /*
       _jsonSettings = new JsonSerializerSettings
        {
            NullValueHandling = NullValueHandling.Ignore,
            Converters = new List<JsonConverter>
            {
                new AgentEndpointJsonConverter(),
                new AttributeFilterConverter()
            }
        };
     */
    // }

    /**
     * @see IWalletRecordService
     */
    override suspend fun <T : RecordBase> add(wallet: Wallet, record: T) {
        record.createdAtUtc = LocalDateTime.now()

        indyWrapper.add(
            wallet,
            record.typeName,
            record.id,
            record.toJson(),
            Json.encodeToString(record.tags)
        )
    }

    /**
     * @see IWalletRecordService
     */
    override suspend fun <T : RecordBase> search(
        wallet: Wallet,
        query: ISearchQuery?,
        options: SearchOptions?,
        count: Int,
        skip: Int,
        recordBaseType: RecordType
    ): List<T> {
        /*val search = indyWrapper.open(wallet, recordBaseType.typeName,
            (query?.let { Json.encodeToString(query) } ?: kotlin.run {
                Json.encodeToString(
                    SearchQuery.empty
                )
            }),
            (query?.let { Json.encodeToString(options) } ?: kotlin.run {
                Json.encodeToString(
                    SearchOptions()
                )
            })
        )*/
        val search = indyWrapper.open(wallet, recordBaseType.typeName,"{}","{}")
        try {
            if (skip > 0) {
                search.fetchNextRecords(wallet, skip)
            }

            val format = Json { ignoreUnknownKeys = true }
            val result = format.decodeFromString<SearchResult>(
                search.fetchNextRecords(wallet, count).get()
            )

            val recordList = mutableListOf<T>()

            var record: T
            for (r in result.records) {
                if (r.value == null) {
                    continue
                }

                val value = r.value!!
                record = when (recordBaseType) {
                    RecordType.CONNECTION_RECORD -> Json.decodeFromString<ConnectionRecord>(
                        value
                    ) as T
                    RecordType.PROVISIONING_RECORD -> Json.decodeFromString<ProvisioningRecord>(
                        value
                    ) as T
                    RecordType.CREDENTIAL_RECORD -> Json.decodeFromString<CredentialRecord>(value) as T
                    RecordType.CREDENTIAL_DEFINITION_RECORD -> Json.decodeFromString<DefinitionRecord>(
                        value
                    ) as T
                    RecordType.REVOCATION_REGISTRY_RECORD -> Json.decodeFromString<RevocationRegistryRecord>(
                        value
                    ) as T
                    RecordType.SCHEMA_RECORD -> Json.decodeFromString<SchemaRecord>(value) as T
                    RecordType.PAYMENT_RECORD -> Json.decodeFromString<PaymentRecord>(value) as T
                    RecordType.PROOF_RECORD -> Json.decodeFromString<ProofRecord>(value) as T
                    RecordType.TRANSACTION_RECORD -> Json.decodeFromString<TransactionRecord>(value) as T
                    RecordType.BASIC_MESSAGE_RECORD -> Json.decodeFromString<BasicMessageRecord>(
                        value
                    ) as T
                }
                r.tags?.forEach { tag ->
                    record.tags[tag.key] = tag.value
                }

                recordList.add(record)
            }
            return recordList
        } finally {
            search.closeSearch()
        }
    }

    /**
     * @see IWalletRecordService
     */
    override suspend fun update(wallet: Wallet, record: RecordBase) {

        record.updatedAtUtc = LocalDateTime.now(ZoneOffset.UTC)

        indyWrapper.updateValue(
            wallet, record.typeName,
            record.id, record.toJson()
        )

        indyWrapper.updateTags(
            wallet, record.typeName,
            record.id, Json.encodeToString(record.tags)
        )
    }

    /**
     * @see IWalletRecordService
     */
    override suspend fun <T : RecordBase> get(
        wallet: Wallet,
        id: String,
        recordBaseType: RecordType
    ): T? {
        try {
            val recordJson = indyWrapper.get(
                wallet,
                recordBaseType.typeName,
                id,
                Json.encodeToString(SearchOptions())
            )
                ?: return null

            val item = Json.decodeFromString<SearchItem>(recordJson)

            if (item.value == null) {
                return null
            }

            val value = item.value!!
            val record: T = when (recordBaseType) {
                RecordType.CONNECTION_RECORD -> Json.decodeFromString<ConnectionRecord>(value) as T
                RecordType.PROVISIONING_RECORD -> Json.decodeFromString<ProvisioningRecord>(value) as T
                RecordType.CREDENTIAL_RECORD -> Json.decodeFromString<CredentialRecord>(value) as T
                RecordType.CREDENTIAL_DEFINITION_RECORD -> Json.decodeFromString<DefinitionRecord>(
                    value
                ) as T
                RecordType.REVOCATION_REGISTRY_RECORD -> Json.decodeFromString<RevocationRegistryRecord>(
                    value
                ) as T
                RecordType.SCHEMA_RECORD -> Json.decodeFromString<SchemaRecord>(value) as T
                RecordType.PAYMENT_RECORD -> Json.decodeFromString<PaymentRecord>(value) as T
                RecordType.PROOF_RECORD -> Json.decodeFromString<ProofRecord>(value) as T
                RecordType.TRANSACTION_RECORD -> Json.decodeFromString<TransactionRecord>(value) as T
                RecordType.BASIC_MESSAGE_RECORD -> Json.decodeFromString<BasicMessageRecord>(value) as T
            }

            item.tags?.forEach { tag ->
                record.tags[tag.key] = tag.value
            }
            return record
        } catch (ex: WalletItemNotFoundException) {
            return null
        }
    }

    /**
     * @see IWalletRecordService
     */
    override suspend fun <T : RecordBase> delete(
        wallet: Wallet,
        id: String,
        recordBaseType: RecordType
    ): Boolean {
        try {
            val record = get<T>(wallet, id, recordBaseType)
            if (record != null) {
                val recordTags = mutableListOf<String>()
                for (t in record.tags) recordTags.add(t.key)
                indyWrapper.deleteTags(
                    wallet,
                    recordBaseType.typeName,
                    id,
                    Json.encodeToString(recordTags.toTypedArray())
                )
                indyWrapper.delete(wallet, recordBaseType.typeName, id)
            }
            return true
        } catch (ex: Exception) {
            return false
        }
    }
}