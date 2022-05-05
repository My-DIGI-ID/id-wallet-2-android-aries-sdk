package com.digital_enabling.android_aries_sdk.didexchange.models

import com.digital_enabling.android_aries_sdk.didexchange.models.dids.DidDoc
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *  Connection object
 */
@Serializable
data class Connection(
    /**
     * The did.
     */
    @SerialName("DID")
    var did: String?,
    /**
     * The did doc.
     */
    @SerialName("DIDDoc")
    var didDoc: DidDoc?
) {}