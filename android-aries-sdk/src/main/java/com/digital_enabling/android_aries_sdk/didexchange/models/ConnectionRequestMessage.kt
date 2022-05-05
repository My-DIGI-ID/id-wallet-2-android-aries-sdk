package com.digital_enabling.android_aries_sdk.didexchange.models

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.toByteArray
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

import java.util.*

/**
 * Represents a connection request message.
 */
@Serializable
class ConnectionRequestMessage : AgentMessage {
    /**
     * The name.
     */
    @SerialName("label")
    var label: String? = ""

    /**
     * The image URL.
     */
    @SerialName("imageUrl")
    var imageUrl: String? = ""

    /**
     * The connection object.
     */
    @SerialName("connection")
    var connection: Connection? = null

    /**
     * @see AgentMessage
     */
    constructor() : super() {
        id = UUID.randomUUID().toString()
        type =
            if (useMessageTypesHttps) MessageTypesHttps.CONNECTION_REQUEST else MessageTypes.CONNECTION_REQUEST
    }

    /**
     * @see AgentMessage
     */
    constructor(useMessageTypesHttps: Boolean = false) : super(useMessageTypesHttps) {
        id = UUID.randomUUID().toString()
        type =
            if (useMessageTypesHttps) MessageTypesHttps.CONNECTION_REQUEST else MessageTypes.CONNECTION_REQUEST
    }

    override fun toString(): String {
        return "${this::class.simpleName}: Id=$id, Type=$type, Did=${connection?.did}, Name=$label, ImageUrl=$imageUrl"
    }

    override fun toJsonObject(): JsonObject {
        val json = Json.encodeToJsonElement(this) as JsonObject
        val newMap = HashMap<String, JsonElement>()
        json.forEach { (key, value) ->
            newMap[key] = value
        }
        this.getDecorators().forEach { decorator ->
            val map = Json.decodeFromJsonElement<Map<String, JsonElement>>(decorator)
            map.forEach{(key, value) ->
                newMap[key] = value
            }
        }
        return JsonObject(newMap)
    }

    override fun asByteArray(): ByteArray{
        return this.toJsonObject().toByteArray()
    }
}