package com.digital_enabling.android_aries_sdk.agentsTests.models

import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.agents.models.UnpackedMessageContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.credential.models.CredentialIssueMessage
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.routing.ForwardMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.HashMap

class UnpackedMessageContextTests {
    @Test
    @DisplayName("UnpackedMessageContext can be constructed from String and ConnectionRecord")
    fun constructor_fromStringAndConnectionRecord() {
        //Arrange
        val testConnection = ConnectionRecord("testConnectionId")
        testConnection.theirVk = "testTheirVk"
        val testMessage = "{ \"testKey\":\"testValue\" }"

        val testObject = UnpackedMessageContext(testMessage, testConnection)

        //Act
        val actualPacked = testObject.packed
        val actualPayload = testObject.payload
        val actualSenderVerkey = testObject.senderVerkey
        val actualMessageJson = testObject.messageJson
        val actualConnection = testObject.connection

        //Assert
        Assertions.assertArrayEquals(
            testMessage.toByteArray(Charsets.UTF_8), actualPayload
        )
        Assertions.assertFalse(actualPacked)
        Assertions.assertEquals(testConnection.theirVk, actualSenderVerkey)
        Assertions.assertEquals(
            Json.decodeFromString<JsonObject>(testMessage),
            actualMessageJson
        )
        Assertions.assertEquals(testConnection, actualConnection)
        Assertions.assertNull(testObject.contextRecord)
    }

    @Test
    @DisplayName("UnpackedMessageContext can be constructed from String and String")
    fun constructor_fromStringAndString() {
        //Arrange
        val testSenderVerkey = "testSenderVerkey"
        val testMessage = "{ \"testKey\":\"testValue\" }"

        val testObject = UnpackedMessageContext(testMessage, testSenderVerkey)

        //Act
        val actualPacked = testObject.packed
        val actualPayload = testObject.payload
        val actualSenderVerkey = testObject.senderVerkey
        val actualMessageJson = testObject.messageJson

        //Assert
        Assertions.assertArrayEquals(
            testMessage.toByteArray(Charsets.UTF_8), actualPayload
        )
        Assertions.assertFalse(actualPacked)
        Assertions.assertEquals(testSenderVerkey, actualSenderVerkey)
        Assertions.assertEquals(
            Json.decodeFromString<JsonObject>(testMessage),
            actualMessageJson
        )
        Assertions.assertNull(testObject.connection)
        Assertions.assertNull(testObject.contextRecord)
    }

    @Test
    @DisplayName("UnpackedMessageContext has an empty messageJson if the given String has errors")
    fun constructor_fromWrongMessageString() {
        //Arrange
        val testSenderVerkey = "testSenderVerkey"
        val testMessage = "{ \"testKey\":=\"testValue\" }"

        val testObject = UnpackedMessageContext(testMessage, testSenderVerkey)

        //Act
        val actualPacked = testObject.packed
        val actualPayload = testObject.payload
        val actualSenderVerkey = testObject.senderVerkey
        val actualMessageJson = testObject.messageJson

        //Assert
        Assertions.assertArrayEquals(
            testMessage.toByteArray(Charsets.UTF_8), actualPayload
        )
        Assertions.assertFalse(actualPacked)
        Assertions.assertEquals(testSenderVerkey, actualSenderVerkey)
        Assertions.assertEquals(
            JsonObject(LinkedHashMap()),
            actualMessageJson
        )
        Assertions.assertNull(testObject.connection)
        Assertions.assertNull(testObject.contextRecord)
    }

    @Test
    @DisplayName("getMessageId works correctly")
    fun getMessageId_works() {
        //Arrange
        val testSenderVerkey = "testSenderVerkey"
        val testAgentMessage = AgentMessage(true)
        testAgentMessage.id = "testId"
        testAgentMessage.type = "testType"

        val testObject = UnpackedMessageContext(Json.encodeToString(testAgentMessage), testSenderVerkey)

        //Act
        val actual = testObject.getMessageId()

        //Assert
        Assertions.assertEquals("testId", actual)
    }

    @Test
    @DisplayName("getMessageId throws an AriesFrameworkException if the messageJson doesn't contain an id")
    fun getMessageId_noId() {
        //Arrange
        val testSenderVerkey = "testSenderVerkey"
        val testMessage = "{ \"noId\":\"testValue\" }"

        val testObject = UnpackedMessageContext(testMessage, testSenderVerkey)

        //Act

        //Assert
        assertThrows<AriesFrameworkException> { testObject.getMessageId() }
    }

