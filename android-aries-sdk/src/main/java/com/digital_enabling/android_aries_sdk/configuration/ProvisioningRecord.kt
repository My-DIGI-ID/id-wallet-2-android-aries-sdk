package com.digital_enabling.android_aries_sdk.configuration

import com.digital_enabling.android_aries_sdk.agents.models.AgentEndpoint
import com.digital_enabling.android_aries_sdk.agents.models.AgentOwner
import com.digital_enabling.android_aries_sdk.decorators.ServiceDecorator
import com.digital_enabling.android_aries_sdk.ledger.models.IndyTaaAcceptance
import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Represents a provisioning record in the agency wallet
 */
@Serializable
class ProvisioningRecord() : RecordBase() {

    companion object {
        /**
         * Record Identifier
         */
        const val UNIQUE_RECORD_ID: String = "SingleRecord"
    }

    override val id = UNIQUE_RECORD_ID
    override var typeName = RecordType.PROVISIONING_RECORD.typeName

    /**
     * The endpoint information for the provisioned agent.
     */
    var endpoint: AgentEndpoint = AgentEndpoint()

    /**
     * The owner information for the provisioned agent.
     */
    var owner: AgentOwner = AgentOwner()

    /**
     * The issuer did for the provisioned agent.
     */
    @SerialName("_issuerDid")
    var issuerDid: String? = null

    /**
     * The issuer verkey for the provisioned agent.
     */
    @SerialName("_issuerVerkey")
    var issuerVerkey: String? = null

    /**
     * The master key identifier for the provisioned agent.
     */
    @SerialName("_masterSecretId")
    var masterSecretId: String? = null

    /**
     * The tails base uri for the provisioned agent.
     */
    @SerialName("_tailsBaseUri")
    var tailsBaseUri: String? = null

    /**
     * True if to use UseMessageTypesHttps.
     *
     * Only affects messages created by the default services.
     */
    @SerialName("_useMessageTypesHttps")
    var useMessageTypesHttps: Boolean = false

    var defaultPaymentAddressId: String? = null

    /**
     * The issuer seed for deterministic DID generation.
     */
    var issuerSeed: String? = null

    /**
     * The [IndyTaaAcceptance] for this agent.
     */
    var taaAcceptance: IndyTaaAcceptance? = null

    fun toServiceDecorator(): ServiceDecorator {
        val result = ServiceDecorator()

        result.serviceEndpoint = this.endpoint.uri
        result.routingKeys = this.endpoint.verkey?.toMutableList()
        result.recipientKeys = mutableListOf(this.issuerVerkey.toString())

        return result
    }

    override fun toJson(): String =
        Json.encodeToString(this)
}