package com.digital_enabling.android_aries_sdk.messagedispatcher

import com.digital_enabling.android_aries_sdk.agents.models.PackedMessageContext
import java.net.URI

/**
 *  Message Dispatcher.
 */
interface IMessageDispatcher {

    /**
     * Supported transport schemes by the dispatcher.
     */
    val transportSchemes: List<String>

    /**
     * Sends a message using the dispatcher.
     * @param uri Uri to dispatch the message to.
     * @param message Message context to dispatch.
     * @return A message context.
     */
    suspend fun dispatch(
        uri: URI,
        message: PackedMessageContext,
        additionalHeaders: List<Pair<String, String>>
    ): PackedMessageContext?
}