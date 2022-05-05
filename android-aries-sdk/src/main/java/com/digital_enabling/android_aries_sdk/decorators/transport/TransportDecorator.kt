package com.digital_enabling.android_aries_sdk.decorators.transport

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an attachment decorator ~transport
 */
@Serializable
class TransportDecorator {
    /**
     * Return route.
     */
    @SerialName("return_route")
    var returnRoute: String? = null

    /**
     * Return route thread.
     */
    @SerialName("return_route_thread")
    var returnRouteThread: String? = null

    /**
     * Queued message count.
     */
    @SerialName("queued_message_count")
    var queuedMessageCount: String? = null

}