package com.digital_enabling.android_aries_sdk.transaction.models

import com.digital_enabling.android_aries_sdk.credential.models.OfferConfiguration
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.proof.models.ProofRequest
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class TransactionRecord(override val id: String) :
    RecordBase() {
    @SerialName("connection")
    var connectionId: String? = null
    @SerialName("connectionRecord")
    var connectionRecord: ConnectionRecord? = null
    var credentialRecordId: String? = null
    var offerConfiguration: OfferConfiguration? = null
    var proofRecordId: String? = null
    var proofRequest: ProofRequest? = null
    override var typeName: String = "AF.TransactionRecord"

    override fun toJson(): String =
        Json.encodeToString(this)
}