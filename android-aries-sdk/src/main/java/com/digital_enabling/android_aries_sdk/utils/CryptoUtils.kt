package com.digital_enabling.android_aries_sdk.utils

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.toByteArray
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.routing.ForwardMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.hyperledger.indy.sdk.wallet.Wallet
import java.security.SecureRandom
import kotlin.math.abs

class CryptoUtils(val indyWrapper: IIndyWrapper = IndyWrapper()) {
    /**
     * Packs a message.
     * @param wallet The wallet.
     * @param recipientKey The recipient key.
     * @param message The message.
     * @param senderKey The sender key.
     * @return Encrypted message formatted as JWE using UTF8 byte order.
     */
    fun pack(
        wallet: Wallet,
        recipientKey: String,
        message: ByteArray,
        senderKey: String? = null
    ) = pack(wallet, arrayOf(recipientKey), message, senderKey)

    /**
     * Packs a message.
     * @param wallet The wallet.
     * @param recipientKey The recipient key.
     * @param message The message.
     * @param senderKey The sender key.
     * @return Encrypted message formatted as JWE using UTF8 byte order.
     */
    fun pack(
        wallet: Wallet,
        recipientKeys: Array<String>,
        message: ByteArray,
        senderKey: String? = null
    ) =
        IndyWrapper().cryptoPack(
            wallet,
            recipientKeys,
            message,
            senderKey
        )

    /**
     * Packs a message.
     * @param wallet The wallet.
     * @param recipientKey The recipient key.
     * @param message The message.
     * @param senderKey The sender key.
     * @param T Type T
     * @return Encrypted message formatted as JWE using UTF8 byte order.
     */
    inline fun <reified T : AgentMessage> pack(
        wallet: Wallet,
        recipientKey: String,
        message: T,
        senderKey: String? = null
    ) = pack(wallet, arrayOf(recipientKey), message.asByteArray(), senderKey)

    /**
     * Packs a message.
     * @param wallet The wallet.
     * @param recipientKey The recipient key.
     * @param message The message.
     * @param senderKey The sender key.
     * @param T Type T
     * @return Encrypted message formatted as JWE using UTF8 byte order.
     */
    inline fun <reified T: AgentMessage> pack(
        wallet: Wallet,
        recipientKeys: Array<String>,
        message: T,
        senderKey: String? = null
    ) = indyWrapper.cryptoPack(
        wallet,
        recipientKeys,
        message.asByteArray(),
        senderKey
    )

    /**
     * Unpacks the message.
     * @param wallet The wallet.
     * @param message The message.
     * @return Decrypted message as UTF8 string and sender/recipient key information
     */
    fun unpack(wallet: Wallet, message: ByteArray): UnpackResult {
        val result = indyWrapper.cryptoUnpack(wallet, message)
        if (result != null) {
            val resultAsString = String(result, Charsets.UTF_8)
            return Json.decodeFromString(resultAsString)
        }
        throw Exception("Unable to unpack message")
    }

    /**
     * Unpacks the message.
     * @param wallet The wallet.
     * @param message The message.
     * @return Decrypted message as UTF8 string and sender/recipient key information
     */
    inline fun <reified T> unpack(wallet: Wallet, message: ByteArray): T {
        val result = indyWrapper.cryptoUnpack(wallet, message)
        if (result != null) {
            val resultAsString = String(result, Charsets.UTF_8)
            return Json.decodeFromString(resultAsString)
        }
        throw Exception("Unable to unpack message")
    }

    /**
     * Generate unique random alpha-numeric key
     * @param maxSize Max size of the key.
     * @return Unique random alpha-numeric key
     */
    fun getUniqueKey(maxSize: Int): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray()
        val data = ByteArray(maxSize)
        val random = SecureRandom()

        random.nextBytes(data)

        val result = StringBuilder()
        for (byte in data) {
            result.append(chars[abs(byte % (chars.size))])
        }
        return result.toString()
    }

    /**
     * Prepares a wire level message from the application level agent message
     * this includes packing the message and wrapping it in required forward messages
     * if the message requires it.
     * @param agentContext The agentContext.
     * @param message The message context.
     * @param recipientKey The key to encrypt the message for.
     * @param routingKeys The routing keys to pack the message for.
     * @param senderKey The sender key to encrypt the message from.
     * @return The response.
     */
    fun <TInput : AgentMessage> prepare(
        agentContext: IAgentContext,
        message: TInput,
        recipientKey: String,
        routingKeys: Array<String>? = null,
        senderKey: String? = null
    ): ByteArray {
        val wallet = agentContext.wallet ?: throw Exception("Wallet not found.")

        val newRecipientKey = if (DidUtils.isDidKey(recipientKey)) {
            DidUtils.convertDidKeyToVerkey(recipientKey)
        } else {
            recipientKey
        }

        var msg = pack(
            wallet,
            newRecipientKey,
            message.asByteArray(),
            senderKey
        )
        var previousKey = newRecipientKey

        if (routingKeys != null) {
            for (routingKey in routingKeys) {
                val verkey = if (DidUtils.isDidKey(routingKey)) {
                    DidUtils.convertDidKeyToVerkey(routingKey)
                } else {
                    routingKey
                }

                val forwardMessage = ForwardMessage(agentContext.useMessageTypesHttps)
                forwardMessage.message =
                    Json.parseToJsonElement(String(msg!!, Charsets.UTF_8)) as JsonObject
                forwardMessage.to = previousKey
                msg = pack(
                    wallet,
                    verkey,
                    forwardMessage
                )
                previousKey = verkey
            }
        }
        if (msg != null) {
            return msg
        } else {
            throw Exception("Unable to pack message")
        }
    }

    /**
     * Prepares a wire level message from the application level agent message for a connection
     * this includes packing the message and wrapping it in required forward messages
     * if the message requires it.
     * @param agentContext The agentContext.
     * @param message The message context.
     * @param connection The connection to prepare the message for.
     * @return The response.
     */
    fun <TInput: AgentMessage> prepare(
        agentContext: IAgentContext,
        message: TInput,
        connection: ConnectionRecord
    ): ByteArray {
        val recipientKey = connection.theirVk ?: throw Exception("Verkey not found.")

        val routingKeys = connection.endpoint?.verkey ?: throw Exception("Verkey not found.")
        return prepare(agentContext, message, recipientKey, routingKeys, connection.myVk)
    }
}