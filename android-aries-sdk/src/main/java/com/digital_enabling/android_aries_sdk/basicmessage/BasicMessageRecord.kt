package com.digital_enabling.android_aries_sdk.basicmessage

import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime

/**
 * Represents a private message record in the user's wallet.
 */
@Serializable
class BasicMessageRecord(override val id: String) : RecordBase() {

    /**
     * The name of the type.
     */
    override var typeName: String = RecordType.BASIC_MESSAGE_RECORD.typeName

    /**
     * The connection identifier.
     */
    @Transient
    var connectionId : String? = null

    /**
     * The sent time.
     */
    @Contextual
    var sentTime : LocalDateTime?
        get() = getDateTime("sentTime")
        set(value) {
            set(value.toString(), false, "sentTime")
        }

    /**
     * The direction.
     */
    var direction : MessageDirection? = null

    /**
     * The text.
     */
    var text : String? = null

    override fun toJson(): String =
        Json.encodeToString(this)
}