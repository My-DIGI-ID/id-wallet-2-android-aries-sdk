package com.digital_enabling.android_aries_sdk.transaction.models

import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionInvitationMessage

data class TransactionInfo(
    var sessionId: String? = null,
    var connectionInvitationMessage: ConnectionInvitationMessage? = null,
    var awaitableConnection: Boolean = false,
    var awaitableProof: Boolean = false
)