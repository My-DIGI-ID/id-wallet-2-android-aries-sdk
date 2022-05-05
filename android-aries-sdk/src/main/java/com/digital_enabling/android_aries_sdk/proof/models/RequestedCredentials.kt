package com.digital_enabling.android_aries_sdk.proof.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Requested credentials dto.
 */
@Serializable
class RequestedCredentials(
    /**
     * The requested attributes.
     */
    @SerialName("requested_attributes")
    var requestedAttributes: Map<String, RequestedAttribute?>? = HashMap(),
    /**
     * The self attested attributes.
     */
    @SerialName("self_attested_attributes")
    var selfAttestedAttributes: Map<String, String?>? = HashMap(),
    /**
     * The requested predicates.
     */
    @SerialName("requested_predicates")
    var requestedPredicates: Map<String, RequestedAttribute?>? = HashMap()
) {
    /**
     * Gets a collection of distinct credential identifiers found in this object.
     */
    internal fun getCredentialIdentifiers(): List<String> {
        var credIds = requestedAttributes?.filter { x -> x.value?.credentialId != null }
            ?.map { x -> x.value?.credentialId!! }?.toList() ?: listOf()
        credIds = credIds.plus(requestedPredicates?.filter { x -> x.value?.credentialId != null }
            ?.map { x -> x.value?.credentialId!! }?.toList() ?: listOf())

        return credIds.distinct()
    }
}