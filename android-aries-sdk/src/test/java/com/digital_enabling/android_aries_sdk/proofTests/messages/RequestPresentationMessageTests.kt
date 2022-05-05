package com.digital_enabling.android_aries_sdk.proofTests.messages

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.decorators.attachments.Attachment
import com.digital_enabling.android_aries_sdk.decorators.attachments.AttachmentContent
import com.digital_enabling.android_aries_sdk.proof.messages.RequestPresentationMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RequestPresentationMessageTests {
    @Test
    @DisplayName("Serialized RequestPresentationMessage has all keys if the values are not null.")
    fun serialization_works() {
        //Arrange
        val testObject = RequestPresentationMessage()
        testObject.comment = "testComment"
        val testAttachment = Attachment("testAttachmentId")
        val testData = AttachmentContent()
        testAttachment.data = testData
        testObject.requests = arrayOf(testAttachment)

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<RequestPresentationMessage>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.comment, actual.comment)
        Assertions.assertEquals(
            testObject.requests!!.first().id,
            actual.requests?.first()?.id
        )
        Assertions.assertEquals(testObject.id, actual.id)
        Assertions.assertEquals(testObject.type, actual.type)
    }

    @Test
    @DisplayName("RequestPresentationMessage has an id when using the primary constructor")
    fun primary_const_has_id() {
        //Arrange
        val testObject = RequestPresentationMessage()

        //Act
        val actual = testObject.id

        //Assert
        Assertions.assertFalse(actual.isNullOrEmpty())
    }

    @Test
    @DisplayName("RequestPresentationMessage has a type when using the primary constructor")
    fun primary_const_has_type() {
        //Arrange
        val testObject = RequestPresentationMessage()

        //Act
        val actual = testObject.type
        val expected = MessageTypes.PresentProofNames.REQUEST_PRESENTATION

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("RequestPresentationMessage has an id when using the secondary constructor")
    fun secondary_const_has_id() {
        //Arrange
        val testObject = RequestPresentationMessage(false)

        //Act
        val actual = testObject.id

        //Assert
        Assertions.assertFalse(actual.isNullOrEmpty())
    }

    @Test
    @DisplayName("RequestPresentationMessage has a type when using the secondary constructor")
    fun secondary_const_has_type() {
        //Arrange
        val testObject = RequestPresentationMessage(false)

        //Act
        val actual = testObject.type
        val expected = MessageTypes.PresentProofNames.REQUEST_PRESENTATION

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("RequestPresentationMessage uses MessageTypesHttps when using the secondary constructor with true")
    fun secondary_const_true() {
        //Arrange
        val testObject = RequestPresentationMessage(true)

        //Act
        val actual = testObject.type
        val expected = MessageTypesHttps.PresentProofNames.REQUEST_PRESENTATION

        //Assert
        Assertions.assertEquals(expected, actual)
    }
}