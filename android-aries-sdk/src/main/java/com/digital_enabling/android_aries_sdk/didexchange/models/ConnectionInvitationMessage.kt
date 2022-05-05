package com.digital_enabling.android_aries_sdk.didexchange.models

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.toByteArray
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.util.*

/**
 * Represents an invitation message for establishing connection.
 */
@Serializable
class ConnectionInvitationMessage : AgentMessage {
    /**
     * The name.
     */
    @SerialName("label")
    var label: String = ""

    /**
     * The image URL.
     */
    @SerialName("imageUrl")
    var imageUrl: String = ""

    /**
     *  The service endpoint.
     */
    @SerialName("serviceEndpoint")
    var serviceEndpoint: String = ""

    /**
     * The routing keys.
     */
    @SerialName("routingKeys")
    var routingKeys: List<String>? = emptyList()

    /**
     * The recipient keys.
     */
    @SerialName("recipientKeys")
    var recipientKeys: List<String>? = emptyList()

    /**
     * @see AgentMessage
     */
    constructor() : super() {
        id = UUID.randomUUID().toString()
        type =
            if (useMessageTypesHttps) MessageTypesHttps.CONNECTION_INVITATION else MessageTypes.CONNECTION_INVITATION
    }

    /**
     * @see AgentMessage
     */
    constructor(useMessageTypesHttps: Boolean = false) : super(useMessageTypesHttps) {
        id = UUID.randomUUID().toString()
        type =
            if (useMessageTypesHttps) MessageTypesHttps.CONNECTION_INVITATION else MessageTypes.CONNECTION_INVITATION
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