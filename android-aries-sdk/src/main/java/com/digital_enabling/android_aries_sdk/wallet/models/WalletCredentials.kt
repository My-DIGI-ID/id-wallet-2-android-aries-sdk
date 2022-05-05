package com.digital_enabling.android_aries_sdk.wallet.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Wallet credentials.
 */
@Serializable
data class WalletCredentials(
    /**
     * The key.
     */
    @SerialName("key")
    val key: String,
    /**
     * The new key.
     */
    @SerialName("rekey")
    var newKey: String? = null,

    /**
     * The key derivation method.
     * [Optional] <string>
     * algorithm to use for master key derivation:
     * ARAGON2I_MOD (used by default)
     * ARAGON2I_INT - less secured but faster
     */
    @SerialName("key_derivation_method")
    var keyDerivationMethod: String? = null,

    /**
     * [Optional] The storage credentials.
     */
    @SerialName("storage_credentials")
    var storageCredentials: String? = null
) {
    override fun toString(): String =
        "${this::class.simpleName}: " +
                "Key=${if (key.isNotEmpty()) "[hidden]" else null}, " +
                "NewKey=${if (!newKey.isNullOrEmpty()) "[hidden]" else null}, " +
                "KeyDerivationmethod=${keyDerivationMethod}, " +
                "StorageCredentials=${if (storageCredentials != null) "[hidden]" else null}"
}
