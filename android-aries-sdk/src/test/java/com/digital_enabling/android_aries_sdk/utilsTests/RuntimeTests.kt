package com.digital_enabling.android_aries_sdk.utilsTests

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RuntimeTests {

    @Test
    @DisplayName("SetFlags method adds nothing to flag list if given list is empty.")
    fun setFlags_emptyList(){
        com.digital_enabling.android_aries_sdk.utils.Runtime.setFlags(emptyList())

        val expected = emptyList<String>()
        val actual = com.digital_enabling.android_aries_sdk.utils.Runtime.usedFlags

        assertTrue(expected == actual)
    }

    @Test
    @DisplayName("SetFlags method adds the given flags to list.")
    fun setFlags_normalList(){
        com.digital_enabling.android_aries_sdk.utils.Runtime.setFlags(listOf("testFlag1", "testFlag2", "testFlag3"))

        val expected = listOf("testFlag1", "testFlag2", "testFlag3")
        val actual = com.digital_enabling.android_aries_sdk.utils.Runtime.usedFlags

        assertTrue(expected == actual)
    }

}