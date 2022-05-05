package com.digital_enabling.android_aries_sdk.configuration

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.AgentEndpoint
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.ledger.models.IndyTaa
import org.hyperledger.indy.sdk.wallet.Wallet

interface IProvisioningService {

    /**
     * Returns the agent provisioning record. This is a single record that contains all agent configuration parameters.
     * @param wallet The Wallet.
     * @throws AriesFrameworkException Throws with ErrorCode.RecordNotFound.
     * @return The provisioning record.
     */
    suspend fun getProvisioning(wallet: Wallet): ProvisioningRecord

    /**
     * Creates a wallet and provisions a new agent with the default [AgentOptions]
     */
    suspend fun provisionAgent()

    /**
     * Creates a wallet and provisions a new agent with the specified [AgentOptions]
     * @param agentOptions
     */
    suspend fun provisionAgent(agentOptions: AgentOptions)

    /**
     * Updates the agent endpoint information.
     * @param wallet The wallet.
     * @param endpoint The endpoint.
     */
    suspend fun updateEndpoint(wallet: Wallet, endpoint: AgentEndpoint)

    /**
     * Accepts the transaction author agreement
     * @param agentContext
     * @param txnAuthorAgreement
     * @param acceptMechanism
     */
    suspend fun acceptTxnAuthorAgreement(agentContext: IAgentContext, txnAuthorAgreement: IndyTaa, acceptMechanism: String = "service_agreement")
}