package com.digital_enabling.android_aries_sdk.credential

import com.digital_enabling.android_aries_sdk.agents.MessageType
import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.abstractions.IMessageHandler
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.configuration.AgentOptions
import com.digital_enabling.android_aries_sdk.credential.abstractions.ICredentialService
import com.digital_enabling.android_aries_sdk.credential.models.*
import com.digital_enabling.android_aries_sdk.credential.records.CredentialRecord
import com.digital_enabling.android_aries_sdk.decorators.attachments.Attachment
import com.digital_enabling.android_aries_sdk.decorators.transport.returnRoutingRequested
import com.digital_enabling.android_aries_sdk.messagedispatcher.IMessageService
import com.digital_enabling.android_aries_sdk.wallet.IWalletRecordService
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import java.util.*

/**
 * Initializes a new instance of the [DefaultCredentialHandler] class.
 * @param agentOptions The agent options.
 * @param credentialService The credential service.
 * @param recordService The wallet record service.
 * @param messageService The message service.
 */

class DefaultCredentialHandler(
    val agentOptions: AgentOptions,
    val credentialService: ICredentialService,
    val recordService: IWalletRecordService,
    val messageService: IMessageService
) : IMessageHandler {
    /**
     * The supported message types.
     */
    override val supportedMessageTypes: List<MessageType> = listOf(
        MessageType(MessageTypes.IssueCredentialNames.OFFER_CREDENTIAL),
        MessageType(MessageTypes.IssueCredentialNames.REQUEST_CREDENTIAL),
        MessageType(MessageTypes.IssueCredentialNames.ISSUE_CREDENTIAL),
        MessageType(MessageTypesHttps.IssueCredentialNames.OFFER_CREDENTIAL),
        MessageType(MessageTypesHttps.IssueCredentialNames.REQUEST_CREDENTIAL),
        MessageType(MessageTypesHttps.IssueCredentialNames.ISSUE_CREDENTIAL),
    )

    /**
     * Processes the agent message
     * @param agentContext The agent context.
     * @param messageContext The agent message.
     * @exception [AriesFrameworkException] Unsupported message type {messageType}
     */
    override suspend fun process(
        agentContext: IAgentContext,
        messageContext: UnpackedMessageContext
    ): AgentMessage? {
        val connection = messageContext.connection
            ?: throw AriesFrameworkException(
                ErrorCode.INVALID_MESSAGE,
                "Message connection missing"
            )
        when (messageContext.getMessageType()) {
            MessageTypesHttps.IssueCredentialNames.OFFER_CREDENTIAL, MessageTypes.IssueCredentialNames.OFFER_CREDENTIAL -> {
                val offer = messageContext.getMessage<CredentialOfferMessage>()
                val recordId = credentialService.processOffer(
                    agentContext,
                    offer,
                    connection
                )
                messageContext.contextRecord = credentialService.get(agentContext, recordId)
                if (agentOptions.autoRespondCredentialOffer) {
                    val pair = credentialService.createRequest(agentContext, recordId)
                    messageContext.contextRecord = pair.second
                    return pair.first
                }
                return null
            }
            MessageTypesHttps.IssueCredentialNames.REQUEST_CREDENTIAL, MessageTypes.IssueCredentialNames.REQUEST_CREDENTIAL -> {
                val request = messageContext.getMessage<CredentialRequestMessage>()
                val recordId = credentialService.processCredentialRequest(
                    agentContext,
                    request,
                    connection
                )
                if (request.returnRoutingRequested() && messageContext.connection == null) {
                    val pair = credentialService.createCredential(agentContext, recordId)
                    messageContext.contextRecord = pair.second
                    return pair.first
                } else {
                    if (agentOptions.autoRespondCredentialRequest) {
                        val pair = credentialService.createCredential(agentContext, recordId)
                        messageContext.contextRecord = pair.second
                        return pair.first
                    }
                    messageContext.contextRecord =
                        credentialService.get(agentContext, recordId)
                    return null
                }

            }
            MessageTypesHttps.IssueCredentialNames.ISSUE_CREDENTIAL, MessageTypes.IssueCredentialNames.ISSUE_CREDENTIAL -> {
                val credential = messageContext.getMessage<CredentialIssueMessage>()
                val recordId = credentialService.processCredential(
                    agentContext,
                    credential,
                    connection
                )
                messageContext.contextRecord = updateValues(
                    recordId,
                    messageContext.getMessage(),
                    agentContext
                )
                return null
            }
            else -> {
                throw AriesFrameworkException(
                    ErrorCode.INVALID_MESSAGE,
                    "Unsupported message type ${messageContext.getMessageType()}"
                )
            }
        }
    }

    private fun updateValues(
        credentialId: String,
        credentialIssue: CredentialIssueMessage,
        agentContext: IAgentContext
    ): CredentialRecord {
        var credentialAttachment: Attachment? = null
        val credentials = credentialIssue.credentials
        if (credentials != null) {
            credentialAttachment =
                credentials.firstOrNull { attachment -> attachment.id == "libindy-cred-0" }
                    ?: throw IllegalArgumentException("Credential attachment not found")
        }

        if (credentialAttachment == null) {
            throw IllegalArgumentException("Credential attachment not found")
        }
        val data = credentialAttachment.data
            ?: throw IllegalArgumentException("Credential attachment has no data")
        val base64 = data.base64
            ?: throw IllegalArgumentException("Credential attachment data has no base64 string")

        val credentialJson = String(Base64.getDecoder().decode(base64), Charsets.UTF_8)

        val jcred = Json.parseToJsonElement(credentialJson) as JsonObject
        val values =
            Json.decodeFromJsonElement<HashMap<String, AttributeValue>>(jcred["values"] as JsonElement)

        var credential: CredentialRecord? = null
        runBlocking { credential = credentialService.get(agentContext, credentialId) }
        if(credential == null){
            throw AriesFrameworkException(ErrorCode.RECORD_NOT_FOUND, "No credential found with id: $credentialId")
        }
        val credentialRecord = credential as CredentialRecord
        val credentialAttributesValues = mutableListOf<CredentialPreviewAttribute>()
        values.forEach{x ->
            val raw = x.value.raw
            if(raw != null){
                val newAttribute = CredentialPreviewAttribute(x.key, raw)
                newAttribute.mimeType = CredentialMimeTypes.TEXT_MIME_TYPE

                credentialAttributesValues.add(newAttribute)
            }
        }

        credentialRecord.credentialAttributesValues = credentialAttributesValues
        val wallet = agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")
        runBlocking { recordService.update(wallet, credentialRecord) }

        return credentialRecord
    }
}