package com.digital_enabling.android_aries_sdk.decoratorsTests

import com.digital_enabling.android_aries_sdk.decorators.attachments.AttachDecorator
import com.digital_enabling.android_aries_sdk.decorators.attachments.Attachment
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AttachDecoratorTests {
    @Test
    @DisplayName("AttachDecorator get operator returns attachment if the given name exists")
    fun get_NameExists() {
        //Arrange
        val testAttachment = Attachment("testId")
        testAttachment.nickname = "testNickname"
        val testObject = AttachDecorator()
        testObject.add(testAttachment)

        //Act
        val actual = testObject["testNickname"]
        val expected = testAttachment

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("AttachDecorator get operator returns null if there is no attachment with given name")
    fun get_NameExistsNot() {
        //Arrange
        val testAttachment = Attachment("testId")
        testAttachment.nickname = "testNickname"
        val testObject = AttachDecorator()
        testObject.add(testAttachment)

        //Act
        val actual = testObject["someOtherName"]
        val expected = null

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("AttachDecorator get operator returns attachment if the given attachment exists")
    fun get_AttachmentExists() {
        //Arrange
        val testAttachment = Attachment("testId")
        testAttachment.nickname = "testNickname"
        val testObject = AttachDecorator()
        testObject.add(testAttachment)

        //Act
        val actual = testObject[testAttachment]
        val expected = testAttachment

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("AttachDecorator get operator returns null if the given attachment does not exist")
    fun get_AttachmentExistsNot() {
        //Arrange
        val testAttachment = Attachment("testId")
        testAttachment.nickname = "testNickname"
        val testAttachment2 = Attachment("testId2")
        testAttachment2.nickname = "testNickname2"
        val testObject = AttachDecorator()
        testObject.add(testAttachment)

        //Act
        val actual = testObject[testAttachment2]
        val expected = null

        //Assert
        Assertions.assertEquals(expected, actual)
    }
}