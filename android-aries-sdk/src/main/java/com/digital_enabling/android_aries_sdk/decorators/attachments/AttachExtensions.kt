package com.digital_enabling.android_aries_sdk.decorators.attachments

import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage

const val DECORATOR_IDENTIFIER: String = "attach"

/**
 * Adds an attachment to the message using the ~attach attachment.
 * @param attachment The attachment.
 * @param overrideExisting If set to true [override existing].
 */
fun AgentMessage.addAttachment(attachment: Attachment, overrideExisting: Boolean = true) {
    val decorator = this.findDecorator<AttachDecorator>(DECORATOR_IDENTIFIER) ?: AttachDecorator()
    val existing = decorator[attachment]

    if (existing != null && !overrideExisting) {
        throw IllegalArgumentException("Attachment {${attachment.nickname}} already exists.")
    }

    if (existing != null) {
        decorator.remove(existing)
    }

    decorator.add(attachment)
    this.addDecorator(decorator, DECORATOR_IDENTIFIER)
}

/**
 * Gets the attachment.
 * @param nickname The nickname.
 */
fun AgentMessage.getAttachment(nickname: String): Attachment? {
    val decorator = this.findDecorator<AttachDecorator>(DECORATOR_IDENTIFIER) ?: AttachDecorator()
    return decorator[nickname]
}

/**
 * Removes the attachment.
 * @param nickname the nickname.
 */
fun AgentMessage.removeAttachment(nickname: String): Boolean {
    val decorator = this.findDecorator<AttachDecorator>(DECORATOR_IDENTIFIER) ?: AttachDecorator()
    val result = decorator.remove(decorator[nickname])
    this.setDecorator(decorator, DECORATOR_IDENTIFIER)
    return result
}