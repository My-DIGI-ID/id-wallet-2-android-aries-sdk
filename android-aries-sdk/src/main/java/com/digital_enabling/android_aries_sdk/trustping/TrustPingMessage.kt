package com.digital_enabling.android_aries_sdk.trustping

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.toByteArray
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.util.*

/**
 * A ping message.
 */
@Serializable
class TrustPingMessage: AgentMessage {
    /**
     * Initializes a new instance of the [TrustPingMessage] class.
     */
    constructor(): super() {
        id = UUID.randomUUID().toString()
        type = MessageTypes.TRUST_PING_MESSAGE_TYPE
    }
    /**
     * Initializes a new instance of the [TrustPingMessage] class.
     */
    constructor(useMessageTypesHttps: Boolean = false): super(useMessageTypesHttps){
        id = UUID.randomUUID().toString()
        type = if (this.useMessageTypesHttps) {
            MessageTypesHttps.TRUST_PING_MESSAGE_TYPE
        } else {
            MessageTypes.TRUST_PING_MESSAGE_TYPE
        }
    }

    /**
     * The comment.
     */
    @SerialName("comment")
    var comment: String? = null

    /**
     * The response requested status.
     */
    @SerialName("response_requested")
    var responseRequested: Boolean? = null

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