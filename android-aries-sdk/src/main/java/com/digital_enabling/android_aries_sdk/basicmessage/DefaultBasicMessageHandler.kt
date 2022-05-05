package com.digital_enabling.android_aries_sdk.basicmessage

import com.digital_enabling.android_aries_sdk.agents.MessageHandlerBase
import com.digital_enabling.android_aries_sdk.agents.MessageType
import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.wallet.IWalletRecordService
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Default basic message handler.
 */
class DefaultBasicMessageHandler(
    private val recordService: IWalletRecordService
) : MessageHandlerBase<BasicMessage>() {

    override val supportedMessageTypes = listOf(
        MessageType(MessageTypes.BASIC_MESSAGE_TYPE),
        MessageType(MessageTypesHttps.BASIC_MESSAGE_TYPE)
    )

    /**
     * Processes the incoming [AgentMessage].
     * @param message The message
     * @param agentContext The message agentContext
     * @param messageContext The message context
     *
     * @return Agent message
     */
    override fun process(
        message: AgentMessage,
        agentContext: IAgentContext,
        messageContext: UnpackedMessageContext
    ): AgentMessage? {
        if (message is BasicMessage) {
            val wallet = agentContext.wallet
            val connection = messageContext.connection
            if (wallet == null || connection == null) {
                throw AriesFrameworkException(
                    ErrorCode.INVALID_MESSAGE,
                    "Could not process the message."
                )
            }
            val record = BasicMessageRecord(UUID.randomUUID().toString())
            record.connectionId = connection.id
            record.text = message.content
            record.direction = MessageDirection.INCOMING

            val formatter = DateTimeFormatter.ofPattern("yyyy/mm/dd hh:mm")
            record.sentTime =
                LocalDateTime.parse(message.sentTime, formatter) ?: LocalDateTime.now()

            runBlocking { recordService.add(wallet, record) }
            messageContext.contextRecord = record
            return null
        } else {
            throw AriesFrameworkException(
                ErrorCode.INVALID_MESSAGE,
                "Invalid message type. Expected 'BasicMessage', found '${message::class.java.simpleName}'"
            )
        }
    }
}