package com.digital_enabling.android_aries_sdk.transaction

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.credential.abstractions.ICredentialService
import com.digital_enabling.android_aries_sdk.credential.models.OfferConfiguration
import com.digital_enabling.android_aries_sdk.didexchange.IConnectionService
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionInvitationMessage
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionState
import com.digital_enabling.android_aries_sdk.didexchange.models.InviteConfiguration
import com.digital_enabling.android_aries_sdk.messagedispatcher.IMessageService
import com.digital_enabling.android_aries_sdk.proof.IProofService
import com.digital_enabling.android_aries_sdk.proof.models.ProofRequest
import com.digital_enabling.android_aries_sdk.transaction.abstractions.ITransactionService
import com.digital_enabling.android_aries_sdk.transaction.models.TransactionInfo
import com.digital_enabling.android_aries_sdk.transaction.models.TransactionRecord
import com.digital_enabling.android_aries_sdk.transaction.models.TransactionResponseMessage
import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.utils.TagConstants
import com.digital_enabling.android_aries_sdk.wallet.IWalletRecordService
import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperledger.indy.sdk.wallet.Wallet
import java.net.URI
import java.util.*
import kotlin.collections.HashMap

class DefaultTransactionService(
    private val connectionService: IConnectionService,
    private val credentialService: ICredentialService,
    //private val logger: Logger,
    private val messageService: IMessageService,
    private val proofService: IProofService,
    private val recordService: IWalletRecordService
) : ITransactionService {

    private val sessionIdTag = "t_o"
    private val invitationMessageTag = "c_i"
    private val waitConnectionTag = "waitconnection"
    private val waitProofTag = "waitproof"

    //region Interface implementation.
    override suspend fun createOrUpdateTransaction(
        agentContext: IAgentContext,
        transactionId: String?,
        connectionId: String?,
        offerConfiguration: OfferConfiguration?,
        proofRequest: ProofRequest?,
        connectionConfig: InviteConfiguration?
    ): Triple<TransactionRecord, ConnectionRecord, ConnectionInvitationMessage> {
        // Logger in dotnet.

        val connectionMessage: ConnectionInvitationMessage
        val connectionRecord: ConnectionRecord
        val wallet = agentContext.wallet ?: throw Exception("Wallet is null")
        var myConnectionConfig = connectionConfig
        if (connectionId.isNullOrEmpty()) {
            if (myConnectionConfig == null) {
                myConnectionConfig = InviteConfiguration()
            }
            myConnectionConfig.autoAcceptConnection = true
            myConnectionConfig.multiPartyInvitation = true

            val connection = connectionService.createInvitation(agentContext, connectionConfig)
            connectionMessage = connection.first
            connectionRecord = connection.second
            connectionRecord.setTag("InvitationMessage", Json.encodeToString(connectionMessage))
            recordService.update(wallet, connectionRecord)
        } else {
            connectionRecord = recordService.get<ConnectionRecord>(
                wallet,
                connectionId,
                RecordType.CONNECTION_RECORD
            ) as ConnectionRecord

            val message = connectionRecord.getTag("InvitationMessage") ?: ""
            connectionMessage = Json.decodeFromString(message) as ConnectionInvitationMessage
        }

        val myTransactionId = if (!transactionId.isNullOrEmpty()) {
            transactionId
        } else {
            UUID.randomUUID().toString()
        }

        var transactionRecord: TransactionRecord? = recordService.get<TransactionRecord>(
            wallet,
            myTransactionId,
            RecordType.TRANSACTION_RECORD
        ) as TransactionRecord
        if (transactionRecord == null) {
            transactionRecord = TransactionRecord(myTransactionId)
            transactionRecord.connectionId = connectionRecord.id
            transactionRecord.offerConfiguration = offerConfiguration
            transactionRecord.proofRequest = proofRequest
            recordService.add(wallet, transactionRecord)
        } else {
            transactionRecord.connectionId = connectionRecord.id
            transactionRecord.offerConfiguration = offerConfiguration
            transactionRecord.proofRequest = proofRequest
            recordService.update(wallet, transactionRecord)
        }

        return Triple(transactionRecord, connectionRecord, connectionMessage)
    }

    override suspend fun delete(agentContext: IAgentContext, transactionId: String) {
        recordService.delete<TransactionRecord>(
            agentContext.wallet ?: throw IllegalArgumentException("Wallet is null"),
            transactionId,
            RecordType.TRANSACTION_RECORD
        )
    }

    override suspend fun get(
        agentContext: IAgentContext,
        transactionId: String
    ): TransactionRecord {
        return recordService.get<TransactionRecord>(
            agentContext.wallet ?: throw IllegalArgumentException("Wallet is null"),
            transactionId,
            RecordType.TRANSACTION_RECORD
        ) as TransactionRecord
    }

    override suspend fun list(
        agentContext: IAgentContext,
        query: ISearchQuery?,
        count: Int,
        skip: Int,
    ): List<TransactionRecord> {
        return recordService.search<TransactionRecord>(
            agentContext.wallet ?: throw IllegalArgumentException("Wallet is null"),
            query,
            null,
            count,
            skip,
            RecordType.TRANSACTION_RECORD
        )
    }

    override suspend fun processTransaction(
        agentContext: IAgentContext,
        connectionTransactionMessage: TransactionResponseMessage,
        connection: ConnectionRecord
    ) {
        // logger in dotnet.
        val wallet: Wallet = agentContext.wallet ?: throw IllegalArgumentException("Wallet is null")
        val transaction = connectionTransactionMessage.transaction
            ?: throw IllegalArgumentException("Transaction is null")

        val transactionRecord = recordService.get<TransactionRecord>(
            wallet,
            transaction,
            RecordType.TRANSACTION_RECORD
        ) as TransactionRecord
        transactionRecord.connectionRecord = connection

        if (transactionRecord.proofRequest != null) {
            val (message, record) = proofService.createRequest(
                agentContext,
                transactionRecord.proofRequest!!
            )
            transactionRecord.proofRecordId = record.id
            val deleteId = UUID.randomUUID().toString()
            message.addDecorator(deleteId, "delete_id")
            record.setTag("delete_id", deleteId)
            record.connectionId = connection.id

            recordService.update(wallet, record)
            messageService.send(agentContext, message, connection)
        }

        if (transactionRecord.offerConfiguration != null) {
            val (credentialOfferMessage, credentialRecord) = credentialService.createOffer(
                agentContext,
                transactionRecord.offerConfiguration!!,
                connection.id
            )

            transactionRecord.credentialRecordId = credentialRecord.id

            messageService.send(agentContext, credentialOfferMessage, connection)
        }

        recordService.update(wallet, transactionRecord)
    }

    override suspend fun readTransactionUrl(invitationUrl: String): TransactionInfo {
        var sessionId: String? = null
        var invitationMessage: ConnectionInvitationMessage? = null
        var awaitableConnection = false
        var awaitableProof = false

        val uri: URI
        try {
            uri = URI(invitationUrl)
        } catch (e: Exception) {
            return TransactionInfo(null, null, awaitableConnection = false, awaitableProof = false)
        }

        try {
            if (uri.query.startsWith("?$sessionIdTag=")) {
                val arguments: HashMap<String, String> = HashMap()
                val params = uri.query.subSequence(1, uri.query.length).split("&")
                for (param in params) {
                    val q = param.split("=")
                    arguments[q.first().toString()] = q[1]
                }

                sessionId = arguments[sessionIdTag]
                invitationMessage = Base64.getDecoder()
                    .decode(arguments[invitationMessageTag]) as ConnectionInvitationMessage

                awaitableConnection = try {
                    arguments[waitConnectionTag].toBoolean()
                } catch (e: Exception) {
                    false
                }

                awaitableProof = try {
                    arguments[waitProofTag].toBoolean()
                } catch (e: Exception) {
                    false
                }
                return TransactionInfo(
                    sessionId,
                    invitationMessage,
                    awaitableConnection,
                    awaitableProof
                )
            }
        } catch (e: Exception) {
            // ignore.
        }

        return TransactionInfo(sessionId, invitationMessage, awaitableConnection, awaitableProof)
    }

    override suspend fun sendTransactionResponse(
        agentContext: IAgentContext,
        transactionId: String,
        connection: ConnectionRecord
    ) {
        val message = TransactionResponseMessage()
        message.transaction = transactionId

        try {
            messageService.send(agentContext, message, connection)
        } catch (e: Exception) {
            // ignore.
        }
    }
//endregion

    suspend fun checkForExistingConnection(
        agentContext: IAgentContext,
        connectionInvitationMessage: ConnectionInvitationMessage,
        awaitableConnection: Boolean = false
    ): ConnectionRecord {
        val transactionConnections = connectionService.list(agentContext, null, 2147483647)

        if (!awaitableConnection) {
            val transactionConnectionsEndpoints =
                transactionConnections.filter { it.endpoint?.uri == connectionInvitationMessage.serviceEndpoint && it.state == ConnectionState.CONNECTED }
            return transactionConnectionsEndpoints.filter { it.endpoint?.verkey == connectionInvitationMessage.routingKeys }
                .sortedByDescending { it.createdAtUtc }.first()
        } else {
            val transactionConnectionEndpoints =
                transactionConnections.filter {
                    it.endpoint?.uri == connectionInvitationMessage.serviceEndpoint && it.state == ConnectionState.CONNECTED && it.getTag(
                        TagConstants.RECIPIENT_KEYS
                    ) != null
                }
            return transactionConnectionEndpoints.filter {
                it.endpoint?.verkey == connectionInvitationMessage.routingKeys && it.getTag(
                    TagConstants.RECIPIENT_KEYS
                ) == Json.encodeToString(connectionInvitationMessage.recipientKeys)
            }.sortedByDescending { it.createdAtUtc }.first()
        }
    }
}