package com.digital_enabling.android_aries_sdk.agents

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgent
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.MessageContext
import com.digital_enabling.android_aries_sdk.ledger.models.PoolAwaitable
import org.hyperledger.indy.sdk.wallet.Wallet
import java.util.*

open class DefaultAgentContext: IAgentContext {

    /**
     * The agent context wallet.
     */
    override var wallet: Wallet? = null

    /**
     * The agent context pool.
     */
    override var pool: PoolAwaitable? = null

    /**
     * The agent context state.
     */
    override var state: Dictionary<String, String?>? = null

    /**
     * A list of supported messages of the current agent.
     */
    override var supportedMessages: List<MessageType>? = null

    /**
     * True if to use UseMessageTypesHttps.
     * Only affects messages created by the default services.
     */
    override var useMessageTypesHttps: Boolean = false

    /**
     * The configured agent for this context.
     */
    override var agent: IAgent? = null

    private var queue: Queue<MessageContext> = LinkedList()

    /**
     * Adds a message to the current processing queue
     */
    fun addNext(message: MessageContext) = queue.add(message)

    /**
     * Own implementation of dotnet tryDequeue without the out-parameter.
     */
    fun tryGetNext(): Pair<Boolean, MessageContext?>{
        return if(!queue.isEmpty()){
            val element = queue.elementAt(0)
            if(queue.remove(element)){
                Pair(true, element)
            } else {
                Pair(false, element)
            }
        } else {
            Pair(false, null)
        }
    }
}