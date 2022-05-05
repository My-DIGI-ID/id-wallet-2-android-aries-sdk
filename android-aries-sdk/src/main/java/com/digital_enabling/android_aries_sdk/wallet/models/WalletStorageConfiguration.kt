package com.digital_enabling.android_aries_sdk.wallet.models

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Wallet storage configuration.
 */
@Serializable
data class WalletStorageConfiguration(
    /**
     * The path.
     */
    @Required
    @SerialName("path")
    var path: String? = null,
    /**
     * The url.
     */
    @SerialName("url")
    var url: String? = null,
    /**
     * The wallet scheme
     */
    @SerialName("wallet_scheme")
    var walletScheme: String? = null,
    /**
     * The database name.
     */
    @SerialName("database_name")
    var databaseName: String? = null,
    /**
     * tls (on|off).
     */
    @Required
    @SerialName("tls")
    var tls: String = "off",
    /**
     * The max connections.
     */
    @SerialName("max_connections")
    var maxConnections: Int? = null,
    /**
     * The minimum idle count.
     */
    @SerialName("min_idle_count")
    var minIdleCount: Int? = null,
    /**
     * The connection timeout.
     */
    @SerialName("connection_timeout")
    var connectionTimeout: Int? = null
) {
    override fun toString(): String =
        "${this::class.simpleName}: " +
                "Path=${path}, " +
                "Url=${url}, " +
                "WalletScheme=${walletScheme}, " +
                "DatabaseName=${databaseName}, " +
                "Tls=${tls}, " +
                "MaxConnections=${maxConnections}, " +
                "MinIdleCount=${minIdleCount}, " +
                "ConnectionTimeout=${connectionTimeout}"
}