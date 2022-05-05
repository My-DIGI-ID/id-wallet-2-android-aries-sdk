package com.digital_enabling.android_aries_sdk.credentialTests.records

import com.digital_enabling.android_aries_sdk.credential.records.RevocationRegistryRecord
import com.digital_enabling.android_aries_sdk.credential.records.SchemaRecord
import com.digital_enabling.android_aries_sdk.utils.RecordType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RevocationRegistryRecordTests {
    @Test
    @DisplayName("Constructor of RevocationRegistryRecord works.")
    fun constructor_works() {
        val testObject = RevocationRegistryRecord("testId")
        testObject.credentialDefinitionId = "testCredentialDefinitionId"
        testObject.tailsFile = "testTailsFile"
        testObject.tailsLocation = "testLocation"

        Assertions.assertEquals("testId", testObject.id)
        Assertions.assertEquals(RecordType.REVOCATION_REGISTRY_RECORD.typeName, testObject.typeName)
        Assertions.assertEquals("testCredentialDefinitionId", testObject.credentialDefinitionId)
        Assertions.assertEquals("testTailsFile", testObject.tailsFile)
        Assertions.assertEquals("testLocation", testObject.tailsLocation)
    }
}