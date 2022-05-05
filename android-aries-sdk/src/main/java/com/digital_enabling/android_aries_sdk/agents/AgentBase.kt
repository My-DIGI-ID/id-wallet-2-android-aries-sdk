package com.digital_enabling.android_aries_sdk.agents

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgent
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentMiddleware
import com.digital_enabling.android_aries_sdk.agents.abstractions.IMessageHandler
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.MessageContext
import com.digital_enabling.android_aries_sdk.agents.models.PackedMessageContext
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext
import com.digital_enabling.android_aries_sdk.basicmessage.DefaultBasicMessageHandler
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.common.toByteArray
import com.digital_enabling.android_aries_sdk.credential.DefaultCredentialHandler
import com.digital_enabling.android_aries_sdk.decorators.transport.returnRoutingRequested
import com.digital_enabling.android_aries_sdk.didexchange.DefaultConnectionHandler
import com.digital_enabling.android_aries_sdk.didexchange.IConnectionService
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.discovery.DefaultDiscoveryHandler
import com.digital_enabling.android_aries_sdk.messagedispatcher.IMessageService
import com.digital_enabling.android_aries_sdk.proof.DefaultProofHandler
import com.digital_enabling.android_aries_sdk.routing.DefaultForwardHandler
import com.digital_enabling.android_aries_sdk.trustping.DefaultTrustPingMessageHandler
import com.digital_enabling.android_aries_sdk.utils.CryptoUtils
import com.digital_enabling.android_aries_sdk.utils.IIndyWrapper
import com.digital_enabling.android_aries_sdk.utils.IndyWrapper
import com.digital_enabling.android_aries_sdk.utils.UnpackResult
import org.koin.core.component.KoinComponent

/**
 * Base agent implementation.
 */
