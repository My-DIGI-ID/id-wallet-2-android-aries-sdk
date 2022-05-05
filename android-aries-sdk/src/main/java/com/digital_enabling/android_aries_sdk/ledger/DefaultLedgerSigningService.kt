package com.digital_enabling.android_aries_sdk.ledger

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.configuration.IProvisioningService
import com.digital_enabling.android_aries_sdk.ledger.abstractions.ILedgerSigningService
import com.digital_enabling.android_aries_sdk.utils.IIndyWrapper
import com.digital_enabling.android_aries_sdk.utils.IndyWrapper
import org.hyperledger.indy.sdk.wallet.Wallet
import java.time.OffsetDateTime

class DefaultLedgerSigningService(
    private val provisioningService: IProvisioningService,
    private val indyWrapper: IIndyWrapper = IndyWrapper()
    ) : ILedgerSigningService {

    /**
     * @see ILedgerSigningService
     */
    override suspend fun signRequest(
        agentContext: IAgentContext,
        submitterDid: String,
        requestJson: String
    ): String {
        var appendedRequestJson: String? = null
        val wallet = agentContext.wallet ?: throw Exception("Wallet not found.")

        try {
            val provisioning = provisioningService.getProvisioning(wallet)
            val provisioningTaaAcceptance = provisioning.taaAcceptance
                ?: throw Exception("TaaAcceptance not found.")
            appendedRequestJson =
                indyWrapper.appendTxnAuthorAgreementAcceptanceToRequest(
                    requestJson,
                    provisioningTaaAcceptance.text,
                    provisioningTaaAcceptance.version,
                    provisioningTaaAcceptance.digest,
                    provisioningTaaAcceptance.acceptanceMechanism,
                    OffsetDateTime.now().toEpochSecond()
                )
        } catch (e: AriesFrameworkException) {

        }
        return indyWrapper.signRequest(
            wallet,
            submitterDid,
            appendedRequestJson ?: requestJson
        )
    }

    /**
     * @see ILedgerSigningService
     */
    override fun signRequest(
        wallet: Wallet,
        submitterDid: String,
        requestJson: String
    ): String {
        return indyWrapper.signRequest(wallet, submitterDid, requestJson)
    }
}