package com.digital_enabling.android_aries_sdk.didexchange.models

import com.digital_enabling.android_aries_sdk.agents.models.AgentEndpoint
import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement

/**
 * Represents a connection record in the agency wallet.
 * @see RecordBase
 */
@Serializable
class ConnectionRecord(@SerialName("Id") override val id: String) : RecordBase() {
    @SerialName("State")
    var state: ConnectionState = ConnectionState.INVITED
        private set

    /**
     * The type name.
     */
    @Transient
    override var typeName: String = RecordType.CONNECTION_RECORD.typeName

    /**
     * Indicates if the property is multi-party.
     */
    var multiPartyInvitation: Boolean = false

    /**
     * My did.
     */
    var myDid: String? = null

    /**
     * my vk.
     */
    var myVk: String? = null

    /**
     * Their did.
     */
    var theirDid: String? = null

    /**
     * Their vk.
     */
    var theirVk: String? = null

    /**
     * The connection alias.
     */
    @SerialName("Alias")
    var alias: ConnectionAlias? = null

    /**
     * The endpoint.
     */
    @SerialName("Endpoint")
    var endpoint: AgentEndpoint? = null

    /**
     * Creates a shallow copy of the current [ConnectionRecord]
     */
    fun shallowCopy(): ConnectionRecord {
        var newConnectionRecord = ConnectionRecord(this.id)
        newConnectionRecord.state = this.state
        newConnectionRecord.typeName = this.typeName
        newConnectionRecord.multiPartyInvitation = this.multiPartyInvitation
        newConnectionRecord.myDid = this.myDid
        newConnectionRecord.myVk = this.myVk
        newConnectionRecord.theirDid = this.theirDid
        newConnectionRecord.theirVk = this.theirVk
        newConnectionRecord.createdAtUtc = this.createdAtUtc
        newConnectionRecord.updatedAtUtc = this.updatedAtUtc
        newConnectionRecord.alias = this.alias
        newConnectionRecord.endpoint = this.endpoint
        newConnectionRecord.tags = this.tags

        return newConnectionRecord
    }

    /**
     * Creates a deep copy of the current [ConnectionRecord]
     */
    fun deepCopy(): ConnectionRecord {
        var newConnectionRecord = ConnectionRecord(this.id)
        newConnectionRecord.state = this.state
        newConnectionRecord.typeName = this.typeName
        newConnectionRecord.multiPartyInvitation = this.multiPartyInvitation
        newConnectionRecord.myDid = this.myDid
        newConnectionRecord.myVk = this.myVk
        newConnectionRecord.theirDid = this.theirDid
        newConnectionRecord.theirVk = this.theirVk
        newConnectionRecord.createdAtUtc = this.createdAtUtc
        newConnectionRecord.updatedAtUtc = this.updatedAtUtc

        val oldAlias = this.alias
        var alias = ConnectionAlias()
        if (oldAlias != null) {
            alias = oldAlias
        }
        newConnectionRecord.alias = alias

        val oldEndpoint = this.endpoint
        var endpoint = AgentEndpoint()
        if (oldEndpoint != null) {
            endpoint = oldEndpoint
        }
        newConnectionRecord.endpoint = endpoint

        var tags = mutableMapOf<String, String>()
        this.tags.forEach { x -> tags.put(x.key, x.value) }
        newConnectionRecord.tags = tags

        return newConnectionRecord
    }

    /**
     * Triggers the ConnectionState Statemachine.
     */
    fun trigger(trigger: ConnectionTrigger) {
        changeState(trigger)
    }

    private fun changeState(trigger: ConnectionTrigger) {
        when (trigger) {
            ConnectionTrigger.INVITATION_ACCEPT -> {
                if (state == ConnectionState.INVITED) {
                    state = ConnectionState.NEGOTIATING
                    return
                }
            }
            ConnectionTrigger.REQUEST -> {
                if (state == ConnectionState.INVITED) {
                    state = ConnectionState.NEGOTIATING
                    return
                } else if (state == ConnectionState.NEGOTIATING) {
                    state = ConnectionState.CONNECTED
                    return
                }
            }
            ConnectionTrigger.RESPONSE -> {
                if (state == ConnectionState.NEGOTIATING) {
                    state = ConnectionState.CONNECTED
                    return
                }
            }
        }
        throw Exception("Invalid Transition with Trigger $trigger from present state $state.")
    }

    override fun toJson(): String {
        return Json.encodeToString(this)
    }
}