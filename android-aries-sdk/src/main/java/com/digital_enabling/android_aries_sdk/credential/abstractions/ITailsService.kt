package com.digital_enabling.android_aries_sdk.credential.abstractions

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import org.hyperledger.indy.sdk.blob_storage.BlobStorageReader
import org.hyperledger.indy.sdk.blob_storage.BlobStorageWriter

/**
 * Tails service
 */
interface ITailsService {
    /**
     * Opens an existing tails file and returns a handle.
     * @param filename The tails filename.
     * @return The tails reader
     */
    suspend fun openTails(filename: String): BlobStorageReader

    /**
     * Gets the BLOB storage writer.
     * @return The BLOB storage writer.
     */
    suspend fun createTails(): BlobStorageWriter

    /**
     * Check if the tails filename exists locally and download latest version if it doesn't.
     * @param agentContext The agent context
     * @param revocationRegistryId Revocation registry identifier.
     * @return The name of the tails file.
     */
    suspend fun ensureTailsExists(agentContext: IAgentContext, revocationRegistryId: String): String
}