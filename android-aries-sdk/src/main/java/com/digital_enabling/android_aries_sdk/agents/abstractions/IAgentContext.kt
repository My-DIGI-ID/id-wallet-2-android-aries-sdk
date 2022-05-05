package com.digital_enabling.android_aries_sdk.agents.abstractions

import org.hyperledger.indy.sdk.wallet.Wallet
import java.util.*
import com.digital_enabling.android_aries_sdk.agents.MessageType
import com.digital_enabling.android_aries_sdk.ledger.models.PoolAwaitable

/**
 * Represents an agent context.
 */
interface IAgentContext {
    /**
     * The agent wallet.
     */
    var wallet: Wallet?

    /**
     * The pool.
     */
    var pool: PoolAwaitable?

    /**
     * The state.
     */
    var state: Dictionary<String, String?>?

    /**
     * Supported messages of the current agent.
     */
    var supportedMessages: List<MessageType>?

    /**
     * True if to use UseMessageTypesHttps.
     *
     * Only affects messages created by the default services.
     */
    var useMessageTypesHttps: Boolean

    /**
     * The configured agent for this context.
     */
    var agent: IAgent?
}