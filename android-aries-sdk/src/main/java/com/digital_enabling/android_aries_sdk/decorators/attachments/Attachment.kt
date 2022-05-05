package com.digital_enabling.android_aries_sdk.decorators.attachments


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

/**
 * Attach decorator
 */
@Serializable
class Attachment(
    /**
     * The attachment identifier.
     */
    @SerialName("@id")
    val id: String? = null
) {
    /**
     * The nickname.
     */
    @SerialName("nickname")
    var nickname: String? = null

    /**
     * The type of the MIME.
     */
    @SerialName("mime-type")
    var mimeType: String? = null

    /**
     * The filename.
     */
    @SerialName("filename")
    var filename: String? = null

    //TODO: Check DateTime Serialization
    /**
     * The last modified time.
     */
    @SerialName("lastmod_time")
    var lastModifiedTime: String? = null

    /**
     * The attachment content.
     */
    var data: AttachmentContent? = null
}