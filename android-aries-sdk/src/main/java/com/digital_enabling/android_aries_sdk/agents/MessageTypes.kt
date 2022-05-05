package com.digital_enabling.android_aries_sdk.agents

object MessageTypes {
    const val CONNECTION_INVITATION: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/invitation"
    const val CONNECTION_REQUEST: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/request"
    const val CONNECTION_RESPONSE: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/response"
    const val FORWARD: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/routing/1.0/forward"
    const val BASIC_MESSAGE_TYPE: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/basicmessage/1.0/message"
    const val TRUST_PING_MESSAGE_TYPE: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/trust_ping/1.0/ping"
    const val TRUST_PING_RESPONSE_MESSAGE_TYPE: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/trust_ping/1.0/ping_response"
    const val DISCOVERY_QUERY_MESSAGE_TYPE: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/discover-features/1.0/query"
    const val DISCOVERY_DISCLOSE_MESSAGE_TYPE: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/discover-features/1.0/disclose"

    object IssueCredentialNames {
        const val PROPOSE_CREDENTIAL: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/issue-credential/1.0/propose-credential"
        const val PREVIEW_CREDENTIAL: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/issue-credential/1.0/credential-preview"
        const val OFFER_CREDENTIAL: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/issue-credential/1.0/offer-credential"
        const val REQUEST_CREDENTIAL: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/issue-credential/1.0/request-credential"
        const val ISSUE_CREDENTIAL: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/issue-credential/1.0/issue-credential"
    }

    object PresentProofNames{
        const val PROPOSE_PRESENTATION: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/present-proof/1.0/propose-presentation";
        const val REQUEST_PRESENTATION: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/present-proof/1.0/request-presentation";
        const val PRESENTATION: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/present-proof/1.0/presentation";
        const val PRESENTATION_PREVIEW: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/present-proof/1.0/presentation-preview";
    }

    object TransactionNames {
        const val DELETE_PROOFS: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/privacy/1.0/deleteproofs"
        const val PROOFS_DELETED: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/privacy/1.0/proofsdeleted"
        const val TRANSACTION_ERROR: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/transaction/1.0/error"
        const val TRANSACTION_OFFER: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/transaction/1.0/offer"
        const val TRANSACTION_RESPONSE: String = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/transaction/1.0/response"
    }
}