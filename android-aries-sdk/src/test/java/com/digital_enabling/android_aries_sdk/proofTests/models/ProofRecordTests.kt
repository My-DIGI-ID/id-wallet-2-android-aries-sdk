package com.digital_enabling.android_aries_sdk.proofTests.models

import com.digital_enabling.android_aries_sdk.proof.models.ProofRecord
import com.digital_enabling.android_aries_sdk.proof.models.ProofState
import com.digital_enabling.android_aries_sdk.proof.models.ProofTrigger
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

class ProofRecordTests {
    //region constructor tests
    @Test
    @DisplayName("ProofRecord can be constructed from only a string.")
    fun primary_constructor() {
        //Arrange
        val testId = UUID.randomUUID().toString()
        val testObject = ProofRecord(testId)

        //Act

        //Assert
        Assertions.assertEquals(testId, testObject.id)
    }

    @Test
    @DisplayName("ProofRecord has state REQUESTED if it is constructed from a string and false.")
    fun secondary_constructor_false() {
        //Arrange
        val testId = UUID.randomUUID().toString()
        val testObject = ProofRecord(testId, false)

        //Act

        //Assert
        Assertions.assertEquals(ProofState.REQUESTED, testObject.state)
    }

    @Test
    @DisplayName("ProofRecord has state PROPOSED if it is constructed from a string and true.")
    fun secondary_constructor_true() {
        //Arrange
        val testId = UUID.randomUUID().toString()
        val testObject = ProofRecord(testId, true)

        //Act

        //Assert
        Assertions.assertEquals(ProofState.PROPOSED, testObject.state)
    }
    //endregion

