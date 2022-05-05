package com.digital_enabling.android_aries_sdk.decorators.service

import com.digital_enabling.android_aries_sdk.configuration.ProvisioningRecord

/**
 * Service decorator extensions
 */

    /**
     * Get a service decorator representation for this provisioning record
     * @param record
     */
    fun ProvisioningRecord.toServiceDecorator() : ServiceDecorator {
        val serviceDecorator = ServiceDecorator()
        val issuerVerkey = this.issuerVerkey ?: throw Exception("Issuer Verkey not found.")
        val endpointVerkey = this.endpoint.verkey ?: throw Exception("Verkey not found.")
        serviceDecorator.recipientKeys = listOf(issuerVerkey)
        serviceDecorator.routingKeys = endpointVerkey.toList()
        serviceDecorator.serviceEndpoint = this.endpoint.uri
        return serviceDecorator
    }
