package com.digital_enabling.android_aries_sdk.trustping

import com.digital_enabling.android_aries_sdk.agents.MessageType
import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.abstractions.IMessageHandler
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.IEventAggregator
import com.digital_enabling.android_aries_sdk.common.ServiceMessageProcessingEvent
import com.digital_enabling.android_aries_sdk.decorators.threading.ThreadDecorator
import com.digital_enabling.android_aries_sdk.decorators.threading.createThreadReply
import com.digital_enabling.android_aries_sdk.messagedispatcher.IMessageService
import kotlinx.coroutines.yield
import java.lang.IllegalArgumentException

/**
 * Default trust ping message handler.
 * Initializes a new instance of the [DefaultTrustPingMessageHandler] class.
 * @param eventAggregator The event aggregator.
 * @param messageService The message service.
 */
class DefaultTrustPingMessageHandler (
    private val eventAggregator: IEventAggregator,
    private val messageService : IMessageService
) : IMessageHandler {

    /**
     * The supported message types.
     */
    override val supportedMessageTypes = listOf<MessageType>(
        MessageType(MessageTypes.TRUST_PING_MESSAGE_TYPE),
        MessageType(MessageTypes.TRUST_PING_RESPONSE_MESSAGE_TYPE),
        MessageType(MessageTypesHttps.TRUST_PING_MESSAGE_TYPE),
        MessageType(MessageTypesHttps.TRUST_PING_RESPONSE_MESSAGE_TYPE)
    )

    /**
     * Processes the agent message
     * @param agentContext Agent context.
     * @param messageContext The agent message agentContext.
     */
    override suspend fun process(
        agentContext: IAgentContext,
        messageContext: UnpackedMessageContext
    ): AgentMessage? {

        yield()
        when (messageContext.getMessageType()) {
            MessageTypes.TRUST_PING_MESSAGE_TYPE -> return null
            MessageTypesHttps.TRUST_PING_MESSAGE_TYPE -> {
                val pingMessage = messageContext.getMessage<TrustPingMessage>()
                if (pingMessage.findDecorator<ThreadDecorator>("thread") != null ) {
                    eventAggregator.publish(
                        ServiceMessageProcessingEvent(
                            messageType = MessageTypes.TRUST_PING_MESSAGE_TYPE,
                            threadId = pingMessage.findDecorator<ThreadDecorator>("thread")!!.threadId ?: throw IllegalArgumentException("ThreadDecorator is missing threadId"),
                            recordId = ""
                        )
                    )
                }
                else {
                    throw Exception("Decorator with key 'thread' not found")
                }

                if (pingMessage.responseRequested != null) {
                    if (pingMessage.responseRequested!!) {
                        return pingMessage.createThreadReply<TrustPingResponseMessage>()
                    }
                }
                return null
            }
            MessageTypes.TRUST_PING_RESPONSE_MESSAGE_TYPE -> return null
            MessageTypesHttps.TRUST_PING_RESPONSE_MESSAGE_TYPE -> {
                val pingMessage = messageContext.getMessage<TrustPingMessage>()
                if (pingMessage.findDecorator<ThreadDecorator>("thread") != null ) {
                    eventAggregator.publish(
                        ServiceMessageProcessingEvent(
                            messageType = MessageTypes.TRUST_PING_RESPONSE_MESSAGE_TYPE,
                            threadId = pingMessage.findDecorator<ThreadDecorator>("thread")!!.threadId ?: throw IllegalArgumentException("ThreadDecorator is missing threadId"),
                            recordId = ""
                        )
                    )
                }
                else{
                    throw Exception("Decorator with key 'thread' not found")
                }
            }
        }
        return null
    }
}