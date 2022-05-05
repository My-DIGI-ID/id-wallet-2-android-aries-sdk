package com.digital_enabling.android_aries_sdk.discovery

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.toByteArray
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.util.*

/**
 * Represents a disclose message in the discovery protocol.
 * @see AgentMessage
 */
@Serializable
class DiscoveryDiscloseMessage: AgentMessage {
    /**
     * A list of supported protocols.
     */
    @SerialName("protocols")
    var protocols: MutableList<DisclosedMessageProtocol>

    constructor(): super(){
        id = UUID.randomUUID().toString()
        type = if (useMessageTypesHttps) MessageTypesHttps.DISCOVERY_DISCLOSE_MESSAGE_TYPE else MessageTypes.DISCOVERY_DISCLOSE_MESSAGE_TYPE
        protocols = mutableListOf()
    }

    constructor(useMessageTypesHttps: Boolean = false) : super(useMessageTypesHttps) {
        id = UUID.randomUUID().toString()
        type = if (useMessageTypesHttps) MessageTypesHttps.DISCOVERY_DISCLOSE_MESSAGE_TYPE else MessageTypes.DISCOVERY_DISCLOSE_MESSAGE_TYPE
        protocols = mutableListOf()
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