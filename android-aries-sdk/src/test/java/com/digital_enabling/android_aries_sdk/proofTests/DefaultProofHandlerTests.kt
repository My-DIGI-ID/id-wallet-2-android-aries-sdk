package com.digital_enabling.android_aries_sdk.proofTests

import com.digital_enabling.android_aries_sdk.agents.MessageType
import com.digital_enabling.android_aries_sdk.agents.MessageTypes
import com.digital_enabling.android_aries_sdk.agents.MessageTypesHttps
import com.digital_enabling.android_aries_sdk.proof.DefaultProofHandler
import com.digital_enabling.android_aries_sdk.proof.IProofService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class DefaultProofHandlerTests {
    private val mockProofService = Mockito.mock(IProofService::class.java)

    //region messagetypes tests
    @Test
    @DisplayName("DefaultProofHandler supported MessageTypes contains MessageTypes.PresentProofNames.PROPOSE_PRESENTATION")
    fun messagetypes_http_propose() {
        //Arrange
        val testObject = DefaultProofHandler(mockProofService)
        val expectedUri = MessageType(MessageTypes.PresentProofNames.PROPOSE_PRESENTATION).baseUri

        //Act

        //Assert
        Assertions.assertTrue(testObject.supportedMessageTypes.find { x -> x.baseUri == expectedUri } != null)
    }

    @Test
    @DisplayName("DefaultProofHandler supported MessageTypes contains MessageTypes.PresentProofNames.REQUEST_PRESENTATION")
    fun messagetypes_http_request() {
        //Arrange
        val testObject = DefaultProofHandler(mockProofService)
        val expectedUri = MessageType(MessageTypes.PresentProofNames.REQUEST_PRESENTATION).baseUri

        //Act

        //Assert
        Assertions.assertTrue(testObject.supportedMessageTypes.find { x -> x.baseUri == expectedUri } != null)
    }

    @Test
    @DisplayName("DefaultProofHandler supported MessageTypes contains MessageTypes.PresentProofNames.PRESENTATION")
    fun messagetypes_http_presentation() {
        //Arrange
        val testObject = DefaultProofHandler(mockProofService)
        val expectedUri = MessageType(MessageTypes.PresentProofNames.PRESENTATION).baseUri

        //Act

        //Assert
        Assertions.assertTrue(testObject.supportedMessageTypes.find { x -> x.baseUri == expectedUri } != null)
    }

    @Test
    @DisplayName("DefaultProofHandler supported MessageTypes contains MessageTypesHttps.PresentProofNames.PROPOSE_PRESENTATION")
    fun messagetypes_https_propose() {
        //Arrange
        val testObject = DefaultProofHandler(mockProofService)
        val expectedUri = MessageType(MessageTypesHttps.PresentProofNames.PROPOSE_PRESENTATION).baseUri

        //Act

        //Assert
        Assertions.assertTrue(testObject.supportedMessageTypes.find { x -> x.baseUri == expectedUri } != null)
    }

    @Test
    @DisplayName("DefaultProofHandler supported MessageTypes contains MessageTypesHttps.PresentProofNames.REQUEST_PRESENTATION")
    fun messagetypes_https_request() {
        //Arrange
        val testObject = DefaultProofHandler(mockProofService)
        val expectedUri = MessageType(MessageTypesHttps.PresentProofNames.REQUEST_PRESENTATION).baseUri

        //Act

        //Assert
        Assertions.assertTrue(testObject.supportedMessageTypes.find { x -> x.baseUri == expectedUri } != null)
    }

    @Test
    @DisplayName("DefaultProofHandler supported MessageTypes contains MessageTypesHttps.PresentProofNames.PRESENTATION")
    fun messagetypes_https_presentation() {
        //Arrange
        val testObject = DefaultProofHandler(mockProofService)
        val expectedUri = MessageType(MessageTypesHttps.PresentProofNames.PRESENTATION).baseUri

        //Act

        //Assert
        Assertions.assertTrue(testObject.supportedMessageTypes.find { x -> x.baseUri == expectedUri } != null)
    }
    //endregion

    //region process tests

    //endregion
}