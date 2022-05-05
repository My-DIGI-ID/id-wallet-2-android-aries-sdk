package com.digital_enabling.android_aries_sdk.credentialTests.records

import com.digital_enabling.android_aries_sdk.credential.records.SchemaRecord
import com.digital_enabling.android_aries_sdk.utils.RecordType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SchemaRecordTests {

    @Test
    @DisplayName("Constructor of SchemaRecord works.")
    fun constructor_works() {
        val testObject = SchemaRecord("testId")
        testObject.name = "testName"
        testObject.version = "testVersion"
        val testAttributeNames = arrayListOf<String>("testAttribute1", "testAttribute2")
        testObject.attributeNames = testAttributeNames

        Assertions.assertEquals("testId", testObject.id)
        Assertions.assertEquals(RecordType.SCHEMA_RECORD.typeName, testObject.typeName)
        Assertions.assertEquals("testName", testObject.name)
        Assertions.assertEquals("testVersion", testObject.version)
        Assertions.assertEquals(testAttributeNames, testObject.attributeNames)
    }

    @Test
    @DisplayName("")
    fun toString_works(){
        val testObject = SchemaRecord("testId")
        testObject.name = "testName"
        testObject.version = "testVersion"
        val testAttributeNames = arrayListOf("testAttribute1", "testAttribute2")
        testObject.attributeNames = testAttributeNames

        val actual = testObject.toString()
        val expected = "SchemaRecord: Name=testName, Version=testVersion, AttributeNames=testAttribute1,testAttribute2, SchemaRecord: Id=testId, TypeName=AF.SchemaRecord, CreatedAtUtc=null, UpdatedAtUtc=null"

        Assertions.assertEquals(expected, actual)
    }
}