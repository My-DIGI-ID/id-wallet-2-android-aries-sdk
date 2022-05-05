package com.digital_enabling.android_aries_sdk.credential.models

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

/**
 * Represents credential preview attribute
 */
@Serializable
class CredentialPreviewAttribute {
    /**
     * Default Constructor.
     */
    constructor() {
        mimeType = CredentialMimeTypes.TEXT_MIME_TYPE
    }

    /**
     * String type credential attribute constructor.
     * @param name Name of the credential attribute.
     * @param value Value of the credential attribute.
     */
    constructor(name: String, value: String): this() {
        this.name = name
        this.value = value
    }

    /**
     * The name.
     */
    @SerialName("name")
    var name: String? = null

    /**
     *  The type of the MIME.
     */
    @Required
    @SerialName("mime-type")
    var mimeType: String? = null

    /**
     * The value.
     */
    @SerialName("value")
    var value: String? = null
}