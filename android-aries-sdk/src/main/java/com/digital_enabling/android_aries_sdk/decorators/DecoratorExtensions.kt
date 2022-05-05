package com.digital_enabling.android_aries_sdk.decorators

import com.digital_enabling.android_aries_sdk.agents.models.MessageContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode

/**
 * Decorator Extensions.
 */

/**
 * Finds the decorator with the specified name or returns null.
 * @param messageContext Message Context.
 * @param name The name.
 * @return The requested decorator or null
 */
fun <T> MessageContext.findDecorator(name: String): T {
    if(this.packed){
        throw AriesFrameworkException(ErrorCode.INVALID_MESSAGE, "Cannot fetch decorator from packed message.")
    }
    val messageJson = this.messageJson
    if (messageJson != null) {
        return messageJson["~{$name}"] as T
    } else {
        throw Exception("MessageJson not found.")
    }

}