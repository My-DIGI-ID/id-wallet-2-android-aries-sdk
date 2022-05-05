package com.digital_enabling.android_aries_sdk.ledger


import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.ledger.abstractions.ILedgerSigningService
import com.digital_enabling.android_aries_sdk.ledger.abstractions.IPoolService
import com.digital_enabling.android_aries_sdk.ledger.models.IndyAml
import com.digital_enabling.android_aries_sdk.ledger.models.IndyTaa
import kotlinx.serialization.json.*
import org.hyperledger.indy.sdk.ledger.Ledger
import org.hyperledger.indy.sdk.pool.Pool
import java.time.OffsetDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class DefaultPoolService : IPoolService {
    /**
     * Collection of active pool handles.
     */
    private val pools = ConcurrentHashMap<String, Pool>()

    /**
     * Concurrent collection of txn author agreements
     */
    private val taas = ConcurrentHashMap<String, IndyTaa>()

    /**
     * Concurrent collection of acceptance mechanisms lists
     */
    private val amls = ConcurrentHashMap<String, IndyAml>()

    /**
     * @see IPoolService
     */
    override fun getPool(poolName: String, protocolVersion: Int): Pool {
        Pool.setProtocolVersion(protocolVersion)
        return getPool(poolName)
    }

    /**
     * @see IPoolService
     */
    override fun getPool(poolName: String): Pool {
        var pool : Pool
        try {
            pool = pools.getValue(poolName)
            return pool
        }
        catch (e: Exception) {
        }

        pool = Pool.openPoolLedger(poolName,null).get()
        if (pool != null) {
            pools[poolName] = pool
            return pool
        }
        else {
            throw Exception("Pool could not be opened.")
        }
    }

    /**
     * @see IPoolService
     */
    override fun getTaa(poolName: String): IndyTaa? {
        fun parseTaa(response : String) : IndyTaa? {
            val jResponse = Json.parseToJsonElement(response) as JsonObject
                if (jResponse.containsValue(Any())) {
                    val text = (((jResponse["result"] as JsonObject)["data"] as JsonObject)["text"]).toString()
                    val version = (((jResponse["result"] as JsonObject)["data"] as JsonObject)["version"]).toString()
                    return IndyTaa(text,version)
                }
                return null
            }

        if(taas.containsKey(poolName)){
            return taas[poolName]
        }

        val pool = getPool(poolName,2)
        val req = Ledger.buildGetTxnAuthorAgreementRequest(null,null).get()
        val res = Ledger.submitRequest(pool, req).get()

        ensureSuccessResponse(res)

        val taa = parseTaa(res)
        if(taa != null){
            taas[poolName] = taa
        }
        return taa
    }

    /**
     * @see IPoolService
     */
    override fun getAml(poolName: String, timestamp: OffsetDateTime, version: String) : IndyAml? {
        fun parseAml(response : String) : IndyAml? {
            val jResponse = Json.parseToJsonElement(response) as JsonObject
            val aml = (jResponse["result"] as JsonObject)["data"]
            if(aml != null){
                return Json.decodeFromJsonElement(aml)
            }
            return null
        }

        if(amls.containsKey(poolName)){
            return amls[poolName]
        }

        val pool = getPool(poolName,2)
        val timeStamp : Int =
            if (OffsetDateTime.MIN == null) {
                -1
            } else {
                timestamp.toEpochSecond().toInt()
            }
        val req = Ledger.buildGetAcceptanceMechanismsRequest(
            null,
            timeStamp,
            version
        ).get()
        val res = Ledger.submitRequest(pool, req).get()

        ensureSuccessResponse(res)

        val aml = parseAml(res)
        if(aml != null){
            amls[poolName] = aml
        }
        return aml
    }

    /**
     * @see IPoolService
     */
    override fun createPool(poolName: String, genesisFile: String) {
        val poolConfig = "{genesis_txn=$genesisFile}"
        Pool.createPoolLedgerConfig(poolName, poolConfig)
    }

    private fun ensureSuccessResponse(res : String)
    {
        val response = Json.parseToJsonElement(res) as JsonObject

        if (response["op"] != null) {
            if (response["op"]!!.toString().lowercase(Locale.getDefault()) != "reply")
                throw AriesFrameworkException(ErrorCode.LEDGER_OPERATION_REJECTED, "Ledger operation rejected")
        }
        else{
            throw AriesFrameworkException(ErrorCode.LEDGER_OPERATION_REJECTED, "Ledger operation rejected")
        }
    }
}