    //region serialization tests
    @Test
    @DisplayName("Serialized ProofRecord has key state if it is not null.")
    fun serialization_state() {
        //Arrange
        val testObject = ProofRecord(UUID.randomUUID().toString())
        testObject.createdAtUtc = LocalDateTime.now()
        testObject.updatedAtUtc = LocalDateTime.now()
        val z = testObject.createdAtUtc
        val x = Json.encodeToString(testObject)
        val y = x

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("state"))
    }

    @Test
    @DisplayName("Serialized ProofRecord has key typeName if it is not null.")
    fun serialization_typeName() {
        //Arrange
        val testObject = ProofRecord(UUID.randomUUID().toString())

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("typeName"))
    }

    @Test
    @DisplayName("Serialized ProofRecord has key proposalJson if it is not null.")
    fun serialization_proposalJson() {
        //Arrange
        val testObject = ProofRecord(UUID.randomUUID().toString())
        testObject.proposalJson = "testJson"

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("proposalJson"))
    }

    @Test
    @DisplayName("Serialized ProofRecord has key requestJson if it is not null.")
    fun serialization_requestJson() {
        //Arrange
        val testObject = ProofRecord(UUID.randomUUID().toString())
        testObject.requestJson = "testJson"

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("requestJson"))
    }

    @Test
    @DisplayName("Serialized ProofRecord has key proofJson if it is not null.")
    fun serialization_proofJson() {
        //Arrange
        val testObject = ProofRecord(UUID.randomUUID().toString())
        testObject.proofJson = "testJson"

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertTrue(serializedObject.containsKey("proofJson"))
    }

    @Test
    @DisplayName("Serialized ProofRecord has no key connectionId even if it is not null.")
    fun serialization_connectionId() {
        //Arrange
        val testObject = ProofRecord(UUID.randomUUID().toString())
        testObject.connectionId = "testId"

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject) as JsonObject

        //Assert
        Assertions.assertFalse(serializedObject.containsKey("connectionId"))
    }
    //endregion

    //region toString tests
    @Test
    @DisplayName("ProofRecord toString() has RequestJson=null if requestJson is null")
    fun toString_request_null() {
        //Arrange
        val testId = UUID.randomUUID().toString()
        val testObject = ProofRecord(testId)
        testObject.connectionId = "testId"

        //Act
        val actual = testObject.toString()
        val expected =
            "ProofRecord: State=REQUESTED, ConnectionId=testId, RequestJson=null, " +
                    "ProofJson=null, " +
                    "ProofRecord: " +
                    "Id=$testId, TypeName=AF.ProofRecord, " +
                    "CreatedAtUtc=null, UpdatedAtUtc=null"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("ProofRecord toString() has RequestJson=null if requestJson is empty")
    fun toString_request_empty() {
        //Arrange
        val testId = UUID.randomUUID().toString()
        val testObject = ProofRecord(testId)
        testObject.connectionId = "testId"
        testObject.requestJson = ""

        //Act
        val actual = testObject.toString()
        val expected =
            "ProofRecord: State=REQUESTED, ConnectionId=testId, RequestJson=null, " +
                    "ProofJson=null, " +
                    "ProofRecord: " +
                    "Id=$testId, TypeName=AF.ProofRecord, " +
                    "CreatedAtUtc=null, UpdatedAtUtc=null"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("ProofRecord toString() has RequestJson=[hidden] if requestJson is not null or empty")
    fun toString_request_filled() {
        //Arrange
        val testId = UUID.randomUUID().toString()
        val testObject = ProofRecord(testId)
        testObject.connectionId = "testId"
        testObject.requestJson = "testJson"

        //Act
        val actual = testObject.toString()
        val expected =
            "ProofRecord: State=REQUESTED, ConnectionId=testId, RequestJson=[hidden], " +
                    "ProofJson=null, " +
                    "ProofRecord: " +
                    "Id=$testId, TypeName=AF.ProofRecord, " +
                    "CreatedAtUtc=null, UpdatedAtUtc=null"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("ProofRecord toString() has ProofJson=null if proofJson is null")
    fun toString_proof_null() {
        //Arrange
        val testId = UUID.randomUUID().toString()
        val testObject = ProofRecord(testId)
        testObject.connectionId = "testId"

        //Act
        val actual = testObject.toString()
        val expected =
            "ProofRecord: State=REQUESTED, ConnectionId=testId, RequestJson=null, " +
                    "ProofJson=null, " +
                    "ProofRecord: " +
                    "Id=$testId, TypeName=AF.ProofRecord, " +
                    "CreatedAtUtc=null, UpdatedAtUtc=null"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("ProofRecord toString() has ProofJson=null if proofJson is empty")
    fun toString_proof_empty() {
        //Arrange
        val testId = UUID.randomUUID().toString()
        val testObject = ProofRecord(testId)
        testObject.connectionId = "testId"
        testObject.proofJson = ""

        //Act
        val actual = testObject.toString()
        val expected =
            "ProofRecord: State=REQUESTED, ConnectionId=testId, RequestJson=null, " +
                    "ProofJson=null, " +
                    "ProofRecord: " +
                    "Id=$testId, TypeName=AF.ProofRecord, " +
                    "CreatedAtUtc=null, UpdatedAtUtc=null"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("ProofRecord toString() has ProofJson=[hidden] if proofJson is not null or empty")
    fun toString_proof_filled() {
        //Arrange
        val testId = UUID.randomUUID().toString()
        val testObject = ProofRecord(testId)
        testObject.connectionId = "testId"
        testObject.proofJson = "testJson"

        //Act
        val actual = testObject.toString()
        val expected =
            "ProofRecord: State=REQUESTED, ConnectionId=testId, RequestJson=null, " +
                    "ProofJson=[hidden], " +
                    "ProofRecord: " +
                    "Id=$testId, TypeName=AF.ProofRecord, " +
                    "CreatedAtUtc=null, UpdatedAtUtc=null"

        //Assert
        Assertions.assertEquals(expected, actual)
    }
    //endregion

    //region state tests
    @Test
    @DisplayName("ProofRecord with state PROPOSED throws an Exception for trigger PROPOSE")
    fun state_proposed_propose() {
        //Arrange
        val testObject =
            Json.decodeFromString<ProofRecord>("{\"id\":\"aafda3e0-c243-4546-9111-171b854825dc\",\"state\":\"PROPOSED\",\"typeName\":\"AF.ProofRecord\"}")
        val trigger = ProofTrigger.PROPOSE

        //Act

        //Assert
        assertThrows<Exception> { testObject.trigger(trigger) }
    }

    @Test
    @DisplayName("ProofRecord with state PROPOSED changes to REQUESTED for trigger REQUEST")
    fun state_proposed_request() {
        //Arrange
        val testObject =
            Json.decodeFromString<ProofRecord>("{\"id\":\"aafda3e0-c243-4546-9111-171b854825dc\",\"state\":\"PROPOSED\",\"typeName\":\"AF.ProofRecord\"}")
        val trigger = ProofTrigger.REQUEST

        //Act
        testObject.trigger(trigger)
        val actual = testObject.state

        //Assert
        assertEquals(ProofState.REQUESTED, actual)
    }

    @Test
    @DisplayName("ProofRecord with state PROPOSED throws an Exception for trigger ACCEPT")
    fun state_proposed_accept() {
        //Arrange
        val testObject =
            Json.decodeFromString<ProofRecord>("{\"id\":\"aafda3e0-c243-4546-9111-171b854825dc\",\"state\":\"PROPOSED\",\"typeName\":\"AF.ProofRecord\"}")
        val trigger = ProofTrigger.ACCEPT

        //Act

        //Assert
        assertThrows<Exception> { testObject.trigger(trigger) }
    }

    @Test
    @DisplayName("ProofRecord with state PROPOSED throws an Exception for trigger REJECT")
    fun state_proposed_reject() {
        //Arrange
        val testObject =
            Json.decodeFromString<ProofRecord>("{\"id\":\"aafda3e0-c243-4546-9111-171b854825dc\",\"state\":\"PROPOSED\",\"typeName\":\"AF.ProofRecord\"}")
        val trigger = ProofTrigger.REJECT

        //Act

        //Assert
        assertThrows<Exception> { testObject.trigger(trigger) }
    }

    @Test
    @DisplayName("ProofRecord with state REQUESTED throws an Exception for trigger PROPOSE")
    fun state_requested_propose() {
        //Arrange
        val testId = UUID.randomUUID().toString()
        val testObject = ProofRecord(testId)
        val trigger = ProofTrigger.PROPOSE

        //Act

        //Assert
        assertThrows<Exception> { testObject.trigger(trigger) }
    }

    @Test
    @DisplayName("ProofRecord with state REQUESTED throws an Exception for trigger REQUEST")
    fun state_requested_request() {
        //Arrange
        val testId = UUID.randomUUID().toString()
        val testObject = ProofRecord(testId)
        val trigger = ProofTrigger.REQUEST

        //Act

        //Assert
        assertThrows<Exception> { testObject.trigger(trigger) }
    }

    @Test
    @DisplayName("ProofRecord with state REQUESTED changes to ACCEPTED for trigger ACCEPT")
    fun state_requested_accept() {
        //Arrange
        val testId = UUID.randomUUID().toString()
        val testObject = ProofRecord(testId)
        val trigger = ProofTrigger.ACCEPT

        //Act
        testObject.trigger(trigger)
        val actual = testObject.state

        //Assert
        assertEquals(ProofState.ACCEPTED, actual)
    }

    @Test
    @DisplayName("ProofRecord with state REQUESTED changes to REJECTED for trigger REJECT")
    fun state_requested_reject() {
        //Arrange
        val testId = UUID.randomUUID().toString()
        val testObject = ProofRecord(testId)
        val trigger = ProofTrigger.REJECT

        //Act
        testObject.trigger(trigger)
        val actual = testObject.state

        //Assert
        assertEquals(ProofState.REJECTED, actual)
    }

    @Test
    @DisplayName("ProofRecord with state REJECTED throws an Exception for trigger PROPOSE")
    fun state_rejected_propose() {
        //Arrange
        val testObject =
            Json.decodeFromString<ProofRecord>("{\"id\":\"aafda3e0-c243-4546-9111-171b854825dc\",\"state\":\"REJECTED\",\"typeName\":\"AF.ProofRecord\"}")
        val trigger = ProofTrigger.PROPOSE

        //Act

        //Assert
        assertThrows<Exception> { testObject.trigger(trigger) }
    }

    @Test
    @DisplayName("ProofRecord with state REJECTED throws an Exception for trigger REQUEST")
    fun state_rejected_request() {
        //Arrange
        val testObject =
            Json.decodeFromString<ProofRecord>("{\"id\":\"aafda3e0-c243-4546-9111-171b854825dc\",\"state\":\"REJECTED\",\"typeName\":\"AF.ProofRecord\"}")
        val trigger = ProofTrigger.REQUEST

        //Act

        //Assert
        assertThrows<Exception> { testObject.trigger(trigger) }
    }

    @Test
    @DisplayName("ProofRecord with state REJECTED throws an Exception for trigger ACCEPT")
    fun state_rejected_accept() {
        //Arrange
        val testObject =
            Json.decodeFromString<ProofRecord>("{\"id\":\"aafda3e0-c243-4546-9111-171b854825dc\",\"state\":\"REJECTED\",\"typeName\":\"AF.ProofRecord\"}")
        val trigger = ProofTrigger.ACCEPT

        //Act

        //Assert
        assertThrows<Exception> { testObject.trigger(trigger) }
    }

    @Test
    @DisplayName("ProofRecord with state REJECTED throws an Exception for trigger REJECT")
    fun state_rejected_reject() {
        //Arrange
        val testObject =
            Json.decodeFromString<ProofRecord>("{\"id\":\"aafda3e0-c243-4546-9111-171b854825dc\",\"state\":\"REJECTED\",\"typeName\":\"AF.ProofRecord\"}")
        val trigger = ProofTrigger.REJECT

        //Act

        //Assert
        assertThrows<Exception> { testObject.trigger(trigger) }
    }

    @Test
    @DisplayName("ProofRecord with state ACCEPTED throws an Exception for trigger PROPOSE")
    fun state_accepted_propose() {
        //Arrange
        val testObject =
            Json.decodeFromString<ProofRecord>("{\"id\":\"aafda3e0-c243-4546-9111-171b854825dc\",\"state\":\"ACCEPTED\",\"typeName\":\"AF.ProofRecord\"}")
        val trigger = ProofTrigger.PROPOSE

        //Act

        //Assert
        assertThrows<Exception> { testObject.trigger(trigger) }
    }

    @Test
    @DisplayName("ProofRecord with state ACCEPTED throws an Exception for trigger REQUEST")
    fun state_accepted_request() {
        //Arrange
        val testObject =
            Json.decodeFromString<ProofRecord>("{\"id\":\"aafda3e0-c243-4546-9111-171b854825dc\",\"state\":\"ACCEPTED\",\"typeName\":\"AF.ProofRecord\"}")
        val trigger = ProofTrigger.REQUEST

        //Act

        //Assert
        assertThrows<Exception> { testObject.trigger(trigger) }
    }

    @Test
    @DisplayName("ProofRecord with state ACCEPTED throws an Exception for trigger ACCEPT")
    fun state_accepted_accept() {
        //Arrange
        val testObject =
            Json.decodeFromString<ProofRecord>("{\"id\":\"aafda3e0-c243-4546-9111-171b854825dc\",\"state\":\"ACCEPTED\",\"typeName\":\"AF.ProofRecord\"}")
        val trigger = ProofTrigger.ACCEPT

        //Act

        //Assert
        assertThrows<Exception> { testObject.trigger(trigger) }
    }

    @Test
    @DisplayName("ProofRecord with state ACCEPTED throws an Exception for trigger REJECT")
    fun state_accepted_reject() {
        //Arrange
        val testObject =
            Json.decodeFromString<ProofRecord>("{\"id\":\"aafda3e0-c243-4546-9111-171b854825dc\",\"state\":\"ACCEPTED\",\"typeName\":\"AF.ProofRecord\"}")
        val trigger = ProofTrigger.REJECT

        //Act

        //Assert
        assertThrows<Exception> { testObject.trigger(trigger) }
    }
    //endregion
}