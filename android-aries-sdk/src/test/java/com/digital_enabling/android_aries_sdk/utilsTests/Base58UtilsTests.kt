package com.digital_enabling.android_aries_sdk.utilsTests

import com.digital_enabling.android_aries_sdk.utils.Base58Utils
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class Base58UtilsTests {

    //region Tests for byteArrayOfInts
    @Test
    @DisplayName("ByteArrayOfInts method returns empty array if given no inputs.")
    fun byteArrayOfInts_emptyArray(){
        val actual = Base58Utils.byteArrayOfInts()
        val expected = ByteArray(0)

        assertArrayEquals(expected, actual)
    }

    @Test
    @DisplayName("ByteArrayOfInts method returns proper converted ByteArray if given some input.")
    fun byteArrayOfInts_simpleArray(){
        val actual = Base58Utils.byteArrayOfInts(1, 2, 3)
        val expected = arrayOf(1.toByte(), 2.toByte(), 3.toByte()).toByteArray()

        assertArrayEquals(expected, actual)
    }
    //endregion

    //region Tests for decodeBase58
    @Test
    @DisplayName("DecodeBase58 method returns empty ByteArray if given empty String.")
    fun decodeBase58_emptyString(){
        val expected = ByteArray(0)
        val actual = Base58Utils.decodeBase58("")

        assertArrayEquals(expected, actual)
    }

    @Test
    @DisplayName("DecodeBase85 method returns ... if given proper String.")
    fun decodeBase58_normalString(){
        val expected = "testtest".toByteArray()
        val actual = Base58Utils.decodeBase58("LUC1e9TnauZ")

        assertArrayEquals(expected, actual)
    }
    //endregion

    //region Tests for encodeBase58
    @Test
    @DisplayName("EncodeBase58 method returns empty String if given empty Byte Array.")
    fun encodeBase58_emptyBytes(){
        val expected = ""
        val actual = Base58Utils.encodeBase58(ByteArray(0))

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("EncodeBase58 method returns correctly encoded String if given proper Byte Array.")
    fun encodeBase58_normalBytes(){
        val expected = "LUC1e9TnauZ"
        val actual = Base58Utils.encodeBase58("testtest".toByteArray())

        assertEquals(expected, actual)
    }
    //endregion
}