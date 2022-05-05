package com.digital_enabling.android_aries_sdk.routing

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.toByteArray
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.util.*

/**
 *  Represents a forwarding message.
 *  @see AgentMessage
 */
@Serializable
class ForwardMessage: AgentMessage {
    constructor(): super() {
        id = UUID.randomUUID().toString()
        type = MessageTypes.FORWARD
    }
    constructor(useMessageTypesHttps: Boolean = false): super(useMessageTypesHttps){
        id = UUID.randomUUID().toString()
        type = if (this.useMessageTypesHttps) {
            MessageTypesHttps.FORWARD
        } else {
            MessageTypes.FORWARD
        }
    }

    /**
     * The to or recipient of the message.
     */
    @SerialName("to")
    var to: String? = null

    /**
     * The content.
     */
    @SerialName("msg")
    var message: JsonObject? = null

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