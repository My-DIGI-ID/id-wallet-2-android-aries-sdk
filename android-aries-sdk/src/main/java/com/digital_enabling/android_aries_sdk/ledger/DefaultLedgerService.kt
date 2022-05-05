package com.digital_enabling.android_aries_sdk.ledger

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.ledger.abstractions.ILedgerService
import com.digital_enabling.android_aries_sdk.ledger.abstractions.ILedgerSigningService
import com.digital_enabling.android_aries_sdk.ledger.models.AuthorizationRule
import com.digital_enabling.android_aries_sdk.payments.models.IndyPaymentInputSource
import com.digital_enabling.android_aries_sdk.payments.models.IndyPaymentOutputSource
import com.digital_enabling.android_aries_sdk.payments.models.TransactionCost
import com.digital_enabling.android_aries_sdk.utils.DidUtils
import com.digital_enabling.android_aries_sdk.utils.IIndyWrapper
import com.digital_enabling.android_aries_sdk.utils.IndyWrapper
import com.digital_enabling.android_aries_sdk.utils.ResilienceUtils
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import org.hyperledger.indy.sdk.ledger.LedgerResults
import kotlin.Exception

/**
 * Initializes a new instance of the [DefaultLedgerService] class.
 * @param signingService The Signing Service.
 */
