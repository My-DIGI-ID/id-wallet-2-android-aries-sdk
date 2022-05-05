package com.digital_enabling.android_aries_sdk.didexchange.extensions

import com.digital_enabling.android_aries_sdk.configuration.ProvisioningRecord
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.didexchange.models.dids.DidDoc
import com.digital_enabling.android_aries_sdk.didexchange.models.dids.DidDocKey
import com.digital_enabling.android_aries_sdk.didexchange.models.dids.IndyAgentDidDocService

/**
 * Extensions for interacting with DID docs.
 */

/**
 * Default key type.
 */
val ConnectionRecord.defaultKeyType: String
    get() = "Ed25519VerificationKey2018"

/**
 * Constructs my DID doc in a pairwise relationship from a connection record and the agents provisioning record.
 * @param provisioningRecord Provisioning record.
 * @return DID Doc.
 */
fun ConnectionRecord.myDidDoc(provisioningRecord: ProvisioningRecord): DidDoc {
    val did = this.myDid ?: throw Exception("Did not found.")
    val vk = this.myVk ?: throw Exception("Vk not found.")
    val keyList = listOf(
        DidDocKey(
            id = "${this.myDid}#keys-1",
            type = defaultKeyType,
            controller = did,
            publicKeyBase58 = vk
        )
    )

    val endpointUri = provisioningRecord.endpoint.uri ?: throw Exception("URI not found.")
    val verkey = provisioningRecord.endpoint.verkey ?: throw Exception("Verkey not found.")
    val endpointList = if (!endpointUri.isEmpty()) listOf(
        IndyAgentDidDocService(
            id = "${this.myDid};indy",
            serviceEndpoint = endpointUri,
            recipientKeys = listOf(vk),
            routingKeys = verkey.toList()
        )
    ) else emptyList()

    return DidDoc(id = did, keys = keyList, services = endpointList)
}

/**
 * Constructs their DID doc in a pairwise relationship from a connection record.
 * @return DID Doc.
 */
fun ConnectionRecord.theirDidDoc(): DidDoc {
    val theirDid = this.theirDid ?: throw IllegalArgumentException("TheirDid not found")
    val theirVk = this.theirVk ?: throw IllegalArgumentException("theirVk not found.")
    val keyList = listOf(
        DidDocKey(
            id = "{$theirDid}#keys-1",
            type = this.defaultKeyType,
            controller = theirDid,
            publicKeyBase58 = theirVk
        )
    )

    val endpointUri = this.endpoint?.uri ?: throw Exception("URI not found.")
    val endpoint = this.endpoint ?: throw Exception("Endpoint not found.")
    val endpointVerkey = endpoint.verkey ?: throw Exception("Verky not found.")
    val endpointList =
        listOf(
            IndyAgentDidDocService(
                id = "{$theirDid};indy",
                serviceEndpoint = endpointUri,
                recipientKeys = listOf(theirVk),
                routingKeys = endpointVerkey.toList()
            )
        )

    return DidDoc(id = theirDid, keys = keyList, services = endpointList)
}