package com.digital_enabling.android_aries_sdk.agents.models

import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Represents an agent message in encrypted (packed) format.
 */
@Serializable
class PackedMessageContext : MessageContext {
    /**
     * Initializes a new instance of the [PackedMessageContext] class.
     * @param message ByteArray
     */
    constructor(message: ByteArray) : super(message, true)

    /**
     * Initializes a new instance of the [PackedMessageContext] class.
     * @param message String
     */
    constructor(message: String) : super(message, true)

    /**
     * Initializes a new instance of the [PackedMessageContext] class.
     * @param message JsonObject
     */
    constructor(message: JsonObject) : super(message.toString(), true)

    /**
     * Initializes a new instance of the [PackedMessageContext] class.
     * @param message String
     * @param connection ConnectionRecord
     */
    constructor(message: String, connection: ConnectionRecord) : super(message, true, connection)
}