package com.digital_enabling.android_aries_sdk.decorators.transport

/**
 * Return route types.
 */
enum class ReturnRouteTypes {
    /**
     * No messages should be returned over this connection.
     */
    NONE,
    /**
     * All messages for this key should be returned over this connection.
     */
    ALL,
    /**
     * Send all messages matching this thread over this conneciton.
     */
    THREAD
}