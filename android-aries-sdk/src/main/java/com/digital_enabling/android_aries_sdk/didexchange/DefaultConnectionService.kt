package com.digital_enabling.android_aries_sdk.didexchange

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.AgentEndpoint
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.common.IEventAggregator
import com.digital_enabling.android_aries_sdk.common.ServiceMessageProcessingEvent
import com.digital_enabling.android_aries_sdk.configuration.IProvisioningService
import com.digital_enabling.android_aries_sdk.decorators.attachments.Attachment
import com.digital_enabling.android_aries_sdk.decorators.attachments.AttachmentContent
import com.digital_enabling.android_aries_sdk.decorators.attachments.addAttachment
import com.digital_enabling.android_aries_sdk.decorators.signature.SignatureUtils
import com.digital_enabling.android_aries_sdk.decorators.threading.getThreadId
import com.digital_enabling.android_aries_sdk.decorators.threading.threadFrom
import com.digital_enabling.android_aries_sdk.didexchange.extensions.myDidDoc
import com.digital_enabling.android_aries_sdk.didexchange.models.*
import com.digital_enabling.android_aries_sdk.didexchange.models.dids.IndyAgentDidDocService
import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.utils.TagConstants
import com.digital_enabling.android_aries_sdk.wallet.IWalletRecordService
import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchQuery
import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery
import org.hyperledger.indy.sdk.crypto.Crypto
import org.hyperledger.indy.sdk.did.Did
import java.util.*

/**
 * Initializes a new instance of the [DefaultConnectionService] class.
 * @param eventAggregator The event aggregator.
 * @param recordService The record service.
 * @param provisioningService The provisioning service.
 */
