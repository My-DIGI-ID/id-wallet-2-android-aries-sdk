package com.digital_enabling.android_aries_sdk.decorators.attachments

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Attachment content
 */
@Serializable
class AttachmentContent {
    /**
     * The base64.
     */
    @SerialName("base64")
    var base64: String? = null

    /**
     * The sha256.
     */
    @SerialName("sha256")
    var sha256: String? = null

    /**
     * The links.
     */
    @SerialName("links")
    var links: Array<String>? = null
}