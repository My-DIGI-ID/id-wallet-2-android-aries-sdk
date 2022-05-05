package com.digital_enabling.android_aries_sdk.didexchangeTests.models

import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionAlias
import com.digital_enabling.android_aries_sdk.didexchange.models.InviteConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class InviteConfigurationTests {
    @Test
    @DisplayName("ToString method works with all properties properly set")
    fun toString_properlyFilled(){
        var testAlias = ConnectionAlias()
        testAlias.name = "testAliasName"
        testAlias.imageUrl = "testAliasUrl"

        var testObject = InviteConfiguration()
        testObject.connectionId = "testConnectionId"
        testObject.multiPartyInvitation = false
        testObject.theirAlias = testAlias
        testObject.myAlias = testAlias
        testObject.autoAcceptConnection = false

        val expected = "InviteConfiguration: ConnectionId=testConnectionId, MultiPartyInvitation=false, TheirAlias=ConnectionAlias: Name=testAliasName, ImageUrl=testAliasUrl, MyAlias=ConnectionAlias: Name=testAliasName, ImageUrl=testAliasUrl, AutoAcceptConnection=false, Tags={}"
        val actual = testObject.toString()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("ToString method throws no exception when connectionId is missing")
    fun toString_missingConnectionId(){
        var testAlias = ConnectionAlias()
        testAlias.name = "testAliasName"
        testAlias.imageUrl = "testAliasUrl"

        var testObject = InviteConfiguration()
        testObject.multiPartyInvitation = false
        testObject.theirAlias = testAlias
        testObject.myAlias = testAlias
        testObject.autoAcceptConnection = false

        assertDoesNotThrow { testObject.toString() }
    }

    @Test
    @DisplayName("ToString method throws no exception when theirAlias is missing")
    fun toString_missingTheirAlias(){
        var testAlias = ConnectionAlias()
        testAlias.name = "testAliasName"
        testAlias.imageUrl = "testAliasUrl"

        var testObject = InviteConfiguration()
        testObject.connectionId = "testConnectionId"
        testObject.multiPartyInvitation = false
        testObject.myAlias = testAlias
        testObject.autoAcceptConnection = false

        assertDoesNotThrow { testObject.toString() }
    }

    @Test
    @DisplayName("ToString method throws no exception when myAlias is missing")
    fun toString_missingMyAlias(){
        var testAlias = ConnectionAlias()
        testAlias.name = "testAliasName"
        testAlias.imageUrl = "testAliasUrl"

        var testObject = InviteConfiguration()
        testObject.connectionId = "testConnectionId"
        testObject.multiPartyInvitation = false
        testObject.theirAlias = testAlias
        testObject.autoAcceptConnection = false

        assertDoesNotThrow { testObject.toString() }
    }
}