open class DefaultConnectionService(
    val eventAggregator: IEventAggregator,
    private val recordService: IWalletRecordService,
    private val provisioningService: IProvisioningService
) : IConnectionService {

    /**
     * @see IConnectionService
     */
    override suspend fun createInvitation(
        agentContext: IAgentContext,
        config: InviteConfiguration?
    ): Pair<ConnectionInvitationMessage, ConnectionRecord> {
        val myConfig = config ?: InviteConfiguration()
        val connectionId = config?.connectionId ?: UUID.randomUUID().toString()

        val connectionKey = Crypto.createKey(agentContext.wallet, "{}").get()
        val connection = ConnectionRecord(connectionId)
        connection.setTag(TagConstants.CONNECTION_KEY, connectionKey)

        if (myConfig.autoAcceptConnection) {
            connection.setTag(TagConstants.AUTO_ACCEPT_CONNECTION, "true")
        }

        connection.multiPartyInvitation = myConfig.multiPartyInvitation

        if (!myConfig.multiPartyInvitation) {
            connection.alias = myConfig.theirAlias
            val myConfigAlias = myConfig.theirAlias
            if (myConfigAlias != null) {
                val aliasName = myConfigAlias.name
                if (!aliasName.isNullOrEmpty()) {
                    connection.setTag(
                        TagConstants.ALIAS,
                        aliasName
                    )
                }
            }
        }

        myConfig.tags.forEach {
            connection.setTag(it.key, it.value)
        }

        val wallet = agentContext.wallet ?: throw Exception("Wallet not found.")

        val provisioning =
            provisioningService.getProvisioning(wallet)
        val endpointUri = provisioning.endpoint.uri ?: throw Exception("Uri not found.")
        if (endpointUri.isEmpty()) {
            throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Provision record has no endpoint information specified"
            )
        }

        recordService.add(wallet, connection)

        val connectionInvitation = ConnectionInvitationMessage(agentContext.useMessageTypesHttps)
        val verkey = provisioning.endpoint.verkey ?: throw Exception("Verkey not found.")
        connectionInvitation.serviceEndpoint = endpointUri
        connectionInvitation.routingKeys = verkey.toList()
        connectionInvitation.recipientKeys = listOf(connectionKey)
        val ownerName = provisioning.owner.name ?: throw Exception("Owner name not found.")
        connectionInvitation.label = myConfig.myAlias?.name ?: ownerName
        val imageUrl = provisioning.owner.imageUrl ?: throw Exception("Image url not found.")
        connectionInvitation.imageUrl = myConfig.myAlias?.imageUrl ?: imageUrl

        return Pair(connectionInvitation, connection)
    }

    /**
     * @see IConnectionService
     */
    override suspend fun revokeInvitation(agentContext: IAgentContext, invitationId: String) {
        val connection =
            get(agentContext, invitationId)
        if (connection.state != ConnectionState.INVITED) {
            throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Connection state was invalid. Expected '{${ConnectionState.INVITED}}', found '{${connection.state}}'"
            )
        }

        val wallet = agentContext.wallet
        if (wallet != null) {
            recordService.delete<ConnectionRecord>(
                wallet,
                invitationId,
                RecordType.CONNECTION_RECORD
            )
        } else {
            throw Exception("Wallet not found.")
        }
    }

    /**
     * @see IConnectionService
     */
    override suspend fun createRequest(
        agentContext: IAgentContext,
        invitation: ConnectionInvitationMessage
    ): Pair<ConnectionRequestMessage, ConnectionRecord> {

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")
        val my = Did.createAndStoreMyDid(agentContext.wallet, "{}").get()
        val connection = ConnectionRecord(UUID.randomUUID().toString().lowercase())
        val endpoint = AgentEndpoint()

        endpoint.did = null
        endpoint.uri = invitation.serviceEndpoint
        endpoint.verkey = invitation.routingKeys?.toTypedArray()

        connection.endpoint = endpoint

        connection.myDid = my.did
        connection.myVk = my.verkey
        //invitation.recipientKeys?.let { connection.setTag("InvitationKey", it.first()) }
        invitation.recipientKeys?.let { connection.theirVk = it.first() }

        if (invitation.label.isNotBlank() || invitation.imageUrl.isNotBlank()) {
            val alias = ConnectionAlias()

            alias.name = invitation.label
            alias.imageUrl = invitation.imageUrl

            connection.alias = alias

            if (invitation.label.isBlank()) {
                connection.setTag(TagConstants.ALIAS, invitation.label)
            }
        }

        connection.trigger(ConnectionTrigger.INVITATION_ACCEPT)

        val provisioning = provisioningService.getProvisioning(wallet)
        val request = ConnectionRequestMessage(agentContext.useMessageTypesHttps)
        val myDid = connection.myDid
        request.connection = Connection(myDid, connection.myDidDoc(provisioning))
        request.label = provisioning.owner.name
        request.imageUrl = provisioning.owner.imageUrl

        if (provisioning.owner.imageUrl != null) {
            val attachment = Attachment()
            attachment.nickname = "profile-image"
            val data = AttachmentContent()
            val imageUrl = provisioning.owner.imageUrl ?: throw Exception("Image Url not found.")
            data.links = arrayOf(imageUrl)
            attachment.data = data
            request.addAttachment(attachment)
        }

        recordService.add(wallet, connection)

        return Pair(request, connection)
    }


    /**
     * @see IConnectionService
     */
    override suspend fun processRequest(
        agentContext: IAgentContext,
        request: ConnectionRequestMessage,
        connection: ConnectionRecord
    ): String {
        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")
        val my = Did.createAndStoreMyDid(agentContext.wallet, "{}").get()
        val identityJson =
            "{did=${request.connection?.did}, verkey=${request.connection?.didDoc?.keys?.first()?.publicKeyBase58}}"
        Did.storeTheirDid(agentContext.wallet, identityJson)

        if (request.connection?.didDoc?.services?.count()!! > 0 && request.connection?.didDoc?.services?.first() is IndyAgentDidDocService) {
            val service = request.connection?.didDoc?.services?.first() as IndyAgentDidDocService

            val endpoint = AgentEndpoint()

            endpoint.uri = service.serviceEndpoint
            endpoint.verkey =
                if (service.routingKeys.count() > 0) service.routingKeys.toTypedArray() else emptyArray()

            connection.endpoint = endpoint
        }

        val requestConnection = request.connection
        if (requestConnection != null) {
            connection.theirDid = requestConnection.did
            connection.theirVk = requestConnection.didDoc?.keys?.first()?.publicKeyBase58
        }

        val requestId = request.id ?: throw Exception("Id not found.")
        connection.myDid = my.did
        connection.myVk = my.verkey
        connection.setTag(TagConstants.LAST_THREAD_ID, requestId)

        connection.alias = ConnectionAlias()

        val requestLabel = request.label
        val connectionAlias = connection.alias

        if (requestLabel != null && connectionAlias != null) {
            if (requestLabel.isNotEmpty() && connectionAlias.name.isNullOrEmpty()) {
                connectionAlias.name = requestLabel
                connection.alias = connectionAlias
            }
        }

        val requestImage = request.imageUrl

        if (requestImage != null && connectionAlias != null) {
            if (requestImage.isNotEmpty() && connectionAlias.imageUrl.isNullOrEmpty()) {
                connectionAlias.imageUrl = requestImage
                connection.alias = connectionAlias
            }
        }

        val requestType = request.type ?: throw Exception("Type not found.")
        if (!connection.multiPartyInvitation) {
            connection.trigger(ConnectionTrigger.INVITATION_ACCEPT)
            recordService.update(wallet, connection)

            val threadId = request.getThreadId()
                ?: throw IllegalArgumentException("Request thread id not found")

            eventAggregator.publish(
                ServiceMessageProcessingEvent(
                    connection.id,
                    requestType,
                    threadId
                )
            )

            return connection.id
        }

        val newConnection = ConnectionRecord(UUID.randomUUID().toString())
        newConnection.alias = connection.alias
        newConnection.endpoint = connection.endpoint
        newConnection.tags = connection.tags
        newConnection.multiPartyInvitation = false
        newConnection.trigger(ConnectionTrigger.INVITATION_ACCEPT)
        recordService.add(wallet, newConnection)

        val threadId = request.getThreadId()
            ?: throw IllegalArgumentException("Request thread id not found")

        eventAggregator.publish(
            ServiceMessageProcessingEvent(
                newConnection.id,
                requestType,
                threadId
            )
        )

        return newConnection.id
    }

    /**
     * @see IConnectionService
     */
    override suspend fun processResponse(
        agentContext: IAgentContext,
        response: ConnectionResponseMessage,
        connection: ConnectionRecord
    ): String {
        val sig = response.connectionSig
            ?: throw IllegalArgumentException("No connection signature found")
        val connectionObj =
            SignatureUtils.unpackAndVerify<Connection>(sig)
        val identityJson =
            "{\"did\":\"${connectionObj.did}\",\"verkey\":\"${connectionObj.didDoc?.keys?.first()?.publicKeyBase58}\"}"
        Did.storeTheirDid(agentContext.wallet, identityJson)

        val threadId = response.getThreadId()
            ?: throw IllegalArgumentException("Request thread id not found")

        connection.theirDid = connectionObj.did
        connection.theirVk = connectionObj.didDoc?.keys?.first()?.publicKeyBase58
        connection.setTag(TagConstants.LAST_THREAD_ID, threadId)

        if (connectionObj.didDoc?.services?.first() is IndyAgentDidDocService) {
            val didDoc = connectionObj.didDoc
            val service = didDoc?.services?.first()
            val endpoint = AgentEndpoint()
            if (service != null) {
                endpoint.uri = service.serviceEndpoint
                endpoint.verkey =
                    if (!service.routingKeys.isNullOrEmpty()) service.routingKeys.toTypedArray() else emptyArray()
            }

            connection.endpoint = endpoint
        }

        connection.trigger(ConnectionTrigger.RESPONSE)

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        recordService.update(wallet, connection)

        val type =
            response.type
                ?: throw IllegalArgumentException("ConnectionResponseMessage has no message type")

        eventAggregator.publish(
            ServiceMessageProcessingEvent(
                recordId = connection.id,
                messageType = type,
                threadId = threadId
            )
        )

        return connection.id
    }

    /**
     * @see IConnectionService
     */
    override suspend fun createResponse(
        agentContext: IAgentContext,
        connectionId: String
    ): Pair<ConnectionResponseMessage, ConnectionRecord> {
        val connection =
            get(agentContext, connectionId)
        if (connection.state != ConnectionState.NEGOTIATING) {
            throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Connection state was invalid. Expected: '{${ConnectionState.NEGOTIATING}}', found '{${connection.state}}'"
            )
        }

        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        connection.trigger(ConnectionTrigger.REQUEST)
        recordService.update(wallet, connection)

        val provisioning =
            provisioningService.getProvisioning(wallet)
        val connectionData = Connection(
            connection.myDid,
            connection.myDidDoc(provisioning)
        )

        val sigData = SignatureUtils.signData(
            agentContext,
            connectionData,
            connection.getTag(TagConstants.CONNECTION_KEY)
        )
        val threadId = connection.getTag(TagConstants.LAST_THREAD_ID) ?: ""

        val response = ConnectionResponseMessage(agentContext.useMessageTypesHttps)
        response.connectionSig = sigData
        response.threadFrom(threadId)

        return Pair(response, connection)
    }

    /**
     * @see IConnectionService
     */
    override suspend fun get(
        agentContext: IAgentContext,
        connectionId: String
    ): ConnectionRecord {
        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        val record = recordService.get<ConnectionRecord>(
            wallet,
            connectionId,
            RecordType.CONNECTION_RECORD
        )

        if (record != null) {
            return record
        } else {
            throw AriesFrameworkException(ErrorCode.RECORD_NOT_FOUND, "Record not found")
        }
    }

    /**
     * @see IConnectionService
     */
    override suspend fun list(
        agentContext: IAgentContext,
        query: ISearchQuery?,
        count: Int,
        skip: Int
    ): List<ConnectionRecord> {
        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        val recordList = recordService.search<ConnectionRecord>(
            wallet,
            query,
            null,
            count,
            skip,
            RecordType.CONNECTION_RECORD
        )
        val connectionList = mutableListOf<ConnectionRecord>()
        for (record in recordList) {
            connectionList.add(record as ConnectionRecord)
        }
        return connectionList
    }


    /**
     * @see IConnectionService
     */
    override suspend fun delete(agentContext: IAgentContext, connectionId: String): Boolean {
        val wallet =
            agentContext.wallet ?: throw IllegalArgumentException("AgentContext has no wallet")

        return recordService.delete<ConnectionRecord>(
            wallet,
            connectionId,
            RecordType.CONNECTION_RECORD
        )
    }

    /**
     * @see IConnectionService
     */
    override suspend fun resolveByMyKey(
        agentContext: IAgentContext,
        myKey: String
    ): ConnectionRecord? {
        if (myKey.isEmpty()) {
            throw IllegalArgumentException("myKey is empty")
        }

        val connectionList = list(agentContext, count = 2_147_483_647)
        val connection = connectionList.firstOrNull { record ->
            record.myVk == myKey
        }
        return if (connection != null) {
            connection
        } else {
            val multiPartyConnection = connectionList.firstOrNull { record ->
                record.getTag(TagConstants.CONNECTION_KEY) == myKey && record.multiPartyInvitation
            }
            multiPartyConnection
        }

        //TODO: Fix Serialization for ISearchQuery
    }
}