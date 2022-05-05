package com.digital_enabling.android_aries_sdk.proof.messages

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.toByteArray
import com.digital_enabling.android_aries_sdk.decorators.attachments.Attachment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.util.*

/**
 * Request presentation message
 */
@Serializable
class RequestPresentationMessage : AgentMessage {
    /**
     * Initializes a new instance of the [RequestPresentationMessage] class.
     */
    constructor() : super() {
        this.id = UUID.randomUUID().toString()
        this.type = MessageTypes.PresentProofNames.REQUEST_PRESENTATION
    }

    /**
     * Initializes a new instance of the [RequestPresentationMessage] class.
     */
    constructor(useMessageTypesHttps: Boolean = false) : super(useMessageTypesHttps) {
        this.id = UUID.randomUUID().toString()
        if (this.useMessageTypesHttps) {
            this.type = MessageTypesHttps.PresentProofNames.REQUEST_PRESENTATION
        } else {
            this.type = MessageTypes.PresentProofNames.REQUEST_PRESENTATION
        }
    }

    /**
     * The comment.
     */
    @SerialName("comment")
    var comment: String? = null

    /**
     * The request presentation attachments.
     */
    @SerialName("request_presentations~attach")
    var requests: Array<Attachment>? = null

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