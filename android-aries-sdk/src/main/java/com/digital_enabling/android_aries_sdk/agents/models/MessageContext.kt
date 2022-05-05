package com.digital_enabling.android_aries_sdk.agents.models

import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

/**
 * A message context object that surrounds an agent message.
 */
@Serializable
abstract class MessageContext {
    /**
     * The message as JSON.
     */
    var messageJson: JsonObject? = null

    /**
     * The raw format of the message.
     */
    val payload: ByteArray

    /**
     * Gets a value indicating whether this [MessageContext] is packed.
     */
    val packed: Boolean

    /**
     * The connection associated to the message.
     */
    var connection: ConnectionRecord? = null

    /**
     * The record associated with this message context.
     */
    var contextRecord: RecordBase? = null

    /**
     * Initializes a new instance of the [MessageContext] class.
     */
    protected constructor(message: ByteArray, packed: Boolean) {
        this.packed = packed
        this.payload = message
        if (!packed) {
            try{
                val messageString = String(message, Charsets.UTF_8)
                this.messageJson = Json.parseToJsonElement(messageString) as JsonObject
            }
            catch (e: Exception){
                this.messageJson = JsonObject(LinkedHashMap())
            }
        }
    }

    protected constructor(
        message: String,
        packed: Boolean
    ) : this(message.toByteArray(Charsets.UTF_8), packed) {
    }

    protected constructor(message: String, packed: Boolean, connection: ConnectionRecord) : this(
        message.toByteArray((Charsets.UTF_8)),
        packed
    ) {
        this.connection = connection
    }
}