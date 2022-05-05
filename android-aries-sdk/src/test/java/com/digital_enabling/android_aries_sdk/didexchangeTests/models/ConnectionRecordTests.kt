package com.digital_enabling.android_aries_sdk.didexchangeTests.models

import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionState
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionTrigger
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import kotlin.Exception

class ConnectionRecordTests {

    @Test
    @DisplayName("test State INVITED with Trigger INVITATION_ACCEPT.")
    fun test_sInvited_tInvitationAccept(){
        //Arrange
        val testObject = ConnectionRecord("testID")

        //Act
        testObject.trigger(ConnectionTrigger.INVITATION_ACCEPT)
        val actual = testObject.state
        val expected = ConnectionState.NEGOTIATING

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test State INVITED with Trigger REQUEST.")
    fun test_sInvited_tRequest(){
        //Arrange
        val testObject = ConnectionRecord("testID")

        //Act
        testObject.trigger(ConnectionTrigger.REQUEST)
        val actual = testObject.state
        val expected = ConnectionState.NEGOTIATING

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test State INVITED with Trigger RESPONSE.")
    fun test_sInvited_tResponse(){
        //Arrange
        val testObject = ConnectionRecord("testID")

        //Act

        //Assert
        assertThrows<Exception> { testObject.trigger(ConnectionTrigger.RESPONSE) }
    }

    @Test
    @DisplayName("test State NEGOTIATING with Trigger INVITATION_ACCEPT.")
    fun test_sNegotiating_tInvitationAccept(){
        //Arrange
        val testObject = ConnectionRecord("testID")
        testObject.trigger(ConnectionTrigger.INVITATION_ACCEPT)
        val initialTestState = testObject.state
        //Act

        //Assert
        Assertions.assertEquals(ConnectionState.NEGOTIATING, initialTestState)
        assertThrows<Exception> { testObject.trigger(ConnectionTrigger.INVITATION_ACCEPT) }
    }

    @Test
    @DisplayName("test State NEGOTIATING with Trigger REQUEST.")
    fun test_sNegotiating_tRequest(){
        //Arrange
        val testObject = ConnectionRecord("testID")
        testObject.trigger(ConnectionTrigger.INVITATION_ACCEPT)
        val initialTestState = testObject.state
        //Act
        testObject.trigger(ConnectionTrigger.REQUEST)
        val actual = testObject.state
        val expected = ConnectionState.CONNECTED

        //Assert
        Assertions.assertEquals(ConnectionState.NEGOTIATING, initialTestState)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test State NEGOTIATING with Trigger RESPONSE.")
    fun test_sNegotiating_tResponse(){
        //Arrange
        val testObject = ConnectionRecord("testID")
        testObject.trigger(ConnectionTrigger.INVITATION_ACCEPT)
        val initialTestState = testObject.state
        //Act
        testObject.trigger(ConnectionTrigger.RESPONSE)
        val actual = testObject.state
        val expected = ConnectionState.CONNECTED

        //Assert
        Assertions.assertEquals(ConnectionState.NEGOTIATING, initialTestState)
        Assertions.assertEquals(expected, actual)
    }


    @Test
    @DisplayName("test State CONNECTED with Trigger INVITATION_ACCEPT.")
    fun test_sConnected_tInvitationAccept(){
        //Arrange
        val testObject = ConnectionRecord("testID")
        testObject.trigger(ConnectionTrigger.INVITATION_ACCEPT)
        testObject.trigger(ConnectionTrigger.REQUEST)
        val initialTestState = testObject.state

        //Act

        //Assert
        Assertions.assertEquals(ConnectionState.CONNECTED, initialTestState)
        assertThrows<Exception> { testObject.trigger(ConnectionTrigger.INVITATION_ACCEPT) }
    }

    @Test
    @DisplayName("test State CONNECTED with Trigger REQUEST.")
    fun test_sConnected_tRequest(){
        //Arrange
        val testObject = ConnectionRecord("testID")
        testObject.trigger(ConnectionTrigger.INVITATION_ACCEPT)
        testObject.trigger(ConnectionTrigger.REQUEST)
        val initialTestState = testObject.state

        //Act

        //Assert
        Assertions.assertEquals(ConnectionState.CONNECTED, initialTestState)
        assertThrows<Exception> { testObject.trigger(ConnectionTrigger.REQUEST) }
    }

    @Test
    @DisplayName("test State CONNECTED with Trigger RESPONSE.")
    fun test_sConnected_tResponse(){
        //Arrange
        val testObject = ConnectionRecord("testID")
        testObject.trigger(ConnectionTrigger.INVITATION_ACCEPT)
        testObject.trigger(ConnectionTrigger.REQUEST)
        val initialTestState = testObject.state

        //Act

        //Assert
        Assertions.assertEquals(ConnectionState.CONNECTED, initialTestState)
        assertThrows<Exception> { testObject.trigger(ConnectionTrigger.RESPONSE) }
    }

    @Test
    @DisplayName("toJson works")
    fun toJson(){
        //Arrange
        val testObject = ConnectionRecord("testID")
        testObject.trigger(ConnectionTrigger.INVITATION_ACCEPT)
        testObject.trigger(ConnectionTrigger.REQUEST)
        testObject.createdAtUtc = LocalDateTime.now()

        //Act
        val actual = testObject.toJson()
        val expected = Json.encodeToString(testObject)

        //Assert
        Assertions.assertEquals(expected, actual)
    }
}