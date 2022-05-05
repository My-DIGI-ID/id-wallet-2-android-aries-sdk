package com.digital_enabling.android_aries_sdk.common

/**
 * Representation of a message processing event.
 */
data class ServiceMessageProcessingEvent(val threadId: String, val recordId: String, val messageType: String) {
    override fun toString(): String{
        return "${this::class.simpleName}: ThreadId=$threadId, RecordId=$recordId, MessageType=$messageType"
    }
}