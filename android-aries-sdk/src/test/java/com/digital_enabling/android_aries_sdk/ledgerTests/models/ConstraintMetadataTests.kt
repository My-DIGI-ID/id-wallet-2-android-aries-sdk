package com.digital_enabling.android_aries_sdk.ledgerTests.models

import com.digital_enabling.android_aries_sdk.ledger.models.ConstraintMetadata
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ConstraintMetadataTests {

    @Test
    @DisplayName("Serialization works correctly with all properties set.")
    fun serialization_works() {
        val testObject = ConstraintMetadata("testFee")

        val expected = "{\"fees\":\"testFee\"}"
        val actual = Json.encodeToString(testObject)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Deserialization works correctly with all properties set.")
    fun deserialization_works() {
        val testJson = "{\"fees\":\"testFee\"}"
        val actualObject = Json.decodeFromString<ConstraintMetadata>(testJson)

        assertEquals("testFee", actualObject.fee)
    }
}