package com.digital_enabling.android_aries_sdk.discoveryTests

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.discovery.DisclosedMessageProtocol
import com.digital_enabling.android_aries_sdk.discovery.DiscoveryDiscloseMessage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DiscoveryDiscloseMessageTests {
    @Test
    @DisplayName("Constructor call without any arguments returns correct http DISCOVERY_DISCLOSE_MESSAGE_TYPE.")
    fun constructor_withoutArgument(){
        val testObject = DiscoveryDiscloseMessage()

        val expectedMessageType = MessageTypes.DISCOVERY_DISCLOSE_MESSAGE_TYPE
        val actualMessageType = testObject.type

        assertEquals(expectedMessageType, actualMessageType)
    }

    @Test
    @DisplayName("Constructor call with argument returns correct https DISCOVERY_DISCLOSE_MESSAGE_TYPE")
    fun constructor_withArgument(){
        val testObject = DiscoveryDiscloseMessage(true)

        val expectedMessageType = MessageTypesHttps.DISCOVERY_DISCLOSE_MESSAGE_TYPE
        val actualMessageType = testObject.type

        assertEquals(expectedMessageType, actualMessageType)
    }

    @Test
    @DisplayName("Serialization works correcly.")
    fun serialization_works(){
        val testObject = DiscoveryDiscloseMessage(true)
        testObject.id = "testId"
        testObject.protocols.add(DisclosedMessageProtocol("testPid", "testRoles"))

        val expected = "{\"useMessageTypesHttps\":true,\"@id\":\"testId\",\"@type\":\"https://didcomm.org/discover-features/1.0/disclose\",\"protocols\":[{\"pid\":\"testPid\",\"roles\":\"testRoles\"}]}"
        val actual = Json.encodeToString(testObject)

        assertEquals(expected, actual)
    }
}