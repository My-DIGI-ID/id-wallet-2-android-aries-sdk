package com.digital_enabling.android_aries_sdk.utils

import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URI
import java.util.*

/**
 * Utilities for handling agent messages.
 */
object MessageUtils {
    private val messageTypeRegex = Regex(
        "^(https:\\/\\/didcomm.org|did:[a-z]+:[a-zA-z\\d]+;spec)\\/([a-z\\S]+)\\/([0-9].[0-9])\\/([a-z\\S]+)"
    )

    /**
     * The valid query parameters.
     */
    private val validQueryParameters = listOf("m", "c_i")

    /**
     * Encodes a message to a valid URL based format.
     * @param baseUrl Base URL for encoding the message with.
     * @param message Message to encode.
     * @return Encoded message as a valid URL.
     */
    inline fun <reified T> encodeMessageToUrlFormat(
        baseUrl: String,
        message: T
    ): String where T : AgentMessage {
        if (baseUrl.isEmpty()) {
            throw IllegalArgumentException("baseUrl is empty")
        }

        if (!isWellFormedString(baseUrl)) {
            throw IllegalArgumentException("Not a valid URI")
        }

        return encodeMessageToUrlFormat(URI(baseUrl), message)
    }

    /**
     * Encodes a message to a valid URL based format.
     * @param baseUrl Base URL for encoding the message with.
     * @param message Message to encode.
     * @return Encoded message as a valid URL.
     */
    inline fun <reified T> encodeMessageToUrlFormat(
        baseUrl: URI,
        message: T
    ): String where T : AgentMessage {
        return "{$baseUrl}?m={${
            Base64.getEncoder().encodeToString(Json.encodeToString(message).toByteArray())
        }}"
    }

    /**
     * Decodes a message from a valid URL based format.
     * @param encodedMessage Encoded message.
     * @return The agent message as a JSON string.
     */
    fun decodeMessageFromUrlFormat(encodedMessage: String): String {
        if (encodedMessage.isEmpty()) {
            throw IllegalArgumentException("Encoded message is empty")
        }

        if (!isWellFormedString(encodedMessage)) {
            throw IllegalArgumentException("Encoded message not a valid URI")
        }

        val uri = URI(encodedMessage)
        var messageBase64: String

        validQueryParameters.forEach() {

        }

        return ""
    }

    /**
     * Decodes a message from a valid URL based format.
     * @param encodedMessage Encoded message.
     * @return The agent message as a object.
     */
    inline fun <reified T> decodeMessageFromUrlFormat(encodedMessage: String): T {
        return Json.decodeFromString(decodeMessageFromUrlFormat(encodedMessage))
    }

    /**
     * Decodes a message type into its composing elements.
     * @param messageType A message type in string representation.
     * @return A tuple of the elements composing a message type string representation.
     */
    fun decodeMessageTypeUri(messageType: String): DecodedMessageType {
        if (messageType.isEmpty()) {
            throw IllegalArgumentException("messageType is empty")
        }

        val regExMatches = messageTypeRegex.matchEntire(messageType)
        if (regExMatches != null) {
            if (regExMatches.groupValues.count() != 5) {
                throw AriesFrameworkException(
                    ErrorCode.INVALID_PARAMETER_FORMAT,
                    "{$messageType} is an invalid message type"
                )
            }

            return DecodedMessageType(
                regExMatches.groupValues[1],
                regExMatches.groupValues[2],
                regExMatches.groupValues[3],
                regExMatches.groupValues[4]
            )
        } else {
            throw AriesFrameworkException(
                ErrorCode.INVALID_PARAMETER_FORMAT,
                "{$messageType} is an invalid message type"
            )
        }
    }

    /**
     * Helper function to check if a string is a valid uri.
     * @param uriString String to be checked.
     * @return True if valid Uri.
     */
    fun isWellFormedString(uriString: String): Boolean{
        return try{
            URI(uriString).toURL()
            true
        } catch(e: java.net.MalformedURLException){
            false
        }
    }
}