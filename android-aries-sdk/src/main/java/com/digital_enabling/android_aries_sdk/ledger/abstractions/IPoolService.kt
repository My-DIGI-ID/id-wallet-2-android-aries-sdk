package com.digital_enabling.android_aries_sdk.ledger.abstractions

import com.digital_enabling.android_aries_sdk.ledger.models.IndyAml
import com.digital_enabling.android_aries_sdk.ledger.models.IndyTaa
import org.hyperledger.indy.sdk.pool.Pool
import java.time.OffsetDateTime

/**
 * Pool service.
 */
interface IPoolService {
    /**
     * Opens the pool coinfiguration with the specified name and sets the node protocol version of the current process.
     * @param poolName Name of the pool configuration.
     * @param protocolVersion The protocol version of the nodes.
     * @return A handle to the pool.
     */
    fun getPool(poolName: String, protocolVersion: Int): Pool

    /**
     * Opens the pool configuration with the specified name.
     * @param poolName Pool name.
     * @return The pool.
     */
    fun getPool(poolName: String): Pool

    /**
     * Gets the transaction author agreement if one is set on the ledger. Otherwise, returns null.
     * @param poolName
     */
    fun getTaa(poolName: String): IndyTaa?

    /**
     * Gets the acceptance mechanisms list from the ledger if one is set.await Otherwise, returns null.
     * @param poolName
     * @param timestamp
     * @param version
     * @return
     */
    fun getAml(poolName: String, timestamp: OffsetDateTime = OffsetDateTime.now(), version: String = "") : IndyAml?

    /**
     * Creates a pool configuration.
     * @param poolName The name of the pool configuration.
     * @param genesisFile Genesis transaction file.
     */
    fun createPool(poolName: String, genesisFile: String)
}