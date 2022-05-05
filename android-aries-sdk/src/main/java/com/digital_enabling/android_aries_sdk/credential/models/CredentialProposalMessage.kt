package com.digital_enabling.android_aries_sdk.credential.models

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.toByteArray
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.util.*

/**
 * A credential content message.
 */
@Serializable
class CredentialProposalMessage: AgentMessage {
    constructor(): super(){
        id = UUID.randomUUID().toString()
        type = if (useMessageTypesHttps) {
            MessageTypesHttps.IssueCredentialNames.PREVIEW_CREDENTIAL
        } else {
            MessageTypes.IssueCredentialNames.PREVIEW_CREDENTIAL
        }
    }
    constructor(useMessageTypesHttps: Boolean = false) : super(useMessageTypesHttps) {
        id = ""
        type = if (useMessageTypesHttps) {
            MessageTypesHttps.IssueCredentialNames.PREVIEW_CREDENTIAL
        } else {
            MessageTypes.IssueCredentialNames.PREVIEW_CREDENTIAL
        }
    }

    @SerialName("comment")
    var comment: String? = null

    @Required
    @SerialName("schema_id")
    var schemaId: String? = null

    @Required
    @SerialName("cred_def_id")
    var credentialDefinitionId: String? = null

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