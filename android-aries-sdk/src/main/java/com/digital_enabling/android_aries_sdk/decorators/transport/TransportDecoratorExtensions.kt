package com.digital_enabling.android_aries_sdk.decorators.transport

import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext
import com.digital_enabling.android_aries_sdk.decorators.DecoratorNames
import com.digital_enabling.android_aries_sdk.decorators.findDecorator
import java.lang.Exception

/**
 * Adds return routing to message.
 * @param message The message to add return routing
 */
fun UnpackedMessageContext.returnRoutingRequested(): Boolean {
    return try {
        this.findDecorator<TransportDecorator?>(DecoratorNames.TRANSPORT_DECORATOR) != null
    } catch (e: Exception) {
        false
    }
}

/**
 * Adds return routing to message
 * @param message The message to add return routing
 */
fun AgentMessage.returnRoutingRequested(): Boolean {
    return try {
        this.findDecorator<TransportDecorator>(DecoratorNames.TRANSPORT_DECORATOR) != null
    } catch (e: Exception) {
        return false
    }
}