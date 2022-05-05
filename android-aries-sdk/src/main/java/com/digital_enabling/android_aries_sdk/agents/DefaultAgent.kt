package com.digital_enabling.android_aries_sdk.agents

import com.digital_enabling.android_aries_sdk.basicmessage.DefaultBasicMessageHandler
import com.digital_enabling.android_aries_sdk.credential.DefaultCredentialHandler
import com.digital_enabling.android_aries_sdk.didexchange.DefaultConnectionHandler
import com.digital_enabling.android_aries_sdk.didexchange.IConnectionService
import com.digital_enabling.android_aries_sdk.discovery.DefaultDiscoveryHandler
import com.digital_enabling.android_aries_sdk.messagedispatcher.IMessageService
import com.digital_enabling.android_aries_sdk.proof.DefaultProofHandler
import com.digital_enabling.android_aries_sdk.routing.DefaultForwardHandler
import com.digital_enabling.android_aries_sdk.trustping.DefaultTrustPingMessageHandler

/**
 * Default agent.
 */
class DefaultAgent(
    connectionService: IConnectionService,
    messageService: IMessageService,
    connectionHandler: DefaultConnectionHandler,
    credentialHandler: DefaultCredentialHandler,
    proofHandler: DefaultProofHandler,
    trustPingMessageHandler: DefaultTrustPingMessageHandler,
    forwardHandler: DefaultForwardHandler,
    basicMessageHandler: DefaultBasicMessageHandler,
    discoveryHandler: DefaultDiscoveryHandler,
) : AgentBase(
    connectionService,
    messageService,
    connectionHandler,
    credentialHandler,
    proofHandler,
    trustPingMessageHandler,
    forwardHandler,
    basicMessageHandler,
    discoveryHandler
) {
    /**
     * Configure the handlers.
     */
    override fun configureHandlers() {
        addConnectionHandler()
        addCredentialHandler()
        addTrustPingHandler()
        addProofHandler()
        addForwardHandler()
        addBasicMessageHandler()
        addDiscoveryHandler()
    }
}