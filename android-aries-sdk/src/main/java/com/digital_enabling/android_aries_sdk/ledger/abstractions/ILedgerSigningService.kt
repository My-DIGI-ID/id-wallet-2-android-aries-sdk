package com.digital_enabling.android_aries_sdk.ledger.abstractions

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import org.hyperledger.indy.sdk.wallet.Wallet

/**
 * Ledger Signing Service
 */
interface ILedgerSigningService {
    /**
     * Signs the outgoing request.
     * @param agentContext The agent context.
     * @param submitterDid The submitter DID.
     * @param requestJson The request json.
     */
    suspend fun signRequest(agentContext: IAgentContext, submitterDid: String, requestJson: String): String

    /**
     * Signs the outgoing request
     * @param wallet
     * @param submitterDid
     * @param requestJson
     */
    fun signRequest(wallet: Wallet, submitterDid: String, requestJson: String) : String
}