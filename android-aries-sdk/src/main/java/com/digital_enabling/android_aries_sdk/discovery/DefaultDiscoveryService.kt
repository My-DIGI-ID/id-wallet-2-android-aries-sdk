package com.digital_enabling.android_aries_sdk.discovery

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.common.IEventAggregator
import com.digital_enabling.android_aries_sdk.decorators.threading.createThreadReply
import java.lang.IllegalArgumentException

class DefaultDiscoveryService(val eventAggregator: IEventAggregator): IDiscoveryService {

    /**
     * @see [IDiscoveryService]
     */
    override fun createQuery(agentContext: IAgentContext, query: String): DiscoveryQueryMessage {
        if(query.isEmpty()){
            throw IllegalArgumentException("Query is empty")
        }

        val message = DiscoveryQueryMessage(agentContext.useMessageTypesHttps)
        message.query = query
        return message
    }

    /**
     * @see [IDiscoveryService]
     */
    override fun createQueryResponse(
        agentContext: IAgentContext,
        message: DiscoveryQueryMessage
    ): DiscoveryDiscloseMessage {
        val query = message.query
        if(query.isNullOrEmpty()){
            throw IllegalArgumentException("Message query is empty")
        }

        val supportedMessages = when {
            message.query == "*" -> {
                agentContext.supportedMessages
            }
            query.endsWith("*") -> {
                agentContext.supportedMessages?.filter{
                    it.messageTypeUri?.startsWith(query.trimEnd('*')) == true
                }
            }
            else -> {
                agentContext.supportedMessages?.filter{
                    it.messageTypeUri == message.query
                }
            }
        }

        val disclosureMessage = message.createThreadReply<DiscoveryDiscloseMessage>()

        supportedMessages?.forEach {
            it.messageFamilyUri?.let { it1 -> DisclosedMessageProtocol(it1) }
        }

        return disclosureMessage
    }

}