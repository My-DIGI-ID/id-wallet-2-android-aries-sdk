package com.digital_enabling.android_aries_sdk.agentsTests.models

import com.digital_enabling.android_aries_sdk.agents.models.AgentOwner
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AgentOwnerTests {
    @Test
    @DisplayName("toString works correctly")
    fun toString_works() {
        //Arrange
        val testObject = AgentOwner("testName", "testImageUrl")

        //Act
        val actual = testObject.toString()
        val expected = "com.digital_enabling.android_aries_sdk.agents.models.AgentOwner: Name=testName, ImageUrl=testImageUrl"

        //Assert
        Assertions.assertEquals(expected, actual)
    }
}