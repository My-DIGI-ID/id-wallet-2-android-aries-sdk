package com.digital_enabling.android_aries_sdk.decorators.attachments

import kotlinx.serialization.Serializable

/**
 * Represents an attachment decorator ~attach. See also [Attachment]
 */
@Serializable
class AttachDecorator() : MutableList<Attachment> by mutableListOf() {
    /**
     * Gets the [Attachment] with the specified name.
     * @param name The name.
     * @return The [Attachment]
     */
    operator fun get(name: String): Attachment? {
        return this.find { it.nickname == name }
    }

    /**
     * Gets the [Attachment] with the specified name.
     * @param attachment The attachment.
     * @return The [Attachment]
     */
    operator fun get(attachment: Attachment): Attachment? {
        return this.find { it == attachment }
    }
}