package com.digital_enabling.android_aries_sdk.proofTests.messages

import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.decorators.attachments.Attachment
import com.digital_enabling.android_aries_sdk.decorators.attachments.AttachmentContent
import com.digital_enabling.android_aries_sdk.proof.messages.PresentationMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PresentationMessageTests {
    @Test
    @DisplayName("Serialized PresentationMessage has all keys if the values are not null.")
    fun serialization_works() {
        //Arrange
        val testObject = PresentationMessage()
        testObject.comment = "testComment"
        val testAttachment = Attachment("testAttachmentId")
        val testData = AttachmentContent()
        testAttachment.data = testData
        testObject.presentations = arrayOf(testAttachment)

        //Act
        val serializedObject = Json.encodeToString(testObject)
        val actual = Json.decodeFromString<PresentationMessage>(serializedObject)

        //Assert
        Assertions.assertEquals(testObject.comment, actual.comment)
        Assertions.assertEquals(
            testObject.presentations!!.first().id,
            actual.presentations?.first()?.id
        )
        Assertions.assertEquals(testObject.id, actual.id)
        Assertions.assertEquals(testObject.type, actual.type)
    }

    @Test
    @DisplayName("PresentationMessage has an id when using the primary constructor")
    fun primary_const_has_id() {
        //Arrange
        val testObject = PresentationMessage()

        //Act
        val actual = testObject.id

        //Assert
        Assertions.assertFalse(actual.isNullOrEmpty())
    }

    @Test
    @DisplayName("PresentationMessage has a type when using the primary constructor")
    fun primary_const_has_type() {
        //Arrange
        val testObject = PresentationMessage()

        //Act
        val actual = testObject.type
        val expected = MessageTypes.PresentProofNames.PRESENTATION

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("PresentationMessage has an id when using the secondary constructor")
    fun secondary_const_has_id() {
        //Arrange
        val testObject = PresentationMessage(false)

        //Act
        val actual = testObject.id

        //Assert
        Assertions.assertFalse(actual.isNullOrEmpty())
    }

    @Test
    @DisplayName("PresentationMessage has a type when using the secondary constructor")
    fun secondary_const_has_type() {
        //Arrange
        val testObject = PresentationMessage(false)

        //Act
        val actual = testObject.type
        val expected = MessageTypes.PresentProofNames.PRESENTATION

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("PresentationMessage uses MessageTypesHttps when using the secondary constructor with true")
    fun secondary_const_true() {
        //Arrange
        val testObject = PresentationMessage(true)

        //Act
        val actual = testObject.type
        val expected = MessageTypesHttps.PresentProofNames.PRESENTATION

        //Assert
        Assertions.assertEquals(expected, actual)
    }
}