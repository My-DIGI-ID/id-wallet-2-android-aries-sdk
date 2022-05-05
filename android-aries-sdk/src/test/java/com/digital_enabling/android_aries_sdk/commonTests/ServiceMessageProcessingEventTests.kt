package com.digital_enabling.android_aries_sdk.commonTests

import com.digital_enabling.android_aries_sdk.common.ServiceMessageProcessingEvent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ServiceMessageProcessingEventTests {
    @Test
    @DisplayName("AriesFrameworkException can be constructed from ErrorCode")
    fun constructor_fromErrorCode() {
        //Arrange
        var testThreadId = "testThreadId"
        var testRecordId = "testRecordId"
        var testMessageType = "testMessageType"
        var testObject = ServiceMessageProcessingEvent(testThreadId, testRecordId, testMessageType)

        //Act
        var actual = testObject.toString()
        var expected =

            //Assert
            Assertions.assertEquals(
                "${testObject::class.simpleName}: ThreadId=$testThreadId, RecordId=$testRecordId, MessageType=$testMessageType",
                actual
            )
    }
}