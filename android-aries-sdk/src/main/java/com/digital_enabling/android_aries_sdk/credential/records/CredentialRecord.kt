package com.digital_enabling.android_aries_sdk.credential.records

import com.digital_enabling.android_aries_sdk.credential.models.CredentialPreviewAttribute
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Represents a credential record in the agency wallet.
 * @see RecordBase
 */
@Serializable
class CredentialRecord(override val id: String) : RecordBase() {
    /**
     * The type name.
     */
    override var typeName: String = RecordType.CREDENTIAL_RECORD.typeName

    /**
     * The state.
     */
    var state: CredentialState = CredentialState.OFFERED
        private set

    /**
     * The schema identifier.
     */
    var schemaId: String? = null

    /**
     * The connection identifier associated with this credential.
     */
    var connectionId: String? = null

    /**
     * The credential request metadata json. This field is only present in the holder wallet.
     */
    var credentialRequestMetadataJson: String? = null

    /**
     * The credential identifier. This field is only present in the holder wallet.
     */
    var credentialId: String? = null

    /**
     * The values json.
     */
    var credentialAttributesValues: List<CredentialPreviewAttribute?>? = null

    /**
     * The revocation registry identifier.
     */
    var revocationRegistryId: String? = null

    /**
     * The request json.
     */
    @Transient
    var requestJson: String? = null

    /**
     * The offer json.
     */
    @Transient
    var offerJson: String? = null

    /**
     * The credential definition identifier.
     */
    @Transient
    var credentialDefinitionId: String? = null

    /**
     * The credential revocation identifier. This field is only present in the issuer wallet.
     */
    @Transient
    var credentialRevocationId: String? = null

    /**
     * Creates a shallow copy.
     */
    fun shallowCopy(): CredentialRecord {
        var newCredentialRecord = CredentialRecord(this.id)
        newCredentialRecord.state = this.state
        newCredentialRecord.typeName = this.typeName
        newCredentialRecord.schemaId = this.schemaId
        newCredentialRecord.connectionId = this.connectionId
        newCredentialRecord.credentialRequestMetadataJson = this.credentialRequestMetadataJson
        newCredentialRecord.credentialId = this.credentialId
        newCredentialRecord.credentialAttributesValues = this.credentialAttributesValues
        newCredentialRecord.revocationRegistryId = this.revocationRegistryId
        newCredentialRecord.requestJson = this.requestJson
        newCredentialRecord.offerJson = this.offerJson
        newCredentialRecord.credentialDefinitionId = this.credentialDefinitionId
        newCredentialRecord.credentialRevocationId = this.credentialRevocationId

        return newCredentialRecord

    }

    /**
     * Creates a deep copy.
     */
    fun deepCopy(): CredentialRecord {
        var newCredentialRecord = CredentialRecord(this.id)
        newCredentialRecord.state = this.state
        newCredentialRecord.typeName = this.typeName
        newCredentialRecord.schemaId = this.schemaId
        newCredentialRecord.connectionId = this.connectionId
        newCredentialRecord.credentialRequestMetadataJson = this.credentialRequestMetadataJson
        newCredentialRecord.credentialId = this.credentialId
        newCredentialRecord.credentialAttributesValues = this.credentialAttributesValues
        newCredentialRecord.revocationRegistryId = this.revocationRegistryId
        newCredentialRecord.requestJson = this.requestJson
        newCredentialRecord.offerJson = this.offerJson
        newCredentialRecord.credentialDefinitionId = this.credentialDefinitionId
        newCredentialRecord.credentialRevocationId = this.credentialRevocationId

        return newCredentialRecord
    }

    /**
     * Triggers the CredentialState Statemachine.
     */
    fun trigger(trigger: CredentialTrigger) {
        _trigger(trigger)
    }


    private fun _trigger(trigger: CredentialTrigger) {
        when (trigger) {
            CredentialTrigger.REQUEST -> {
                if (state == CredentialState.OFFERED) {
                    state = CredentialState.REQUESTED
                    return
                }
            }
            CredentialTrigger.REJECT -> {
                if (state == CredentialState.OFFERED || state == CredentialState.REQUESTED) {
                    state = CredentialState.REJECTED
                    return
                }
            }
            CredentialTrigger.ISSUE -> {
                if (state == CredentialState.REQUESTED) {
                    state = CredentialState.ISSUED
                    return
                }
            }
            CredentialTrigger.REVOKE -> {
                if (state == CredentialState.ISSUED) {
                    state = CredentialState.REVOKED
                    return
                }
            }
        }
        throw Exception("Invalid Transition with Trigger $trigger from present state $state.")
    }

    override fun toJson(): String =
        Json.encodeToString(this)
}