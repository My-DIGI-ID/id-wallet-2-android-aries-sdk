package com.digital_enabling.android_aries_sdk.proof.models

import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
/**
 * Represents a proof record in the agency wallet.
 * Initializes a new instance of the [ProofRecord] class.
 */
@Serializable
class ProofRecord(override val id: String) : RecordBase() {
    /**
     * The state.
     */
    @Required
    var state: ProofState = ProofState.REQUESTED
        private set

    /**
     * The type name.
     */
    @Required
    override var typeName: String = RecordType.PROOF_RECORD.typeName

    /**
     * The proof proposal json.
     */
    var proposalJson: String? = null

    /**
     * The proof request json.
     */
    var requestJson: String? = null

    /**
     * The proof json.
     */
    var proofJson: String? = null

    /**
     * The connection identifier.
     */
    @Transient
    var connectionId: String? = null

    constructor(id: String, isProposal: Boolean = false) : this(id) {
        if(isProposal){
            state = ProofState.PROPOSED
        }
    }

    /**
     * Creates a shallow copy of the current [ProofRecord] object.
     */
    fun shallowCopy(): ProofRecord {
        var newProofRecord = ProofRecord(this.id)
        newProofRecord.state = this.state
        newProofRecord.typeName = this.typeName
        newProofRecord.proposalJson = this.proposalJson
        newProofRecord.requestJson = this.requestJson
        newProofRecord.proofJson = this.proofJson
        newProofRecord.connectionId = this.connectionId
        newProofRecord.createdAtUtc = this.createdAtUtc
        newProofRecord.updatedAtUtc = this.updatedAtUtc
        newProofRecord.tags = this.tags

        return newProofRecord
    }

    /**
     * Creates a deep copy of the current [ProofRecord] object.
     */
    fun deepCopy(): ProofRecord {
        var newProofRecord = ProofRecord(this.id)
        newProofRecord.state = this.state
        newProofRecord.typeName = this.typeName
        newProofRecord.proposalJson = this.proposalJson
        newProofRecord.requestJson = this.requestJson
        newProofRecord.proofJson = this.proofJson
        newProofRecord.connectionId = this.connectionId
        newProofRecord.createdAtUtc = this.createdAtUtc
        newProofRecord.updatedAtUtc = this.updatedAtUtc
        var tags = mutableMapOf<String, String>()
        this.tags.forEach { x -> tags.put(x.key, x.value) }
        newProofRecord.tags = tags

        return newProofRecord
    }

    override fun toString(): String {
        return "${this::class.java.simpleName}: " +
                "State=$state, " +
                "ConnectionId=$connectionId, " +
                "RequestJson=${
                    if (requestJson.isNullOrEmpty()) {
                        null
                    } else {
                        "[hidden]"
                    }
                }, " +
                "ProofJson=${
                    if (proofJson.isNullOrEmpty()) {
                        null
                    } else {
                        "[hidden]"
                    }
                }, " +
                super.toString()
    }

    /**
     * Triggers the ProofState Statemachine.
     */
    fun trigger(trigger: ProofTrigger) {
        changeState(trigger)
    }

    private fun changeState(trigger: ProofTrigger) {
        when (trigger) {
            ProofTrigger.REQUEST -> {
                if (state == ProofState.PROPOSED) {
                    state = ProofState.REQUESTED
                    return
                }
            }
            ProofTrigger.ACCEPT -> {
                if (state == ProofState.REQUESTED) {
                    state = ProofState.ACCEPTED
                    return
                }
            }
            ProofTrigger.REJECT -> {
                if (state == ProofState.REQUESTED) {
                    state = ProofState.REJECTED
                    return
                }
            }
            else -> {

            }
        }
        throw Exception("Invalid Transition with Trigger $trigger from present state $state.")
    }

    override fun toJson(): String =
        Json.encodeToString(this)
}