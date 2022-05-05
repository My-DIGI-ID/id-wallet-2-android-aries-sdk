package com.digital_enabling.android_aries_sdk.configurationTests

import com.digital_enabling.android_aries_sdk.agents.models.AgentEndpoint
import com.digital_enabling.android_aries_sdk.configuration.ProvisioningRecord
import com.digital_enabling.android_aries_sdk.ledger.models.IndyTaaAcceptance
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProvisioningRecordTests {
    @Test
    @DisplayName("Serialized ProvisioningRecord has key _issuerDid")
    fun serialization_works() {
        //Arrange
        val testObject = ProvisioningRecord()
        testObject.issuerDid = "testIssuerDid"
        testObject.issuerVerkey = "testIssuerVerkey"
        testObject.masterSecretId = "testMasterSecretId"
        testObject.tailsBaseUri = "testTailsBaseUri"
        testObject.defaultPaymentAddressId = "testDefaultPaymentAddressId"
        testObject.issuerSeed = "testIssuerSeed"
        testObject.useMessageTypesHttps = true

        val testAgentEndpoint = AgentEndpoint()
        testAgentEndpoint.did = "testDid"
        testAgentEndpoint.uri = "testUri"
        testAgentEndpoint.verkey = emptyArray()

        val testIndyTaaAcceptance = IndyTaaAcceptance("testDigest", "testAcceptanceMechanism")

        testObject.taaAcceptance = testIndyTaaAcceptance
        testObject.endpoint = testAgentEndpoint

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("_issuerDid"))
        Assertions.assertTrue(serializedObject.containsKey("_issuerVerkey"))
        Assertions.assertTrue(serializedObject.containsKey("_masterSecretId"))
        Assertions.assertTrue(serializedObject.containsKey("_tailsBaseUri"))
        Assertions.assertTrue(serializedObject.containsKey("_useMessageTypesHttps"))
    }
}