class DefaultLedgerService(
    private val signingService: ILedgerSigningService,
    private val indyWrapper: IIndyWrapper = IndyWrapper()
) : ILedgerService {

    //region Implemented Interface
    /**
     * @see ILedgerService
     */
    override fun lookupAuthorizationRules(agentContext: IAgentContext): List<AuthorizationRule> {
        val pool = agentContext.pool ?: throw Exception("Pool not found.")
        val req = indyWrapper.buildGetAuthRuleRequest(null, null, null, null, null, null)
        val res = indyWrapper.submitRequest(pool.getAwaiter(), req)
        ensureSuccessResponse(res)
        val json = Json.encodeToJsonElement(res).jsonObject

        val data = json["result"]?.jsonObject?.get("data")?.jsonObject?.toList()
        return emptyList()
    }

    /**
     * @see ILedgerService
     */
    override fun lookupAttribute(
        agentContext: IAgentContext,
        targetDid: String,
        attributeName: String
    ): String {
        val req = indyWrapper.buildGetAttribRequest(null, targetDid, attributeName, null, null)
        val pool = agentContext.pool ?: throw Exception("Pool not found.")
        return indyWrapper.submitRequest(pool.getAwaiter(), req)
    }

    /**
     * @see ILedgerService
     */
    override suspend fun registerAttribute(
        agentContext: IAgentContext,
        submittedDid: String,
        targetDid: String,
        attributeName: String,
        value: Any,
        paymentInfo: TransactionCost?
    ) {
        val data = "{{\"{$attributeName}\": {${Json.encodeToString(value)}}}}"
        val req = indyWrapper.buildAttribRequest(submittedDid, targetDid, null, data, null)
        signAndSubmit(agentContext, submittedDid, req, paymentInfo)
    }

    /**
     * @see ILedgerService
     */
    override fun lookupSchema(
        agentContext: IAgentContext,
        schemaId: String
    ): LedgerResults.ParseResponseResult {

        fun lookupSchema(): LedgerResults.ParseResponseResult {
            val req = indyWrapper.buildGetSchemaRequest(null, schemaId)
            val pool = agentContext.pool ?: throw Exception("Pool not found.")
            val res = indyWrapper.submitRequest(pool.getAwaiter(), req)
            ensureSuccessResponse(res)
            return indyWrapper.parseGetSchemaResponse(res)
        }

        return ResilienceUtils.retryPolicy(
            { lookupSchema() },
            {
                Pair<Exception?, Boolean>(
                    null,
                    false
                )
            }) // TODO: Implement proper ExceptionPredicate for Resilience Framework.
    }

    /**
     * @see ILedgerService
     */
    override fun lookupNym(agentContext: IAgentContext, did: String): String {
        val req = indyWrapper.buildGetNymRequest(null, did)
        val pool = agentContext.pool ?: throw Exception("Pool not found.")
        val res = indyWrapper.submitRequest(pool.getAwaiter(), req)
        ensureSuccessResponse(res)
        return res
    }

    /**
     * @see ILedgerService
     */
    override fun lookupTransaction(
        agentContext: IAgentContext,
        ledgerType: String?,
        sequenceId: Int
    ): String {
        val req = indyWrapper.buildGetTxnRequest(null, ledgerType, sequenceId)
        val pool = agentContext.pool ?: throw Exception("Pool not found.")
        return indyWrapper.submitRequest(pool.getAwaiter(), req)
    }

    /**
     * @see ILedgerService
     */
    override fun lookupDefinition(
        agentContext: IAgentContext,
        definitionId: String
    ): LedgerResults.ParseResponseResult {

        fun lookupDefinition(): LedgerResults.ParseResponseResult {
            val req = indyWrapper.buildGetCredDefRequest(null, definitionId)
            val pool = agentContext.pool ?: throw Exception("Pool not found.")
            val res = indyWrapper.submitRequest(pool.getAwaiter(), req)
            return indyWrapper.parseGetCredDefResponse(res)
        }

        return ResilienceUtils.retryPolicy({ lookupDefinition() },
            {
                Pair<Exception?, Boolean>(
                    null,
                    false
                )
            }) // TODO: Implement proper ExceptionPredicate for Resilience Framework.
    }

    /**
     * @see ILedgerService
     */
    override fun lookupRevocationRegistryDefinition(
        agentContext: IAgentContext,
        registryId: String
    ): LedgerResults.ParseResponseResult {
        val req =
            indyWrapper.buildGetRevocRegDefRequest(null, registryId)
        val pool = agentContext.pool ?: throw Exception("Pool not found.")
        val res = indyWrapper.submitRequest(
            pool.getAwaiter(),
            req
        )
        return indyWrapper.parseGetRevocRegDefResponse(res)
    }

    /**
     * @see ILedgerService
     */
    override fun lookupRevocationRegistryDelta(
        agentContext: IAgentContext,
        revocationRegistryId: String,
        from: Long,
        to: Long
    ): LedgerResults.ParseRegistryResponseResult {
        val req = indyWrapper.buildGetRevocRegDeltaRequest(null, revocationRegistryId, from, to)
        val pool = agentContext.pool ?: throw Exception("Pool not found.")
        val res = indyWrapper.submitRequest(pool.getAwaiter(), req)
        ensureSuccessResponse(res)
        return indyWrapper.parseGetRevocRegDeltaResponse(res)
    }

    /**
     * @see ILedgerService
     */
    override fun lookupRevocationRegistry(
        agentContext: IAgentContext,
        revocationRegistryId: String,
        timestamp: Long
    ): LedgerResults.ParseRegistryResponseResult {
        val req = indyWrapper.buildGetRevocRegRequest(null, revocationRegistryId, timestamp)
        val pool = agentContext.pool ?: throw Exception("Pool not found.")
        val res = indyWrapper.submitRequest(pool.getAwaiter(), req)
        ensureSuccessResponse(res)
        return indyWrapper.parseGetRevocRegResponse(res)
    }

    /**
     * @see ILedgerService
     */
    override suspend fun registerNym(
        agentContext: IAgentContext,
        submitterDid: String,
        theirDid: String,
        theirVerkey: String,
        role: String,
        paymentInfo: TransactionCost?
    ) {
        val newTheirVerkey = if (DidUtils.isFullVerkey(theirVerkey)) {
            org.hyperledger.indy.sdk.did.Did.AbbreviateVerkey(theirDid, theirVerkey).get()
        } else {
            theirVerkey
        }
        val req = indyWrapper.buildNymRequest(submitterDid, theirDid, newTheirVerkey, null, role)
        signAndSubmit(agentContext, submitterDid, req, paymentInfo)
    }

    /**
     * @see ILedgerService
     */
    override suspend fun registerCredentialDefinition(
        agentContext: IAgentContext,
        submitterDid: String,
        data: String,
        paymentInfo: TransactionCost?
    ) {
        val req = indyWrapper.buildCredDefRequest(submitterDid, data)
        signAndSubmit(agentContext, submitterDid, req, paymentInfo)
    }

    /**
     * @see ILedgerService
     */
    override suspend fun registerRevocationRegistryDefinition(
        agentContext: IAgentContext,
        submitterDid: String,
        data: String,
        paymentInfo: TransactionCost?
    ) {
        val req = indyWrapper.buildRevocRegDefRequest(submitterDid, data)
        signAndSubmit(agentContext, submitterDid, req, paymentInfo)
    }

    /**
     * @see ILedgerService
     */
    override suspend fun sendRevocationRegistryEntry(
        agentContext: IAgentContext,
        issuerDid: String,
        revocationRegistryDefinitionId: String,
        revocationDefinitionType: String,
        value: String,
        paymentInfo: TransactionCost?
    ) {
        val req = indyWrapper.buildRevocRegEntryRequest(
            issuerDid,
            revocationRegistryDefinitionId,
            revocationDefinitionType,
            value
        )
        val res = signAndSubmit(agentContext, issuerDid, req, paymentInfo)
        ensureSuccessResponse(res)
    }

    /**
     * @see ILedgerService
     */
    override suspend fun registerSchema(
        agentContext: IAgentContext,
        issuerDid: String,
        schemaJson: String,
        paymentInfo: TransactionCost?
    ) {
        val req = indyWrapper.buildCredDefRequest(issuerDid, schemaJson)
        signAndSubmit(agentContext, issuerDid, req, paymentInfo)
    }
    //endregion

    //region Helper methods.
    private suspend fun signAndSubmit(
        agentContext: IAgentContext,
        submitterDid: String,
        request: String,
        paymentInfo: TransactionCost?
    ): String {
        var newRequest = ""
        val wallet = agentContext.wallet ?: throw Exception("Wallet not found.")
        if (paymentInfo != null) {
            val address =
                paymentInfo.paymentAddress.address ?: throw Exception("Address not found.")
            val requestWithFees = indyWrapper.addRequestFees(
                wallet,
                null,
                request,
                Json.encodeToString(paymentInfo.paymentAddress.sources),
                Json.encodeToString(
                    listOf(
                        IndyPaymentOutputSource(
                            recipient = address,
                            amount = paymentInfo.paymentAddress.balance - paymentInfo.amount,
                            receipt = "",
                            extra = ""
                        )
                    )
                ),
                null
            )
            newRequest = requestWithFees.reqWithFeesJson
        }

        val signedRequest = signingService.signRequest(agentContext, submitterDid, newRequest)
        val pool = agentContext.pool ?: throw Exception("Pool not found.")
        val response = indyWrapper.submitRequest(pool.getAwaiter(), signedRequest)

        ensureSuccessResponse(response)

        if (paymentInfo != null) {
            val responsePayment = indyWrapper.parseResponseWithFees(
                paymentInfo.paymentMethod,
                response
            )
            val paymentOutputs =
                Json.decodeFromString<List<IndyPaymentOutputSource>>(responsePayment)

            val inputList: MutableList<IndyPaymentInputSource> = mutableListOf()
            for (outputSource in paymentOutputs.filter { output -> output.recipient == paymentInfo.paymentAddress.address }) {
                inputList.add(
                    IndyPaymentInputSource(
                        amount = outputSource.amount,
                        paymentAddress = outputSource.receipt,
                        source = outputSource.receipt,
                        extra = ""
                    )
                )
            }
            paymentInfo.paymentAddress.sources = inputList
        }
        return response
    }

    private fun ensureSuccessResponse(res: String) {
        val response = Json.decodeFromString<JsonObject>(res)
        if (response["op"].toString().lowercase() != "reply") {
            throw AriesFrameworkException(
                ErrorCode.LEDGER_OPERATION_REJECTED,
                "Ledger operation rejected"
            )
        }
    }
    //endregion
}