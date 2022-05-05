package com.digital_enabling.android_aries_sdk.agents.models

import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * Represents an agent message in unencrypted (unpacked) format.
 */
@Serializable
class UnpackedMessageContext : MessageContext {
    var senderVerkey: String?

    /**
     * @param message AgentMessage objects should be serialized with Json.encodeToString.
     */
    constructor(message: String, connection: ConnectionRecord) : super(message, false, connection) {
        this.senderVerkey = connection.theirVk
    }

    /**
     * @param message AgentMessage objects should be serialized with Json.encodeToString.
     */
    constructor(message: String, senderVerkey: String) : super(message, false) {
        this.senderVerkey = senderVerkey
    }

    /**
     * Gets the message id of the current message.
     * @throws AriesFrameworkException
     */
    fun getMessageId(): String {
        if (this.packed) {
            throw AriesFrameworkException(
                ErrorCode.INVALID_MESSAGE,
                "Cannot deserialize packed message."
            )
        } else {
            val messageJson = this.messageJson
                ?: throw AriesFrameworkException(ErrorCode.INVALID_MESSAGE, "MessageJson is missing.")
            if (!messageJson.containsKey("@id")) {
                throw AriesFrameworkException(
                    ErrorCode.INVALID_MESSAGE,
                    "Keyword \"id\" is missing."
                )
            }
            else{
                return Json.decodeFromJsonElement(messageJson["@id"]!!)
            }
        }
    }

    /**
     * Gets the type of the current message.
     * @return The type.
     * @throws AriesFrameworkException
     */
    fun getMessageType(): String {
        if (this.packed) {
            throw AriesFrameworkException(
                ErrorCode.INVALID_MESSAGE,
                "Cannot deserialize packed message."
            )
        } else {
            val messageJson = this.messageJson
                ?: throw AriesFrameworkException(ErrorCode.INVALID_MESSAGE, "MessageJson is missing.")
            if (!messageJson.containsKey("@type")) {
                throw AriesFrameworkException(
                    ErrorCode.INVALID_MESSAGE,
                    "Keyword \"type\" is missing."
                )
            }
            else{
                return Json.decodeFromJsonElement(messageJson["@type"]!!)
            }
        }
    }

    /**
     * Gets the message cast to the expect message type.
     * @return The agent message.
     */
    inline fun <reified T: AgentMessage> getMessage(): T {
        if (this.packed) {
            throw AriesFrameworkException(
                ErrorCode.INVALID_MESSAGE,
                "Cannot deserialize packed message."
            )
        } else {
            try {
                val messageJson = this.messageJson
                    ?: throw AriesFrameworkException(ErrorCode.INVALID_MESSAGE, "MessageJson is missing.")
                val propertyMap = HashMap<String, JsonElement>()
                val decoratorMap = HashMap<String, JsonElement>()
                messageJson.forEach { key, value ->
                    if(key.startsWith('~')){
                        decoratorMap[key] = value
                    }
                    else{
                        propertyMap[key] = value
                    }
                }
                val propertyJsonObject = JsonObject(propertyMap)
                val outputMessage = Json.decodeFromJsonElement<T>(propertyJsonObject)
                decoratorMap.forEach { (key, value) ->
                    val newKey = key.drop(1)
                    outputMessage.addDecorator(value, newKey)
                }
                return outputMessage
            } catch (e: Exception) {
                throw AriesFrameworkException(
                    ErrorCode.INVALID_MESSAGE,
                    "Cannot deserialize packed message.",
                    e
                )
            }
        }
    }

    /**
     * Gets the message cast to the expect message type.
     * @return The agent message.
     */
    fun getThisMessageJson(): String {
        if (this.packed) {
            throw AriesFrameworkException(
                ErrorCode.INVALID_MESSAGE,
                "Cannot deserialize packed message."
            )
        } else {
            return this.messageJson.toString()
        }
    }
}