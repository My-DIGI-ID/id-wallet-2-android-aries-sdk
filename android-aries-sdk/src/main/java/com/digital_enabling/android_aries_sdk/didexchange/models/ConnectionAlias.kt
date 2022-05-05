package com.digital_enabling.android_aries_sdk.didexchange.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Connection alias object for tagging a connection record with an alias to give more context.
 * Initializes a new instance of the [ConnectionAlias] class.
 */
@Serializable
data class ConnectionAlias(val copy: ConnectionAlias? = null) {
    /**
     * The name of the alias for the connection.
     */
    @SerialName("Name")
    var name: String? = copy?.name

    /**
     * The image url of the alias for the connection.
     */
    @SerialName("ImageUrl")
    var imageUrl: String? = copy?.imageUrl

    override fun toString(): String {
        return "${this::class.simpleName}: Name=$name, ImageUrl=$imageUrl"
    }
}