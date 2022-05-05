package com.digital_enabling.android_aries_sdk.agents.extensions

import com.digital_enabling.android_aries_sdk.agents.MessageType
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgent

/**
 * Gets the supported message types.
 * @return The supported message types.
 */
fun IAgent.getSupportedMessageTypes(): List<MessageType>{
    val supportedMessageTypes = mutableListOf<MessageType>()
    for(handler in this.handlers){
        supportedMessageTypes.addAll(handler.supportedMessageTypes)
    }

    return supportedMessageTypes.distinct()
}