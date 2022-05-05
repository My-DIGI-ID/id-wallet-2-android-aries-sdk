package com.digital_enabling.android_aries_sdk.configuration

import com.digital_enabling.android_aries_sdk.wallet.models.WalletConfiguration
import com.digital_enabling.android_aries_sdk.wallet.models.WalletCredentials
import kotlinx.serialization.Serializable

/**
 * Agent options
 */
@Serializable
class AgentOptions {
    /**
     * The wallet configuration.
     */
    var walletConfiguration = WalletConfiguration(id = "DefaultWallet")

    /**
     * The wallet credentials.
     */
    var walletCredentials = WalletCredentials("DefaultKey")

    /**
     * The DID of the issuer key pair.
     */
    var issuerDid: String = ""

    /**
     * The issuer key generation seed.
     */
    var issuerKeySeed: String = ""

    /**
     * The agent DID.
     */
    var agentDid: String = ""

    /**
     * The agent key generation seed.
     */
    var agentKeySeed: String = ""

    /**
     * Agent endpoint uri.
     */
    var endpointUri: String = ""
    /**
     * The agent name used in connection invitations.
     */
    var agentName: String = ""

    /**
     * The agent image uri.
     */
    var agentImageUri: String = ""

    /**
     * The verification key of the agent.
     */
    var agentKey: String = ""

    /**
     * The name of the pool.
     */
    var poolName: String = "DefaultPool"

    /**
     * The genesis filename.
     */
    var genesisFilenameFilter: String = ""

    /**
     * The protocol version of the nodes.
     */
    var protocolVersion: Int = 2

    /**
     * The revocation registry base URI path.
     */
    var revocationRegistryUriPath: String = "/tails"

    /**
     * The revocation registry directory.
     *
     * Gets or sets the revocation registry directory where revocation tails files will be stored. The default path is ~/.indy_client/tails
     */
    var revocationRegistryDirectory: String = ""

    /**
     * The backup directory.
     *
     * The backup directory where physical backups will be stored. This property is only used when running a mediator service.
     */
    var backupDirectory: String = ""

    /**
     * Automatically respond to credential offers with a credential request. Default: [false]
     */
    var autoRespondCredentialOffer: Boolean = false

    /**
     * Automatically respond to credential request with corresponding credentials. Default: [false]
     */
    var autoRespondCredentialRequest: Boolean = false

    /**
     * True if to use UseMessageTypesHttps.
     */
    var useMessageTypesHttps: Boolean = false

    /**
     * The value for Metadata dictionary.
     * This dictionary can be used with InboxCreation on the mediator agent.
     * Data is stored under InboxRecord tags on the mediator agent.
     */
    var metaData: Map<String, String> = HashMap()
}