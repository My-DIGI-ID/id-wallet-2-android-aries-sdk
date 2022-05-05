package com.digital_enabling.android_aries_sdk.credential.models

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.toByteArray
import com.digital_enabling.android_aries_sdk.decorators.attachments.Attachment
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.util.*

/**
 * A credential content message.
 */
@Serializable
class CredentialOfferMessage: AgentMessage {

    constructor(): super(){
        id = UUID.randomUUID().toString()
        type = if (useMessageTypesHttps) {
            MessageTypesHttps.IssueCredentialNames.OFFER_CREDENTIAL
        } else {
            MessageTypes.IssueCredentialNames.OFFER_CREDENTIAL
        }
    }
    constructor(useMessageTypesHttps: Boolean = false): super(useMessageTypesHttps) {
        id = UUID.randomUUID().toString()
        type = if (useMessageTypesHttps) {
            MessageTypesHttps.IssueCredentialNames.OFFER_CREDENTIAL
        } else {
            MessageTypes.IssueCredentialNames.OFFER_CREDENTIAL
        }
    }

    @SerialName("comment")
    var comment: String? = null

    @Required
    @SerialName("credential_preview")
    var credentialPreview: CredentialPreviewMessage? = null

    @Required
    @SerialName("offers~attach")
    var offers: Array<Attachment>? = null

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