package com.digital_enabling.android_aries_sdk.wallet.models

import kotlinx.serialization.*

/**
 * Wallet configuration.
 */
@Serializable
data class WalletConfiguration(
    /**
     * The identifier.
     */
    @SerialName("id")
    val id: String,
    /**
     * The type of the storage.
     */
    @SerialName("storage_type")
    var storageType: String? = "default",
    /**
     * The storage configuration.
     */
    @SerialName("storage_config")
    var storageConfiguration: WalletStorageConfiguration? = null
) {
    override fun toString(): String =
        "${this::class.simpleName}: " +
                "Id=${id}, " +
                "StorageType=${storageType}, " +
                "StorageConfiguration=${storageConfiguration}"
}