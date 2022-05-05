package com.digital_enabling.android_aries_sdk.ledgerTests.models

import com.digital_enabling.android_aries_sdk.ledger.models.AuthorizationConstraint
import com.digital_enabling.android_aries_sdk.ledger.models.AuthorizationRule
import com.digital_enabling.android_aries_sdk.ledger.models.ConstraintMetadata
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AuthorizationRuleTests {
    @Test
    @DisplayName("Serialization works correctly with all properties set.")
    fun serialization_works(){
        val testMetadata = ConstraintMetadata("testFee")
        val testConstraint = AuthorizationConstraint("testRole", true, 1337, testMetadata, "testId", emptyList())
        val testObject = AuthorizationRule("testType", "testNewValue", "testOldValue", "testField", "testAction", testConstraint)

        val expected = "{\"auth_type\":\"testType\",\"new_value\":\"testNewValue\",\"old_value\":\"testOldValue\",\"field\":\"testField\",\"auth_action\":\"testAction\",\"constraint\":{\"role\":\"testRole\",\"need_to_be_owner\":true,\"sig_count\":1337,\"metadata\":{\"fees\":\"testFee\"},\"constraint_id\":\"testId\",\"auth_constraints\":[]}}"
        val actual = Json.encodeToString(testObject)

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Deserialization works correctly with all properties set.")
    fun deserialization_works(){
        val testJson = "{\"auth_type\":\"testType\",\"new_value\":\"testNewValue\",\"old_value\":\"testOldValue\",\"field\":\"testField\",\"auth_action\":\"testAction\",\"constraint\":{\"role\":\"testRole\",\"need_to_be_owner\":true,\"sig_count\":1337,\"metadata\":{\"fees\":\"testFee\"},\"constraint_id\":\"testId\",\"auth_constraints\":[]}}"
        val actualObject = Json.decodeFromString<AuthorizationRule>(testJson)

        assertEquals("testType", actualObject.transactionType)
        assertEquals("testNewValue", actualObject.newValue)
        assertEquals("testOldValue", actualObject.oldValue)
        assertEquals("testField", actualObject.field)
        assertEquals("testAction", actualObject.action)
        assertEquals("testRole", actualObject.constraint.role)
    }
}