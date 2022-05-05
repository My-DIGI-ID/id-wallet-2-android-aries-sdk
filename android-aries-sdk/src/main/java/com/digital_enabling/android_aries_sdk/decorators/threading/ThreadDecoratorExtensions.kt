package com.digital_enabling.android_aries_sdk.decorators.threading

import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode

/**
 * Threading decorator extension
 */
const val DECORATOR_IDENTIFIER: String = "thread"

/**
 * Created a new threaded message response.
 */
inline fun <reified T> AgentMessage.createThreadReply(): T where T : AgentMessage {
    val newMsg = T::class.objectInstance // TODO: find out why object is always null
    if (newMsg != null) {
        newMsg.threadMessage(this)
        return newMsg
    } else {
        throw AriesFrameworkException(ErrorCode.INVALID_MESSAGE, "Can't get threadMessage")
    }
}

/**
 * Threads the current message from a previous message.
 * @param previousMessage The message to thread from.
 */
fun AgentMessage.threadFrom(previousMessage: AgentMessage) {
    var hasThreadBlock = false
    try {
        this.getDecorator<ThreadDecorator>(DECORATOR_IDENTIFIER)
        hasThreadBlock = true
    } catch (e: AriesFrameworkException) {
    }

    if (hasThreadBlock) {
        throw AriesFrameworkException(
            ErrorCode.INVALID_MESSAGE,
            "Cannot thread message when it already has a valid thread decorator"
        )
    }

    this.threadMessage(previousMessage)
}

/**
 * Gets the current messages thread id.
 * @return Thread id of the message.
 */
fun AgentMessage.getThreadId(): String? {
    var threadId: String? = null
    try {
        val threadBlock = this.getDecorator<ThreadDecorator>(DECORATOR_IDENTIFIER)
        threadId = threadBlock.threadId
    } catch (e: Exception) {
    }

    return threadId
}


/**
 * Threads the current message.
 * @param threadId Thread id to thread the message with.
 */
fun AgentMessage.threadFrom(threadId: String) {
    val currentThreadContext = ThreadDecorator()
    currentThreadContext.threadId = threadId
    this.addDecorator(currentThreadContext, DECORATOR_IDENTIFIER)
}

/**
 *
 */
fun AgentMessage.threadMessage(messageToThreadFrom: AgentMessage) {
    var previousMessageThreadContext: ThreadDecorator? = null
    try {
        previousMessageThreadContext = this.getDecorator<ThreadDecorator>(DECORATOR_IDENTIFIER)
    } catch (e: AriesFrameworkException) {
    }

    val currentThreadContext = ThreadDecorator()
    if (previousMessageThreadContext != null) {
        currentThreadContext.parentThreadId = previousMessageThreadContext.parentThreadId
        currentThreadContext.threadId = previousMessageThreadContext.threadId
    } else {
        currentThreadContext.threadId = messageToThreadFrom.id
    }

    this.addDecorator(currentThreadContext, DECORATOR_IDENTIFIER)
}
