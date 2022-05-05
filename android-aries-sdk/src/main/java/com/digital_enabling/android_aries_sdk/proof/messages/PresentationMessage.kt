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
 * Proof Presentation message.
 */
@Serializable
class PresentationMessage : AgentMessage {
    /**
     * Initializes a new instace of the [PresentationMessage] class
     */
    constructor() : super() {
        this.id = UUID.randomUUID().toString()
        this.type = MessageTypes.PresentProofNames.PRESENTATION
    }

    /**
     * Initializes a new instace of the [PresentationMessage] class
     */
    constructor(useMessageTypesHttps: Boolean = false) : super(useMessageTypesHttps) {
        this.id = UUID.randomUUID().toString()
        if (this.useMessageTypesHttps) {
            this.type = MessageTypesHttps.PresentProofNames.PRESENTATION
        } else {
            this.type = MessageTypes.PresentProofNames.PRESENTATION
        }
    }

    /**
     * The comment.
     */
    @SerialName("comment")
    var comment: String? = null

    /**
     * The presentation attachment
     */
    @SerialName("presentations~attach")
    var presentations: Array<Attachment>? = null

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