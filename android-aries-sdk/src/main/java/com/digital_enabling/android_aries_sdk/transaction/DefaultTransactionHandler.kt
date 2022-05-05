package com.digital_enabling.android_aries_sdk.transaction

import com.digital_enabling.android_aries_sdk.agents.MessageType
import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.abstractions.IMessageHandler
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.transaction.abstractions.ITransactionService
import com.digital_enabling.android_aries_sdk.transaction.models.TransactionResponseMessage
import java.lang.IllegalArgumentException

class DefaultTransactionHandler(
    private val transactionService: ITransactionService
) : IMessageHandler {

    override val supportedMessageTypes: Iterable<MessageType> = listOf(
        MessageType(MessageTypes.TransactionNames.TRANSACTION_RESPONSE),
        MessageType(MessageTypesHttps.TransactionNames.TRANSACTION_RESPONSE)
    )

    override suspend fun process(
        agentContext: IAgentContext,
        messageContext: UnpackedMessageContext
    ): AgentMessage? {
        when (messageContext.getMessageType()) {
            MessageTypes.TransactionNames.TRANSACTION_RESPONSE,
            MessageTypesHttps.TransactionNames.TRANSACTION_RESPONSE -> {
                val transaction = messageContext.getMessage<TransactionResponseMessage>()
                val connection = messageContext.connection ?: throw IllegalArgumentException("Connection is null")
                transactionService.processTransaction(
                    agentContext,
                    transaction,
                    connection
                )
                messageContext.contextRecord = messageContext.connection
                return null
            }
            else -> throw AriesFrameworkException(
                ErrorCode.INVALID_MESSAGE,
                "Unsupported message type ${messageContext.getMessageType()}"
            )
        }
    }
}