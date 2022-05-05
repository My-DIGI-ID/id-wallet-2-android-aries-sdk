package com.digital_enabling.android_aries_sdk.routing

import com.digital_enabling.android_aries_sdk.agents.*
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.PackedMessageContext
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.didexchange.IConnectionService
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DefaultForwardHandler(
    private val connectionService: IConnectionService
) : MessageHandlerBase<ForwardMessage>() {

    override val supportedMessageTypes = listOf(
        MessageType(MessageTypes.FORWARD),
        MessageType(MessageTypesHttps.FORWARD)
    )

    suspend fun process(
        message: ForwardMessage,
        agentContext: IAgentContext,
    ): AgentMessage? {

        val to = message.to ?: throw IllegalArgumentException("To is null")
        val connectionRecord = connectionService.resolveByMyKey(agentContext, to)

        val context = DefaultAgentContext()
        if (agentContext is DefaultAgentContext) {
            context.addNext(
                PackedMessageContext(
                    Json.encodeToString(message.message),
                    connectionRecord!!
                )
            )
        }

        return null
    }

    override fun process(
        message: AgentMessage,
        agentContext: IAgentContext,
        messageContext: UnpackedMessageContext
    ): AgentMessage? {
        if (message is ForwardMessage) {
            val messageTo = message.to
            var connectionRecord: ConnectionRecord? = null
            if (!messageTo.isNullOrEmpty()) {
                runBlocking {
                    connectionRecord = connectionService.resolveByMyKey(agentContext, messageTo)
                }
            }

            if (connectionRecord != null) {
                (agentContext as? DefaultAgentContext)?.addNext(
                    PackedMessageContext(
                        Json.encodeToString(message.message),
                        connectionRecord!!
                    )
                )
            }

            return null
        } else {
            throw AriesFrameworkException(
                ErrorCode.INVALID_MESSAGE,
                "Invalid message type. Expected 'ForwardMessage', found '${message::class.java.simpleName}'"
            )
        }
    }
}