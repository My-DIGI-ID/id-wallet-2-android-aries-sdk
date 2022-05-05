package com.digital_enabling.android_aries_sdk.payments

import com.digital_enabling.android_aries_sdk.payments.models.IndyPaymentInputSource
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*

/**
 * Payment address record.
 * Initializes a new instance of the [PaymentAddressRecord] class.
 */
@Serializable
class PaymentAddressRecord: RecordBase() {

    override val id: String = UUID.randomUUID().toString()

    /**
     * The name of the type.
     */
    override var typeName: String = "AF.PaymentAddress"
    var sources: List<IndyPaymentInputSource> = emptyList()

    /**
     * The balance.
     */
    var balance: ULong = 0u
        get() = if(sources.isNotEmpty()) sources.sumOf { it.amount } else 0u

    /**
     * The address.
     */
    var address: String? = null

    /**
     * The method.
     */
    var method: String? = null

    /**
     * The sources synced at.
     */
    @Contextual
    var sourcesSyncedAt: LocalDateTime?
        get() = getDateTime("sourcesSyncedAt")
        set(value) {
            set(value.toString(), false, "sourcesSyncedAt")
        }

    override fun toJson(): String =
        Json.encodeToString(this)
}