abstract class AgentBase(
    protected val connectionService: IConnectionService,
    protected val messageService: IMessageService,
    private val connectionHandler: DefaultConnectionHandler,
    private val credentialHandler: DefaultCredentialHandler,
    private val proofHandler: DefaultProofHandler,
    private val trustPingMessageHandler: DefaultTrustPingMessageHandler,
    private val forwardHandler: DefaultForwardHandler,
    private val basicMessageHandler: DefaultBasicMessageHandler,
    private val discoveryHandler: DefaultDiscoveryHandler,
    private val indyWrapper: IIndyWrapper = IndyWrapper()
) : IAgent, KoinComponent {
    override var handlers: MutableList<IMessageHandler> = mutableListOf()
    protected val middlewares: List<IAgentMiddleware> = emptyList()

    //region Handler functions
    /**
     * Adds a handler for supporting default connection flow.
     */
    fun addConnectionHandler() = handlers.add(connectionHandler)

    /**
     * Adds a handler for supporting default credential flow.
     */
    fun addCredentialHandler() = handlers.add(credentialHandler)

    /**
     * Adds the handler for supporting default proof flow.
     */
    fun addTrustPingHandler() = handlers.add(trustPingMessageHandler)

    /**
     * Adds the handler for supporting default proof flow.
     */
    fun addProofHandler() = handlers.add(proofHandler)

    /**
     * Adds a default forwarding handler.
     */
    fun addForwardHandler() = handlers.add(forwardHandler)

    /**
     * Adds a default basic message handler.
     */
    fun addBasicMessageHandler() = handlers.add(basicMessageHandler)

    /**
     * Adds a default discovery handler.
     */
    fun addDiscoveryHandler() = handlers.add(discoveryHandler)

    /**
     * Adds an instance of a custom handler.
     */
    fun <T> addHandler(instance: T) where T : IMessageHandler = handlers.add(instance)
    //endregion

    /**
     * Invoke the handler pipeline and process the passed message.
     * @param agentContext The agent context.
     * @param messageContext The message context.
     * @throws Exception Expected inner message to be of type 'ForwardMessage'
     * @throws AriesFrameworkException Couldn't locate a message handler for type {messageType}
     * @return
     */
    override suspend fun process(
        agentContext: IAgentContext,
        messageContext: MessageContext
    ): MessageContext? {
        ensureConfigured()

        if (agentContext is DefaultAgentContext) {
            agentContext.addNext(messageContext)
            agentContext.agent = this

            try {
                var outgoingMessageContext: MessageContext?
                do {
                    val (next, message) = agentContext.tryGetNext()
                    outgoingMessageContext = processMessage(agentContext, message)
                } while (next)
                return outgoingMessageContext
            } catch (e: Exception) {
                throw Exception("Unable to process message", e)
            }

        }
        throw Exception("Unsupported agent context. When using custom context, please inherit from 'DefaultAgentContext'")
    }

    private suspend fun processMessage(
        agentContext: IAgentContext,
        messageContext: MessageContext?
    ): MessageContext? {

        val inboundMessageContext = if (messageContext is PackedMessageContext) {
            unpack(agentContext, messageContext).first
        } else {
            null
        }

        val messageHandler: IMessageHandler? =
            handlers.singleOrNull { handler -> handler.supportedMessageTypes.any { message -> message.messageTypeUri == inboundMessageContext?.getMessageType() } }

        if (messageHandler is IMessageHandler) {
            val response: AgentMessage?
            try {
                response = if (inboundMessageContext != null) {
                    messageHandler.process(agentContext, inboundMessageContext)
                } else {
                    throw AriesFrameworkException(
                        ErrorCode.INVALID_MESSAGE,
                        "Inbound message is null"
                    )
                }
            } catch (e: AriesFrameworkException) {
                throw AriesFrameworkException(
                    e.errorCode,
                    e.message ?: "Unable to process response"
                )
            }

            for (middleware in middlewares) {
                middleware.onMessage(agentContext, inboundMessageContext)
            }

            val connection =
                inboundMessageContext.connection ?: throw Exception("Unable to pack message")
            val wallet = agentContext.wallet
            val theirVk = connection.theirVk

            if (wallet == null || theirVk == null) {
                throw Exception("Unable to pack message")
            }

            if (response != null) {
                if (inboundMessageContext.returnRoutingRequested()) {
                    val cryptoUtils = CryptoUtils(indyWrapper)
                    val result = cryptoUtils.pack(
                        wallet,
                        theirVk,
                        response.toByteArray()
                    )
                    if (result != null) {
                        return PackedMessageContext(result)
                    } else {
                        throw Exception("Unable to pack message")
                    }
                }

                messageService.send(
                    agentContext,
                    response,
                    connection
                )
            }
        }
        return null
    }

    private suspend fun unpack(
        agentContext: IAgentContext,
        message: PackedMessageContext
    ): Pair<UnpackedMessageContext?, UnpackResult> {
        val unpacked: UnpackResult

        try {
            val cryptoUtils = CryptoUtils(indyWrapper)
            val wallet = agentContext.wallet ?: throw AriesFrameworkException(
                ErrorCode.INVALID_MESSAGE,
                "Failed to un-pack message"
            )
            unpacked = cryptoUtils.unpack(wallet, message.payload)
        } catch (e: Exception) {
            throw AriesFrameworkException(ErrorCode.INVALID_MESSAGE, "Failed to un-pack message", e)
        }

        var result: UnpackedMessageContext? = null
        if (unpacked.senderVerkey != null && message.connection == null) {
            if (unpacked.recipientVerkey == null
                || unpacked.message == null
            ) {
                throw AriesFrameworkException(
                    ErrorCode.INVALID_MESSAGE,
                    "Unpacked message is invalid"
                )
            }
            try {
                val connection =
                    connectionService.resolveByMyKey(agentContext, unpacked.recipientVerkey)
                result = if (connection is ConnectionRecord) {
                    UnpackedMessageContext(unpacked.message, connection)
                } else {
                    UnpackedMessageContext(unpacked.message, unpacked.senderVerkey)
                }
            } catch (e: Exception) {
                throw e
            }
        } else {
            val connection = message.connection
            if (connection != null) {
                if (unpacked.message == null) {
                    throw AriesFrameworkException(
                        ErrorCode.INVALID_MESSAGE,
                        "Unpacked message is invalid"
                    )
                }
                result = UnpackedMessageContext(unpacked.message, connection)
            }
        }

        return Pair(result, unpacked)
    }

    fun ensureConfigured() {
        if (handlers.isEmpty()) {
            configureHandlers()
        }
    }

    /**
     *
     */
    protected open fun configureHandlers() {
    }
}