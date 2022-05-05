package com.digital_enabling.android_aries_sdk.commonTests

import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AriesFrameworkExceptionTests {
    @Test
    @DisplayName("AriesFrameworkException can be constructed from ErrorCode")
    fun constructor_fromErrorCode() {
        //Arrange
        var testErrorCode = ErrorCode.INVALID_MESSAGE
        var testObject = AriesFrameworkException(testErrorCode)

        //Act

        //Assert
        Assertions.assertEquals(testErrorCode, testObject.errorCode)
        Assertions.assertEquals(
            "Framework error occurred. Code: {$testErrorCode}",
            testObject.message
        )
        Assertions.assertEquals(null, testObject.contextRecord)
        Assertions.assertEquals(null, testObject.contextRecordId)
        Assertions.assertEquals(null, testObject.connectionRecord)
    }

    @Test
    @DisplayName("AriesFrameworkException can be constructed from ErrorCode and String")
    fun constructor_fromErrorCodeAndString() {
        //Arrange
        var testErrorCode = ErrorCode.INVALID_MESSAGE
        var testErrorMessage = "testMessage"
        var testObject = AriesFrameworkException(testErrorCode, testErrorMessage)

        //Act

        //Assert
        Assertions.assertEquals(testErrorCode, testObject.errorCode)
        Assertions.assertEquals(testErrorMessage, testObject.message)
        Assertions.assertEquals(null, testObject.contextRecord)
        Assertions.assertEquals(null, testObject.contextRecordId)
        Assertions.assertEquals(null, testObject.connectionRecord)
    }

    @Test
    @DisplayName("AriesFrameworkException can be constructed from ErrorCode and String and Throwable")
    fun constructor_fromErrorCodeAndStringAndThrowable() {
        //Arrange
        var testErrorCode = ErrorCode.INVALID_MESSAGE
        var testErrorMessage = "testMessage"
        var testInnerException = Exception("testInnerExceptionMessage")
        var testObject =
            AriesFrameworkException(testErrorCode, testErrorMessage, testInnerException)

        //Act

        //Assert
        Assertions.assertEquals(testErrorCode, testObject.errorCode)
        Assertions.assertEquals(testErrorMessage, testObject.message)
        Assertions.assertEquals(testInnerException, testObject.cause)
        Assertions.assertEquals(null, testObject.contextRecord)
        Assertions.assertEquals(null, testObject.contextRecordId)
        Assertions.assertEquals(null, testObject.connectionRecord)
    }

    @Test
    @DisplayName("AriesFrameworkException can be constructed from ErrorCode and StringArray")
    fun constructor_fromErrorCodeAndStringArray() {
        //Arrange
        var testErrorCode = ErrorCode.INVALID_MESSAGE
        var testErrorMessages = arrayOf("testMessage1", "testMessage2", "testMessage3")
        var testObject = AriesFrameworkException(testErrorCode, testErrorMessages)

        //Act
        var expectedMessage = testErrorMessages.joinToString("\n")

        //Assert
        Assertions.assertEquals(testErrorCode, testObject.errorCode)
        Assertions.assertEquals(expectedMessage, testObject.message)
        Assertions.assertEquals(null, testObject.contextRecord)
        Assertions.assertEquals(null, testObject.contextRecordId)
        Assertions.assertEquals(null, testObject.connectionRecord)
    }

    @Test
    @DisplayName("AriesFrameworkException can be constructed from ErrorCode, String, RecordBase and ConnectionRecord")
    fun constructor_fromErrorCodeAndStringAndRecordBaseAndConnectionRecord() {
        //Arrange
        var testErrorCode = ErrorCode.INVALID_MESSAGE
        var testErrorMessage = "testMessage"
        var testContextRecord = ConnectionRecord("testContextRecord")
        var testConnectionRecord = ConnectionRecord("testConnectionRecord")
        var testObject = AriesFrameworkException(
            testErrorCode,
            testErrorMessage,
            testContextRecord,
            testConnectionRecord
        )

        //Act

        //Assert
        Assertions.assertEquals(testErrorCode, testObject.errorCode)
        Assertions.assertEquals(testErrorMessage, testObject.message)
        Assertions.assertEquals(testContextRecord, testObject.contextRecord)
        Assertions.assertEquals(testConnectionRecord, testObject.connectionRecord)
        Assertions.assertEquals(null, testObject.contextRecordId)
    }

    @Test
    @DisplayName("AriesFrameworkException can be constructed from ErrorCode and String and String")
    fun constructor_fromErrorCodeAndStringAndString() {
        //Arrange
        var testErrorCode = ErrorCode.INVALID_MESSAGE
        var testErrorMessage = "testMessage"
        var testContextRecordId = "testContextRecordId"
        var testObject =
            AriesFrameworkException(testErrorCode, testErrorMessage, testContextRecordId)

        //Act

        //Assert
        Assertions.assertEquals(testErrorCode, testObject.errorCode)
        Assertions.assertEquals(testErrorMessage, testObject.message)
        Assertions.assertEquals(testContextRecordId, testObject.contextRecordId)
        Assertions.assertEquals(null, testObject.contextRecord)
        Assertions.assertEquals(null, testObject.connectionRecord)
    }
}