package com.digital_enabling.android_aries_sdk.messagedispatcher

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.MessageContext
import com.digital_enabling.android_aries_sdk.agents.models.PackedMessageContext
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.decorators.DecoratorNames
import com.digital_enabling.android_aries_sdk.decorators.transport.ReturnRouteTypes
import com.digital_enabling.android_aries_sdk.decorators.transport.TransportDecorator
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.utils.CryptoUtils
import com.digital_enabling.android_aries_sdk.utils.IIndyWrapper
import com.digital_enabling.android_aries_sdk.utils.IndyWrapper
import com.digital_enabling.android_aries_sdk.utils.UnpackResult
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperledger.indy.sdk.wallet.Wallet
import java.net.URI

/**
 * Initializes a new instance of the [DefaultMessageService] class.
 * @param messageDispatchers The message handler.
 */
open class DefaultMessageService(
    protected val messageDispatchers: List<IMessageDispatcher>,
    val indyWrapper: IIndyWrapper = IndyWrapper()
) :
    IMessageService {
    companion object {
        /**
         * The agent wire message MIME type
         */
        const val AgentWireMessageMimeType = "application/ssi-agent-wire"
    }

    override fun unpack(
        wallet: Wallet,
        message: PackedMessageContext,
        senderKey: String
    ): UnpackedMessageContext {
        val unpacked: UnpackResult
        val cryptoUtils = CryptoUtils(indyWrapper)
        try {
            unpacked = cryptoUtils.unpack(wallet, message.payload)
        } catch (e: Exception) {
            throw AriesFrameworkException(ErrorCode.INVALID_MESSAGE, "Failed to un-pack message", e)
        }

        return UnpackedMessageContext(unpacked.message ?: "{}", senderKey)
    }

    /**
     * @see IMessageService
     */
    override suspend fun <TInput : AgentMessage> send(
        agentContext: IAgentContext,
        message: TInput,
        recipientKey: String,
        endpointUri: String,
        routingKeys: Array<String>?,
        senderKey: String?,
        additionalHeaders: List<Pair<String, String>>
    ) {
        val uri = URI(endpointUri)
        val dispatcher = getDispatcher(uri.scheme)
            ?: throw AriesFrameworkException(
                ErrorCode.A2A_MESSAGE_TRANSMISSION_ERROR,
                "No registered dispatcher for transport scheme : ${uri.scheme}"
            )

        val cryptoUtils = CryptoUtils(indyWrapper)
        val wireMsg =
            cryptoUtils.prepare(agentContext, message, recipientKey, routingKeys, senderKey)

        dispatcher.dispatch(uri, PackedMessageContext(wireMsg), additionalHeaders)
    }

    /**
     * @see IMessageService
     */
    override suspend fun <TInput : AgentMessage> send(
        agentContext: IAgentContext,
        message: TInput,
        connection: ConnectionRecord,
        additionalHeaders: List<Pair<String, String>>
    ) {
        val routingKeys = connection.endpoint?.verkey ?: arrayOf()
        val recipientKey = connection.theirVk ?: connection.getTag("InvitationKey")
        ?: throw UnsupportedOperationException("Cannot locate a recipient key")

        val endpointUri = connection.endpoint?.uri
        if (endpointUri != null) {
            send(
                agentContext,
                message,
                recipientKey,
                endpointUri,
                routingKeys,
                connection.myVk
            )
        }
    }

    /**
     * @see IMessageService
     */
    override suspend fun <TInput : AgentMessage> sendReceive(
        agentContext: IAgentContext,
        message: TInput,
        recipientKey: String,
        endpointUri: String,
        routingKeys: Array<String>?,
        senderKey: String?,
        returnType: ReturnRouteTypes,
        additionalHeaders: List<Pair<String, String>>
    ): MessageContext {
        val uri = URI(endpointUri)
        val dispatcher = getDispatcher(uri.scheme)
            ?: throw AriesFrameworkException(
                ErrorCode.A2A_MESSAGE_TRANSMISSION_ERROR,
                "No registered dispatcher for transport scheme : ${uri.scheme}"
            )

        val decorator = TransportDecorator()
        decorator.returnRoute = returnType.toString()
        message.addDecorator(decorator, DecoratorNames.TRANSPORT_DECORATOR)

        val cryptoUtils = CryptoUtils(indyWrapper)
        val wireMsg = cryptoUtils.prepare(agentContext, message, recipientKey, routingKeys, senderKey)

        val response = dispatcher.dispatch(
            uri,
            PackedMessageContext(wireMsg),
            additionalHeaders
        )
        val wallet = agentContext.wallet
            ?: throw AriesFrameworkException(
                ErrorCode.INVALID_MESSAGE,
                "Could not process the message."
            )
        if (response is PackedMessageContext) {
            return unpack(wallet, response, senderKey ?: "")
        }
        throw UnsupportedOperationException()
    }

    /**
     * @see IMessageService
     */
    override suspend fun <TInput : AgentMessage> sendReceive(
        agentContext: IAgentContext,
        message: TInput,
        connection: ConnectionRecord,
        returnType: ReturnRouteTypes,
        additionalHeaders: List<Pair<String, String>>
    ): MessageContext? {
        val routingKeys = connection.endpoint?.verkey ?: arrayOf()
        val recipientKey = connection.theirVk ?: connection.getTag("InvitationKey")
        val endpointUri = connection.endpoint?.uri
            ?: throw AriesFrameworkException(
                ErrorCode.A2A_MESSAGE_TRANSMISSION_ERROR,
                "Cannot send to connection that does not have endpoint information specified"
            )

        if (recipientKey == null) {
            throw AriesFrameworkException(
                ErrorCode.INVALID_MESSAGE,
                "Could not process the message."
            )
        }
        return sendReceive(
            agentContext,
            message,
            recipientKey,
            endpointUri,
            routingKeys,
            connection.myVk,
            returnType,
            additionalHeaders
        )
    }

    override fun getDispatcher(scheme: String) =
        messageDispatchers.firstOrNull { x -> x.transportSchemes.contains(scheme) }
}