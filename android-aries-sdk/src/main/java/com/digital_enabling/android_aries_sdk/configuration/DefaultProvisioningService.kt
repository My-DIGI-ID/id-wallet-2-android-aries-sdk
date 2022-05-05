package com.digital_enabling.android_aries_sdk.configuration

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.AgentEndpoint
import com.digital_enabling.android_aries_sdk.agents.models.AgentOwner
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.ledger.models.IndyTaa
import com.digital_enabling.android_aries_sdk.ledger.models.IndyTaaAcceptance
import com.digital_enabling.android_aries_sdk.utils.CredentialUtils.Companion.sha256
import com.digital_enabling.android_aries_sdk.utils.CryptoUtils
import com.digital_enabling.android_aries_sdk.utils.IndyWrapper
import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.wallet.IWalletRecordService
import com.digital_enabling.android_aries_sdk.wallet.IWalletService
import org.hyperledger.indy.sdk.wallet.Wallet
import java.net.URI
import java.time.OffsetDateTime

class DefaultProvisioningService(
    private val recordService: IWalletRecordService,
    private val walletService: IWalletService,
    private val agentOptions: AgentOptions,
    private val indyWrapper: IndyWrapper = IndyWrapper()
) : IProvisioningService {

    //region Interface implementation
    /**
     * @see IProvisioningService
     */
    override suspend fun getProvisioning(wallet: Wallet): ProvisioningRecord {
        return recordService.get(
            wallet,
            ProvisioningRecord.UNIQUE_RECORD_ID,
            RecordType.PROVISIONING_RECORD
        )
            ?: throw AriesFrameworkException(
                ErrorCode.RECORD_NOT_FOUND,
                "Provisioning record not found"
            )
    }

    /**
     * @see IProvisioningService
     */
    override suspend fun provisionAgent() {
        provisionAgent(agentOptions)
    }

    /**
     * @see IProvisioningService
     */
    override suspend fun provisionAgent(agentOptions: AgentOptions) {
        walletService.createWallet(
            agentOptions.walletConfiguration,
            agentOptions.walletCredentials
        )
        val wallet = walletService.getWallet(
            agentOptions.walletConfiguration,
            agentOptions.walletCredentials
        )

        val endpoint = AgentEndpoint()
        endpoint.uri = agentOptions.endpointUri
        if (agentOptions.agentKeySeed.isNotEmpty()) {
            val agent = indyWrapper.createAndStoreMyDid(
                wallet,
                "{\"seed\": \"${agentOptions.agentKeySeed}\"}"
            )
            endpoint.did = agent.did
            endpoint.verkey = arrayOf(agent.verkey)
        } else if (agentOptions.agentKey.isNotEmpty()) {
            endpoint.did = agentOptions.agentDid
            endpoint.verkey = arrayOf(agentOptions.agentKey)
        } else {
            val agent = indyWrapper.createAndStoreMyDid(wallet, "{}")
            endpoint.did = agent.did
            endpoint.verkey = arrayOf(agent.verkey)
        }


        val masterSecredId =
            indyWrapper.proverCreateMasterSecret(wallet, null)

        val record = ProvisioningRecord()
        record.masterSecretId = masterSecredId
        record.endpoint = endpoint
        record.owner = AgentOwner(agentOptions.agentName, agentOptions.agentImageUri)

        if (agentOptions.issuerKeySeed.isEmpty()) {
            val cryptoUtils = CryptoUtils()
            agentOptions.issuerKeySeed = cryptoUtils.getUniqueKey(32)
        }

        val issuer = indyWrapper.createAndStoreMyDid(
            wallet,
            "{\"did\": \"${agentOptions.issuerDid}\", \"seed\": \"${agentOptions.issuerKeySeed}\"}"
        )

        record.issuerSeed = agentOptions.issuerKeySeed
        record.issuerDid = issuer.did
        record.issuerVerkey = issuer.verkey
        record.tailsBaseUri = if (agentOptions.endpointUri.isNotEmpty()) {
            URI(agentOptions.endpointUri + "/tails/").toString()
        } else {
            ""
        }
        record.useMessageTypesHttps = agentOptions.useMessageTypesHttps
        record.setTag("AgentKeySeed", agentOptions.agentKeySeed)
        record.setTag("IssuerKeySeed", agentOptions.issuerKeySeed)

        recordService.add(wallet, record)
    }

    /**
     * @see IProvisioningService
     */
    override suspend fun updateEndpoint(wallet: Wallet, endpoint: AgentEndpoint) {
        val record = getProvisioning(wallet)
        record.endpoint = endpoint
        recordService.update(wallet, record)
    }

    /**
     * @see IProvisioningService
     */
    override suspend fun acceptTxnAuthorAgreement(
        agentContext: IAgentContext,
        txnAuthorAgreement: IndyTaa,
        acceptMechanism: String
    ) {
        val wallet = agentContext.wallet
        if(wallet != null){
            val provisioning = getProvisioning(wallet)
            provisioning.taaAcceptance = IndyTaaAcceptance(
                digest = getDigest(txnAuthorAgreement),
                _text = txnAuthorAgreement.text,
                _version = txnAuthorAgreement.version,
                acceptanceDate = OffsetDateTime.now().toEpochSecond(),
                acceptanceMechanism = acceptMechanism
            )
            recordService.update(wallet, provisioning)
        }
    }
    //endregion

    //region Helper functions
    @ExperimentalUnsignedTypes
    private fun getDigest(taa: IndyTaa): String {
        return "${taa.version}${taa.text}".sha256().encodeToByteArray().toHexString()
    }

    @ExperimentalUnsignedTypes
    fun ByteArray.toHexString() = asUByteArray().joinToString("") { it.toString(16).padStart(2, '0') }
    //endregion
}