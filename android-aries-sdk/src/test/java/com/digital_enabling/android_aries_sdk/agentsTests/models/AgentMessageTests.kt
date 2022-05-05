package com.digital_enabling.android_aries_sdk.agentsTests.models

import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import kotlinx.serialization.json.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AgentMessageTests() {
    @Test
    @DisplayName("Serialized AgentMessage has no key useMessageTypesHttps when its false")
    fun serialization_UseMessageTypesHttpsDoesNotExist() {
        //Arrange
        val testObject = AgentMessage()
        testObject.id = "testId"
        testObject.type = "testType"

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject)
        val actual = (serializedObject as JsonObject).containsKey("useMessageTypesHttps")

        //Assert
        Assertions.assertTrue(!actual)
    }

    @Test
    @DisplayName("Serialized AgentMessage has key useMessageTypesHttps when its true")
    fun serialization_UseMessageTypesHttpsExist() {
        //Arrange
        val testObject = AgentMessage(true)
        testObject.id = "testId"
        testObject.type = "testType"

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject)
        val actual = (serializedObject as JsonObject).containsKey("useMessageTypesHttps")

        //Assert
        Assertions.assertTrue(actual)
    }

    @Test
    @DisplayName("Serialized AgentMessage has key id")
    fun serialization_IdExists() {
        //Arrange
        val testObject = AgentMessage()
        testObject.id = "testId"
        testObject.type = "testType"

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject)
        val actual = (serializedObject as JsonObject).containsKey("@id")

        //Assert
        Assertions.assertTrue(actual)
    }

    @Test
    @DisplayName("Serialized AgentMessage has key type")
    fun serialization_TypeExists() {
        //Arrange
        val testObject = AgentMessage()
        testObject.id = "testId"
        testObject.type = "testType"

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject)
        val actual = (serializedObject as JsonObject).containsKey("@type")

        //Assert
        Assertions.assertTrue(actual)
    }

    @Test
    @DisplayName("Serialized AgentMessage has no key decorators")
    fun serialization_DecoratorsDoesNotExist() {
        //Arrange
        val testObject = AgentMessage()
        testObject.id = "testId"
        testObject.type = "testType"

        //Act
        val serializedObject = Json.encodeToJsonElement(testObject)
        val actual = (serializedObject as JsonObject).containsKey("decorators")

        //Assert
        Assertions.assertFalse(actual)
    }

    @Test
    @DisplayName("setDecorators works correctly with a JsonArray as input")
    fun setDecorators_jsonArray() {
        //Arrange
        val testObject = AgentMessage()
        val testElement = "testElement"
        val inputArray = JsonArray(listOf(Json.encodeToJsonElement(testElement)))

        //Act
        testObject.setDecorators(inputArray)
        val actual = testObject.getDecorators()[0]
        val expected = Json.encodeToJsonElement(testElement)

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("setDecorators works correctly with a List of JsonElements as input")
    fun setDecorators_listOfJsonElement() {
        //Arrange
        val testObject = AgentMessage()
        val testElement = "testElement"
        val inputArray = listOf(Json.encodeToJsonElement(testElement))
        testObject.setDecorators(inputArray)

        //Act
        val actual = testObject.getDecorators()[0]
        val expected = Json.encodeToJsonElement(testElement)

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("getDecorators works correctly")
    fun getDecorators_works() {
        //Arrange
        val testObject = AgentMessage()
        val testElement = "testElement"
        val inputArray = listOf(Json.encodeToJsonElement(testElement))
        testObject.setDecorators(inputArray)

        //Act
        val actual = testObject.getDecorators()
        val expected = inputArray

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("getDecorator works correctly when the given key exists and the type is correct")
    fun getDecorator_works() {
        //Arrange
        val testObject = AgentMessage()

        val testMap1 = LinkedHashMap<String, JsonElement>()
        val testKey1 = "~testKey1"
        val testValue1 = "testValue1"
        testMap1[testKey1] = Json.encodeToJsonElement(testValue1)

        val testMap2 = LinkedHashMap<String, JsonElement>()
        val testKey2 = "~testKey2"
        val testValue2 = 2
        testMap2[testKey2] = Json.encodeToJsonElement(testValue2)

        val inputArray = listOf(JsonObject(testMap1), JsonObject(testMap2))
        testObject.setDecorators(inputArray)

        //Act
        val actual1 = testObject.getDecorator<String>("testKey1")
        val expected1 = testValue1

        val actual2 = testObject.getDecorator<Int>("testKey2")
        val expected2 = testValue2

        //Assert
        Assertions.assertEquals(expected1, actual1)
        Assertions.assertEquals(expected2, actual2)
    }

    @Test
    @DisplayName("getDecorator throws an Exception when the given key doesn't exist")
    fun getDecorator_wrongKey() {
        //Arrange
        val testObject = AgentMessage()

        val testMap = LinkedHashMap<String, JsonElement>()
        val testKey = "testKey"
        val testValue = "testValue"
        testMap[testKey] = Json.encodeToJsonElement(testValue)
        val testKey2 = "testKey2"

        val inputArray = listOf(JsonObject(testMap))
        testObject.setDecorators(inputArray)

        //Act

        //Assert
        assertThrows<AriesFrameworkException> { testObject.getDecorator<String>(testKey2) }
    }

    @Test
    @DisplayName("getDecorator throws an Exception when the given type is incorrect")
    fun getDecorator_wrongType() {
        //Arrange
        val testObject = AgentMessage()

        val testMap = LinkedHashMap<String, JsonElement>()
        val testKey = "testKey"
        val testValue = "testValue"
        testMap[testKey] = Json.encodeToJsonElement(testValue)

        val inputArray = listOf(JsonObject(testMap))
        testObject.setDecorators(inputArray)

        //Act

        //Assert
        assertThrows<AriesFrameworkException> { testObject.getDecorator<Int>(testKey) }
    }

    @Test
    @DisplayName("findDecorator works correctly when the given key exists and the type is correct")
    fun findDecorator_works() {
        //Arrange
        val testObject = AgentMessage()

        val testMap1 = LinkedHashMap<String, JsonElement>()
        val testKey1 = "~testKey1"
        val testValue1 = "testValue1"
        testMap1[testKey1] = Json.encodeToJsonElement(testValue1)

        val testMap2 = LinkedHashMap<String, JsonElement>()
        val testKey2 = "~testKey2"
        val testValue2 = 2
        testMap2[testKey2] = Json.encodeToJsonElement(testValue2)

        val inputArray = listOf(JsonObject(testMap1), JsonObject(testMap2))
        testObject.setDecorators(inputArray)

        //Act
        val actual1 = testObject.findDecorator<String>("testKey1")
        val expected1 = testValue1

        val actual2 = testObject.findDecorator<Int>("testKey2")
        val expected2 = testValue2

        //Assert
        Assertions.assertEquals(expected1, actual1)
        Assertions.assertEquals(expected2, actual2)
    }

    @Test
    @DisplayName("findDecorator returns null when the given key doesn't exist")
    fun findDecorator_wrongKey() {
        //Arrange
        val testObject = AgentMessage()

        val testMap = LinkedHashMap<String, JsonElement>()
        val testKey = "testKey"
        val testValue = "testValue"
        testMap[testKey] = Json.encodeToJsonElement(testValue)
        val testKey2 = "testKey2"

        val inputArray = listOf(JsonObject(testMap))
        testObject.setDecorators(inputArray)

        //Act
        val actual = testObject.findDecorator<String>(testKey2)
        val expected = null

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("findDecorator returns null when the given type is incorrect")
    fun findDecorator_wrongType() {
        //Arrange
        val testObject = AgentMessage()

        val testMap = LinkedHashMap<String, JsonElement>()
        val testKey = "testKey"
        val testValue = "testValue"
        testMap[testKey] = Json.encodeToJsonElement(testValue)

        val inputArray = listOf(JsonObject(testMap))
        testObject.setDecorators(inputArray)

        //Act
        val actual = testObject.findDecorator<Int>(testKey)
        val expected = null

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("addDecorator works correctly")
    fun addDecorator_works() {
        //Arrange
        val testObject = AgentMessage()
        testObject.addDecorator("testValue", "testKey")

        //Act
        val actual = testObject.findDecorator<String>("testKey")
        val expected = "testValue"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("addDecorator throws an Exception if the decorator is not serializable")
    fun addDecorator_nonSerializable() {
        //Arrange
        class TestClass{
            val name : String = "testName"
        }
        val testDecorator = TestClass()
        val testObject = AgentMessage()

        //Act

        //Assert
        assertThrows<AriesFrameworkException> { testObject.addDecorator(testDecorator, "testKey") }
    }

    @Test
    @DisplayName("setDecorator adds a new decorator if the key doesn't exist")
    fun setDecorator_newKey() {
        //Arrange
        val testObject = AgentMessage()
        testObject.setDecorator("testValue", "testKey")

        //Act
        val actual = testObject.findDecorator<String>("testKey")
        val expected = "testValue"

        //Assert
        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("setDecorator overrides an old decorator if the key already exists")
    fun setDecorator_keyExists() {
        //Arrange
        val testObject = AgentMessage()
        testObject.addDecorator("testValueOld", "testKey")
        testObject.setDecorator("testValueNew", "testKey")

        //Act
        val actual = testObject.findDecorator<String>("testKey")
        val expected = "testValueNew"

        //Assert
        Assertions.assertEquals(expected, actual)
    }
}