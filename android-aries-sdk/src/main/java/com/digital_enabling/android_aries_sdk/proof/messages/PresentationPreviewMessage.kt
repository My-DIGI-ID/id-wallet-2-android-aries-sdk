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
 * Presentation Preview Message
 */
@Serializable
class PresentationPreviewMessage : AgentMessage {
    /**
     * Initializes a new instance of the [PresentationPreviewMessage] class.
     */
    constructor() : super() {
        this.id = UUID.randomUUID().toString()
        this.type = MessageTypes.PresentProofNames.PRESENTATION_PREVIEW
    }

    /**
     * Initializes a new instance of the [PresentationPreviewMessage] class.
     */
    constructor(useMessageTypesHttps: Boolean = false) : super(useMessageTypesHttps) {
        this.id = UUID.randomUUID().toString()
        if (this.useMessageTypesHttps) {
            this.type = MessageTypesHttps.PresentProofNames.PRESENTATION_PREVIEW
        } else {
            this.type = MessageTypes.PresentProofNames.PRESENTATION_PREVIEW
        }
    }

    /**
     * The proposed attributes.
     */
    @SerialName("attributes")
    var proposedAttributes: Array<ProposedAttribute>? = null

    /**
     * The proposed predicates
     */
    @SerialName("predicates")
    var proposedPredicates: Array<ProposedPredicate>? = null

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