package com.digital_enabling.android_aries_sdk.agents

object MessageTypesHttps {
    const val CONNECTION_INVITATION: String = "https://didcomm.org/connections/1.0/invitation"
    const val CONNECTION_REQUEST: String = "https://didcomm.org/connections/1.0/request"
    const val CONNECTION_RESPONSE: String = "https://didcomm.org/connections/1.0/response"
    const val FORWARD: String = "https://didcomm.org/routing/1.0/forward"
    const val BASIC_MESSAGE_TYPE: String = "https://didcomm.org/basicmessage/1.0/message"
    const val TRUST_PING_MESSAGE_TYPE: String = "https://didcomm.org/trust_ping/1.0/ping"
    const val TRUST_PING_RESPONSE_MESSAGE_TYPE: String = "https://didcomm.org/trust_ping/1.0/ping_response"
    const val DISCOVERY_QUERY_MESSAGE_TYPE: String = "https://didcomm.org/discover-features/1.0/query"
    const val DISCOVERY_DISCLOSE_MESSAGE_TYPE: String = "https://didcomm.org/discover-features/1.0/disclose"

    object IssueCredentialNames {
        const val PROPOSE_CREDENTIAL : String = "https://didcomm.org/issue-credential/1.0/propose-credential"
        const val PREVIEW_CREDENTIAL : String = "https://didcomm.org/issue-credential/1.0/credential-preview"
        const val OFFER_CREDENTIAL : String = "https://didcomm.org/issue-credential/1.0/offer-credential"
        const val REQUEST_CREDENTIAL : String = "https://didcomm.org/issue-credential/1.0/request-credential"
        const val ISSUE_CREDENTIAL : String = "https://didcomm.org/issue-credential/1.0/issue-credential"
    }

    object PresentProofNames{
        const val PROPOSE_PRESENTATION: String = "https://didcomm.org/present-proof/1.0/propose-presentation";
        const val REQUEST_PRESENTATION: String = "https://didcomm.org/present-proof/1.0/request-presentation";
        const val PRESENTATION: String = "https://didcomm.org/present-proof/1.0/presentation";
        const val PRESENTATION_PREVIEW: String = "https://didcomm.org/present-proof/1.0/presentation-preview";
    }

    object TransactionNames {
        const val DELETE_PROOFS: String = "https://didcomm.org/privacy/1.0/deleteproofs"
        const val PROOFS_DELETED: String = "https://didcomm.org/privacy/1.0/proofsdeleted"
        const val TRANSACTION_ERROR: String = "https://didcomm.org/transaction/1.0/error"
        const val TRANSACTION_OFFER: String = "https://didcomm.org/transaction/1.0/offer"
        const val TRANSACTION_RESPONSE: String = "https://didcomm.org/transaction/1.0/response"
    }
}