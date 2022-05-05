package com.digital_enabling.android_aries_sdk.didexchange.models

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.toByteArray
import com.digital_enabling.android_aries_sdk.decorators.signature.SignatureDecorator
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

import java.util.*
/**
 * Represents a connection response message
 */
@Serializable
class ConnectionResponseMessage : AgentMessage {
    /**
     * The connection object.
     */
    @SerialName("connection~sig")
    var connectionSig: SignatureDecorator? = null

    /**
     * @see AgentMessage
     */
    constructor() : super() {
        id = UUID.randomUUID().toString()
        type =
            if (useMessageTypesHttps) MessageTypesHttps.CONNECTION_RESPONSE else MessageTypes.CONNECTION_RESPONSE
    }

    /**
     * @see AgentMessage
     */
    constructor(useMessageTypesHttps: Boolean = false) : super(useMessageTypesHttps) {
        id = UUID.randomUUID().toString()
        type =
            if (useMessageTypesHttps) MessageTypesHttps.CONNECTION_RESPONSE else MessageTypes.CONNECTION_RESPONSE
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