package com.digital_enabling.android_aries_sdk.trustping

import com.digital_enabling.android_aries_sdk.agents.DefaultAgentContext
import com.digital_enabling.android_aries_sdk.agents.MessageType
import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.abstractions.IMessageHandler
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.PackedMessageContext
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext
import com.digital_enabling.android_aries_sdk.common.IEventAggregator
import com.digital_enabling.android_aries_sdk.common.ServiceMessageProcessingEvent
import com.digital_enabling.android_aries_sdk.common.toByteArray
import com.digital_enabling.android_aries_sdk.decorators.threading.ThreadDecorator
import com.digital_enabling.android_aries_sdk.decorators.threading.createThreadReply
import com.digital_enabling.android_aries_sdk.messagedispatcher.IMessageService
import com.digital_enabling.android_aries_sdk.routing.ForwardMessage
import kotlinx.coroutines.yield
import kotlinx.serialization.SerialName
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.util.*

/**
 * A ping response message.
 */
class TrustPingResponseMessage : AgentMessage {
    /**
     * Initializes a new instance of the [TrustPingResponseMessage] class.
     */
    constructor() : super() {
        id = UUID.randomUUID().toString()
        type = MessageTypes.TRUST_PING_RESPONSE_MESSAGE_TYPE
    }

    /**
     * Initializes a new instance of the [TrustPingResponseMessage] class.
     */
    constructor(useMessageTypesHttps: Boolean = false) : super(useMessageTypesHttps) {
        id = UUID.randomUUID().toString()
        type = if (this.useMessageTypesHttps) {
            MessageTypesHttps.TRUST_PING_RESPONSE_MESSAGE_TYPE
        } else {
            MessageTypes.TRUST_PING_RESPONSE_MESSAGE_TYPE
        }
    }

    /**
     * The comment.
     */
    @SerialName("comment")
    var comment: String? = null

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