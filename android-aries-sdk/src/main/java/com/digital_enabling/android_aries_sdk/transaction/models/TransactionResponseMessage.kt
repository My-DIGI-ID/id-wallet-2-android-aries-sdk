package com.digital_enabling.android_aries_sdk.transaction.models

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.toByteArray
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.util.*

@Serializable
class TransactionResponseMessage : AgentMessage {

    constructor(): super() {
        id = UUID.randomUUID().toString()
        type = MessageTypes.TransactionNames.TRANSACTION_RESPONSE
    }

    @SerialName("comment")
    var comment: String? = null

    @SerialName("transaction")
    var transaction: String? = null

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