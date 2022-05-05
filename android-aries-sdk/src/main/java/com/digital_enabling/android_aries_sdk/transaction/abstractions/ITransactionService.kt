package com.digital_enabling.android_aries_sdk.transaction.abstractions

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.credential.models.OfferConfiguration
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionInvitationMessage
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.didexchange.models.InviteConfiguration
import com.digital_enabling.android_aries_sdk.proof.models.ProofRequest
import com.digital_enabling.android_aries_sdk.transaction.models.TransactionInfo
import com.digital_enabling.android_aries_sdk.transaction.models.TransactionRecord
import com.digital_enabling.android_aries_sdk.transaction.models.TransactionResponseMessage
import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery

interface ITransactionService {

    suspend fun createOrUpdateTransaction(
        agentContext: IAgentContext,
        transactionId: String? = null,
        connectionId: String? = null,
        offerConfiguration: OfferConfiguration? = null,
        proofRequest: ProofRequest? = null,
        connectionConfig: InviteConfiguration? = null
    ): Triple<TransactionRecord, ConnectionRecord, ConnectionInvitationMessage>

    suspend fun delete(agentContext: IAgentContext, transactionId: String)

    suspend fun get(agentContext: IAgentContext, transactionId: String): TransactionRecord

    suspend fun list(
        agentContext: IAgentContext,
        query: ISearchQuery? = null,
        count: Int = 100,
        skip: Int = 0
    ): List<TransactionRecord>

    suspend fun processTransaction(
        agentContext: IAgentContext,
        connectionTransactionMessage: TransactionResponseMessage,
        connection: ConnectionRecord
    )

    suspend fun readTransactionUrl(invitationUrl: String): TransactionInfo

    suspend fun sendTransactionResponse(agentContext: IAgentContext, transactionId: String, connection: ConnectionRecord)
}