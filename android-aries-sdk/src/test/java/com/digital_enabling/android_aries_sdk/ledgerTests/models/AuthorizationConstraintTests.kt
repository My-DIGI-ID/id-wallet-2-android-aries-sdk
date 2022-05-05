package com.digital_enabling.android_aries_sdk.ledgerTests.models

import com.digital_enabling.android_aries_sdk.ledger.models.AuthorizationConstraint
import com.digital_enabling.android_aries_sdk.ledger.models.ConstraintMetadata
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AuthorizationConstraintTests {

    @Test
    @DisplayName("Serialization works correctly with all properties set.")
    fun serialization_works() {
        val testMetadata = ConstraintMetadata("testFee")
        val testObject =
            AuthorizationConstraint("testRole", true, 1337, testMetadata, "testId", emptyList())

        val expected =
            "{\"role\":\"testRole\",\"need_to_be_owner\":true,\"sig_count\":1337,\"metadata\":{\"fees\":\"testFee\"},\"constraint_id\":\"testId\",\"auth_constraints\":[]}"
        val actual = Json.encodeToString(testObject)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Deserialization works correctly with all properties set.")
    fun deserialization_works() {
        val testJson =
            "{\"role\":\"testRole\",\"need_to_be_owner\":true,\"sig_count\":1337,\"metadata\":{\"fees\":\"testFee\"},\"constraint_id\":\"testId\",\"auth_constraints\":[]}"
        val actualObject = Json.decodeFromString<AuthorizationConstraint>(testJson)

        assertEquals("testRole", actualObject.role)
        assertEquals(true, actualObject.mustBeOwner)
        assertEquals(1337, actualObject.signatureCount)
        assertEquals("testFee", actualObject.metadata.fee)
        assertEquals("testId", actualObject.constraintId)
        assertEquals(emptyList<AuthorizationConstraint>(), actualObject.constraints)
    }
}