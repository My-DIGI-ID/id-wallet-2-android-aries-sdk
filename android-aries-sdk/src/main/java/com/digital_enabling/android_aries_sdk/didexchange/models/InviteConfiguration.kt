package com.digital_enabling.android_aries_sdk.didexchange.models

/**
 * Config for controlling invitation creation.
 */
class InviteConfiguration {
    /**
     * Id of the resulting connection record created by the invite.
     */
    var connectionId: String? = null

    /**
     * Used to generated an invitation that multiple parties can use to connect.
     */
    var multiPartyInvitation: Boolean = false

    /**
     * Alias object for marking the invite subject
     * with an alias for giving the inviter greater context.
     */
    var theirAlias: ConnectionAlias? = null

    /**
     * For optionally setting my alias information on the invite.
     */
    var myAlias: ConnectionAlias? = null

    /**
     *  For automatically accepting a connection request generated from this created invite
     */
    var autoAcceptConnection: Boolean = false

    /**
     *  Controls the tags that are persisted against the invite/connection record.
     */
    var tags: Map<String, String> = emptyMap()

    override fun toString(): String {
        return "${this.javaClass.simpleName}: ConnectionId=$connectionId, MultiPartyInvitation=$multiPartyInvitation, TheirAlias=$theirAlias, MyAlias=$myAlias, AutoAcceptConnection=$autoAcceptConnection, Tags=$tags"
    }
}