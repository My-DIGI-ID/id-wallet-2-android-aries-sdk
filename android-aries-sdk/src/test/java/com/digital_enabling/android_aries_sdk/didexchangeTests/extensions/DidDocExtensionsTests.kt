package com.digital_enabling.android_aries_sdk.didexchangeTests.extensions

import com.digital_enabling.android_aries_sdk.agents.models.AgentEndpoint
import com.digital_enabling.android_aries_sdk.configuration.ProvisioningRecord
import com.digital_enabling.android_aries_sdk.didexchange.extensions.myDidDoc
import com.digital_enabling.android_aries_sdk.didexchange.extensions.theirDidDoc
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.didexchange.models.dids.DidDoc
import com.digital_enabling.android_aries_sdk.didexchange.models.dids.DidDocKey
import com.digital_enabling.android_aries_sdk.didexchange.models.dids.IndyAgentDidDocService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DidDocExtensionsTests {

    //region Tests for myDidDoc
    @Test
    @DisplayName("MyDidDoc method returns proper DidDoc.")
    fun myDidDoc_setProvisioningRecordEndpoint() {
        val testConnectionRecord = ConnectionRecord("testId")
        testConnectionRecord.myVk = "testVK"
        testConnectionRecord.myDid = "testDid"
        val testProvisioningRecord = ProvisioningRecord()
        testProvisioningRecord.endpoint = AgentEndpoint()
        testProvisioningRecord.endpoint.uri = "testUri"
        testProvisioningRecord.endpoint.verkey = arrayOf("testVerkey")

        val expectedDidDoc = DidDoc(
            id = "testDid",
            keys = listOf(
                DidDocKey(
                    "testDid#keys-1",
                    "Ed25519VerificationKey2018",
                    "testDid",
                    "testVK"
                )
            ),
            services = listOf(IndyAgentDidDocService(
                "testDid;indy",
                "IndyAgent",
                listOf(testConnectionRecord.myVk!!),
                testProvisioningRecord.endpoint.verkey!!.toList(),
                testProvisioningRecord.endpoint.uri!!
            ))
        )
        val actualDidDoc = testConnectionRecord.myDidDoc(testProvisioningRecord)

        assertEquals(expectedDidDoc.id, actualDidDoc.id)
        assertEquals(expectedDidDoc.keys, actualDidDoc.keys)
        assertEquals(expectedDidDoc.services, actualDidDoc.services)
    }

    @Test
    @DisplayName("MyDidDoc method returns DidDoc with empty endpoint list if provisioning record endpoint uri is not set.")
    fun myDidDoc_noSetProvisioningRecordEndpoiunt() {
        val testConnectionRecord = ConnectionRecord("testId")
        testConnectionRecord.myVk = "testVK"
        testConnectionRecord.myDid = "testDid"
        val testProvisioningRecord = ProvisioningRecord()
        testProvisioningRecord.endpoint = AgentEndpoint()
        testProvisioningRecord.endpoint.uri = ""
        testProvisioningRecord.endpoint.verkey = arrayOf("testVerkey")

        val expectedDidDoc = DidDoc(
            id = "testDid",
            keys = listOf(
                DidDocKey(
                    "testDid#keys-1",
                    "Ed25519VerificationKey2018",
                    "testDid",
                    "testVK"
                )
            ),
            services = emptyList()
        )
        val actualDidDoc = testConnectionRecord.myDidDoc(testProvisioningRecord)

        assertEquals(expectedDidDoc.id, actualDidDoc.id)
        assertEquals(expectedDidDoc.keys, actualDidDoc.keys)
        assertEquals(expectedDidDoc.services, actualDidDoc.services)
    }
    //endregion

    //region Tests for theirDidDoc
    @Test
    @DisplayName("TheirDidDoc method returns proper DidDoc.")
    fun theirDidDoc_properReturn(){
        val testConnectionRecord = ConnectionRecord("testId")
        testConnectionRecord.myVk = "testVK"
        testConnectionRecord.myDid = "testDid"
        testConnectionRecord.theirDid = "testTheirDid"
        testConnectionRecord.theirVk = "testTheirVK"
        testConnectionRecord.endpoint = AgentEndpoint()
        testConnectionRecord.endpoint!!.uri = "testUri"
        testConnectionRecord.endpoint!!.verkey = arrayOf("testVerkey")

        val expectedDidDoc = DidDoc(
            id = testConnectionRecord.theirDid!!,
            keys = listOf(
                DidDocKey(
                    "{${testConnectionRecord.theirDid}}#keys-1",
                    "Ed25519VerificationKey2018",
                    testConnectionRecord.theirDid!!,
                    testConnectionRecord.theirVk!!
                )
            ),
            services = listOf(IndyAgentDidDocService(
                "{${testConnectionRecord.theirDid}};indy",
                "IndyAgent",
                listOf(testConnectionRecord.theirVk!!),
                testConnectionRecord.endpoint!!.verkey!!.toList(),
                testConnectionRecord.endpoint!!.uri!!
            ))
        )
        val actualDidDoc = testConnectionRecord.theirDidDoc()

        assertEquals(expectedDidDoc.id, actualDidDoc.id)
        assertEquals(expectedDidDoc.keys, actualDidDoc.keys)
        assertEquals(expectedDidDoc.services, actualDidDoc.services)
    }
    //endregion

}