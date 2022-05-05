package com.digital_enabling.android_aries_sdk.basicmessage

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.toByteArray
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.util.*

/**
 * Basic message.
 */
@Serializable
class BasicMessage : AgentMessage{

    /**
     * Initializes a new instance of the [BasicMessage] class.
     */
    constructor(): super() {
        id = UUID.randomUUID().toString()
        type = MessageTypes.BASIC_MESSAGE_TYPE
    }

    /**
     * Initializes a new instance of the [BasicMessage] class.
     */
    constructor(useMessageTypesHttps: Boolean = false): super(useMessageTypesHttps){
        id = UUID.randomUUID().toString()
        type = if (this.useMessageTypesHttps) {
            MessageTypesHttps.BASIC_MESSAGE_TYPE
        } else {
            MessageTypes.BASIC_MESSAGE_TYPE
        }
    }

    /**
     * The content
     */
    @SerialName("content")
    var content: String? = null

    /**
     * The sent time.
     *
     * The UTC sent time in ISO 8601 string format.
     */
    @SerialName("sent_time")
    var sentTime: String? = null

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