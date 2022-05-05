package com.digital_enabling.android_aries_sdk.didexchangeTests.models

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.decorators.signature.SignatureDecorator
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionInvitationMessage
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionResponseMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.Exception

class ConnectionResponseMessageTests {

    //region Tests for serialization
    @Test
    @DisplayName("Serialization works with all properties set.")
    fun serialization_allPropertiesSet(){
        val testObject = ConnectionResponseMessage()

        val testDecorator = SignatureDecorator()
        testDecorator.signatureType = "testSignatureType"
        testDecorator.signatureData = "testSignatureData"
        testDecorator.signature = "testSignature"

        testObject.connectionSig = testDecorator

        val expected = "{\"@id\":\"testId\",\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/response\",\"connection~sig\":{\"@type\":\"testSignatureType\",\"sig_data\":\"testSignatureData\",\"signature\":\"testSignature\"}}".split(',')
        val actual = Json.encodeToString(testObject).split(',')

        assertEquals(expected[1], actual[1])
        assertEquals(expected[2], actual[2])
        assertEquals(expected[3], actual[3])
        assertEquals(expected[4], actual[4])
    }

    @Test
    @DisplayName("Deserialization works with all properties set.")
    fun deserialization_allPropertiesSet(){
        val testDecorator = SignatureDecorator()
        testDecorator.signatureType = "testSignatureType"
        testDecorator.signatureData = "testSignatureData"
        testDecorator.signature = "testSignature"

        val testJson = "{\"@id\":\"testId\", \"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/response\",\"connection~sig\":{\"@type\":\"testSignatureType\",\"sig_data\":\"testSignatureData\",\"signature\":\"testSignature\"}}"
        val actualObject = Json.decodeFromString<ConnectionResponseMessage>(testJson)

        assertEquals("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/response", actualObject.type)
        assertEquals(testDecorator.signatureType, actualObject.connectionSig?.signatureType)
        assertEquals(testDecorator.signatureData, actualObject.connectionSig?.signatureData)
        assertEquals(testDecorator.signature, actualObject.connectionSig?.signature)
    }

    @Test
    @DisplayName("Deserialization throws exception if signature decorator is missing.")
    fun deserialization_missingConnectionSig(){
        val testJson = "{\"@id\":\"testId\", \"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/connections/1.0/response\"}"
        assertThrows<Exception> { Json.decodeFromString(testJson) }
    }
    //endregion

    //region Tests for constructor
    @Test
    @DisplayName("Messagetype is https if constructor parameter is set true.")
    fun constructor_withParameter(){
        val testObject = ConnectionResponseMessage(true)
        assertEquals(MessageTypesHttps.CONNECTION_RESPONSE, testObject.type)
    }

    @Test
    @DisplayName("Messagetype is http if constructor parameter is missing.")
    fun constructor_withoutParameter(){
        val testObject = ConnectionResponseMessage()
        assertEquals(MessageTypes.CONNECTION_RESPONSE, testObject.type)
    }
    //endregion

}