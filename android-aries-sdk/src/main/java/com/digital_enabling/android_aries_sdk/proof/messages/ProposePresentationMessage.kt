package com.digital_enabling.android_aries_sdk.proof.messages

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.toByteArray
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.util.*

/**
 * Propose Presentation Message
 */
@Serializable
class ProposePresentationMessage : AgentMessage {
    /**
     * Initializes a new instance of [ProposePresentationMessage] class.
     */
    constructor() : super() {
        this.id = UUID.randomUUID().toString()
        this.type = MessageTypes.PresentProofNames.PROPOSE_PRESENTATION
    }

    /**
     * Initializes a new instance of [ProposePresentationMessage] class.
     */
    constructor(useMessageTypesHttps: Boolean = false) : super(useMessageTypesHttps) {
        this.id = UUID.randomUUID().toString()
        if (this.useMessageTypesHttps) {
            this.type = MessageTypesHttps.PresentProofNames.PROPOSE_PRESENTATION
        } else {
            this.type = MessageTypes.PresentProofNames.PROPOSE_PRESENTATION
        }
    }

    /**
     * The comment.
     */
    @SerialName("comment")
    var comment: String? = null

    /**
     * The presentation proposal.
     */
    @SerialName("presentation_proposal")
    var presentationPreviewMessage: PresentationPreviewMessage? = null

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