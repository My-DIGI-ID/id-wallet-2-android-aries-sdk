package com.digital_enabling.android_aries_sdk.credential.models

import com.digital_enabling.android_aries_sdk.proof.models.RevocationInterval
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a credential object as stored in the wallet.
 */
@Serializable
data class Credential(
    /**
     * The credential object info.
     */
    @Required
    @SerialName("cred_info")
    var credentialInfo: CredentialInfo,
    /**
     * The non revocation interval for this credential.
     */
    @Required
    @SerialName("interval")
    var nonRevocationInterval: RevocationInterval
) {
}