    @Test
    @DisplayName("getMessageType works correctly")
    fun getMessageType_works() {
        //Arrange
        val testSenderVerkey = "testSenderVerkey"
        val testAgentMessage = AgentMessage(true)
        testAgentMessage.id = "testId"
        testAgentMessage.type = "testType"

        val testObject = UnpackedMessageContext(Json.encodeToString(testAgentMessage), testSenderVerkey)

        //Act
        val actual = testObject.getMessageType()

        //Assert
        Assertions.assertEquals("testType", actual)
    }

    @Test
    @DisplayName("getMessageType throws an AriesFrameworkException if the messageJson doesn't contain a type")
    fun getMessageType_noId() {
        //Arrange
        val testSenderVerkey = "testSenderVerkey"
        val testMessage = "{ \"noType\":\"testValue\" }"

        val testObject = UnpackedMessageContext(testMessage, testSenderVerkey)

        //Act

        //Assert
        assertThrows<AriesFrameworkException> { testObject.getMessageType() }
    }

    @Test
    @DisplayName("getMessage works correctly")
    fun getMessage_works() {
        //Arrange
        val testSenderVerkey = "testSenderVerkey"
        val testAgentMessage = AgentMessage(true)
        testAgentMessage.id = "testId"
        testAgentMessage.type = "testType"

        val testObject = UnpackedMessageContext(Json.encodeToString(testAgentMessage), testSenderVerkey)

        //Act
        val actual = testObject.getMessage<AgentMessage>()

        //Assert
        Assertions.assertEquals(Json.encodeToString(testAgentMessage), Json.encodeToString(actual))
        Assertions.assertEquals(testAgentMessage.id, actual.id)
        Assertions.assertEquals(testAgentMessage.type, actual.type)
    }

    @Test
    @DisplayName("getMessage throws an AriesFrameworkException the type doesn't match the message")
    fun getMessage_wrongType() {
        //Arrange
        val testSenderVerkey = "testSenderVerkey"
        val testCredentialIssueMessage = CredentialIssueMessage()
        testCredentialIssueMessage.comment = "testComment"
        testCredentialIssueMessage.credentials = listOf()

        val testObject = UnpackedMessageContext(Json.encodeToString(testCredentialIssueMessage), testSenderVerkey)

        //Act

        //Assert
        assertThrows<AriesFrameworkException> { testObject.getMessage<ForwardMessage>() }
    }

    @Test
    @DisplayName("getThisMessageJson works correctly")
    fun getThisMessageJson_works() {
        //Arrange
        val testSenderVerkey = "testSenderVerkey"
        val testAgentMessage = AgentMessage(true)
        testAgentMessage.id = "testId"
        testAgentMessage.type = "testType"

        val testObject = UnpackedMessageContext(Json.encodeToString(testAgentMessage), testSenderVerkey)

        //Act
        val actual = testObject.getThisMessageJson()

        //Assert
        Assertions.assertEquals(
            String(
                Json.encodeToString(testAgentMessage).toByteArray(Charsets.UTF_8),
                Charsets.UTF_8
            ), actual
        )
    }

    @Test
    @DisplayName("getMessage works correctly for derived classes")
    fun getMessage_worksForDerivatives() {
        //Arrange
        val testSenderVerkey = "testSenderVerkey"
        val testCreateInboxMessage = ForwardMessage()
        testCreateInboxMessage.id = "testId"
        testCreateInboxMessage.type = "testType"
        testCreateInboxMessage.to = "testTo"
        testCreateInboxMessage.message = JsonObject(HashMap())

        val testObject = UnpackedMessageContext(Json.encodeToString(testCreateInboxMessage), testSenderVerkey)

        //Act
        val actual = testObject.getMessage<ForwardMessage>()

        //Assert
        Assertions.assertEquals(Json.encodeToString(testCreateInboxMessage), Json.encodeToString(actual))
        Assertions.assertEquals(testCreateInboxMessage.id, actual.id)
        Assertions.assertEquals(testCreateInboxMessage.type, actual.type)
        Assertions.assertEquals(testCreateInboxMessage.message!!["Mobile-Secret"],
            actual.message?.get("Mobile-Secret")
        )
        Assertions.assertEquals(testCreateInboxMessage.message!!["Device-Validation"],
            actual.message?.get("Device-Validation")
        )
    }
}