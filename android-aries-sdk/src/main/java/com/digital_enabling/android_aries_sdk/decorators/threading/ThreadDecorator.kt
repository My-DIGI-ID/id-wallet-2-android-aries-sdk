package com.digital_enabling.android_aries_sdk.decorators.threading

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Thread object decorator representation.
 */
@Serializable
class ThreadDecorator {
    /**
     * Thread id.
     */
    @SerialName("thid")
    var threadId: String? = null

    /**
     * Parent thread id.
     */
    @SerialName("pthid")
    var parentThreadId: String? = null

    /**
     * Sender order.
     */
    @SerialName("sender_order")
    var senderOrder: Int = 0

    /**
     * Received order.
     */
    @SerialName("received_orders")
    var receivedOrders: Map<String, Int> = emptyMap()
}