package com.digital_enabling.android_aries_sdk.utilsTests

import com.digital_enabling.android_aries_sdk.utils.DidUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DidUtilsTests {
    //region Tests for toDid()
    @Test
    @DisplayName("ToDid method creates a did string with empty elements if given empty Strings.")
    fun toDid_emptyMethodSpecEmptyIdentifier() {
        val expected = "did::"
        val actual = DidUtils.toDid("", "")

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("ToDid method creates a did string with set elements if given proper Strings.")
    fun toDid_setMethodSpecSetIdentifier() {
        val expected = "did:testspec:testidentifier"
        val actual = DidUtils.toDid("testspec", "testidentifier")

        assertEquals(expected, actual)
    }
    //endregion

    //region Tests for identifierFromDid()
    @Test
    @DisplayName("IdentifierFromDid method returns null if given empty String.")
    fun identifierFromDid_emptyString() {
        val expected = null
        val actual = DidUtils.identifierFromDid("")

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("IdentifierFromDid method returns null if given String does not match an identifier.")
    fun identifierFromDid_noDid() {
        val expected = null
        val actual = DidUtils.identifierFromDid("did:testspec:")

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("IdentifierFromDid method returns correct identifier from given did String.")
    fun identifierFromDid_properDid() {
        val expected = "testidentifier"
        val actual = DidUtils.identifierFromDid("did:testspec:testidentifier")

        assertEquals(expected, actual)
    }
    //endregion

    //region Tests for isFullVerkey()
    @Test
    @DisplayName("IsFullVerkey method returns false if given empty String.")
    fun isFullVerkey_emptyString() {
        val expected = false
        val actual = DidUtils.isFullVerkey("")

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("IsFullVerkey method returns true if given proper verkey.")
    fun isFullVerkey_actualVerkey() {
        val expected = true
        val actual = DidUtils.isFullVerkey("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("IsFullVerkey method returns false if given verkey in wrong format.")
    fun isFullVerkey_wrongVerkey() {
        val expected = false
        val actual = DidUtils.isFullVerkey("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")

        assertEquals(expected, actual)
    }
    //endregion

    //region Tests for isAbbreviatedVerkey()
    @Test
    @DisplayName("IsAbbreviatedVerkey method retuns false if given empty String.")
    fun isAbbreviatedVerkey_emptyString() {
        val expected = false
        val actual = DidUtils.isAbbreviatedVerkey("")

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("IsAbbreviatedVerkey method returns true if given matching key.")
    fun isAbbreviatedVerkey_actualVerkey() {
        val expected = true
        val actual = DidUtils.isAbbreviatedVerkey("~aaaaaaaaaaaaaaaaaaaaaa")

        assertEquals(expected, actual)

    }

    @Test
    @DisplayName("IsAbbreviatedVerkey method retuns false if given key in wrong format.")
    fun isAbbreviatedVerkey_wrongVerkey() {
        val expected = false
        val actual = DidUtils.isAbbreviatedVerkey("aaaaaaaaaaaaaaaaaaaaaa")

        assertEquals(expected, actual)

    }
    //endregion

    //region Tests for isVerkey()
    @Test
    @DisplayName("IsVerkey method returns false if given empty String.")
    fun isVerkey_emptyString() {
        val expected = false
        val actual = DidUtils.isVerkey("")

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("IsVerkey method returns true if given full verkey.")
    fun isVerkey_fullVerkey() {
        val expected = true
        val actual = DidUtils.isVerkey("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("IsVerkey method returns true if given abbreviated verkey.")
    fun isVerkey_abbreviatedVerkey() {
        val expected = true
        val actual = DidUtils.isVerkey("~aaaaaaaaaaaaaaaaaaaaaa")

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("IsVerkey method returns false if given verkey in wrong format.")
    fun isVerkey_wrongVerkey() {
        val expected = false
        val actual = DidUtils.isVerkey("aaa")

        assertEquals(expected, actual)
    }
    //endregion

    //region Tests for isDidKey()
    @Test
    @DisplayName("IsDidKey method returns false if given empty String.")
    fun isDidKey_emptyString() {
        val expected = false
        val actual = DidUtils.isDidKey("")

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("IsDidKey method returns true if given correct didKey.")
    fun isDidKey_actualKey() {
        val expected = true
        val actual = DidUtils.isDidKey("did:key:test")

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("IsDidKey method returns false if given key in wrong format.")
    fun isDidKey_wrongKey() {
        val expected = false
        val actual = DidUtils.isDidKey("did:test:test")

        assertEquals(expected, actual)
    }
    //endregion

    //region Tests for convertVerkeyToDidKey()
    @Test
    @DisplayName("ConvertVerkeyToDidKey method throws IllegalArgumentException if given empty or wrong String.")
    fun convertVerkeyToDidKey_emptyString() {
        assertThrows<IllegalArgumentException> { DidUtils.convertVerkeyToDidKey("") }
    }

    @Test
    @DisplayName("ConvertVerkeyToDidKey method returns correct didKey if given proper verkey.")
    fun convertVerkeyToDidKey_actualKey() {
        val expected = "did:key:zQebetUz1jUJQqaEFFsY9E54BH5onZSYfzmwuCcCBf3pdUzsg"
        val actual = DidUtils.convertVerkeyToDidKey("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")

        assertEquals(expected, actual)
    }
    //endregion

    //region Tests for convertDidKeyToVerkey()
    @Test
    @DisplayName("ConvertDidKeyToVerkey method throws IllegalArgumentException if given empty or wrong String.")
    fun convertDidKeyToVerkey_emptyString() {
        assertThrows<IllegalArgumentException> { DidUtils.convertDidKeyToVerkey("") }
    }

    @Test
    @DisplayName("ConvertDidKeyToVerkey method throws IllegalArgumentException if given didKey without Multicodec prefix.")
    fun convertDidKeyToVerkey_missingPrefix() {
        assertThrows<IllegalArgumentException> { DidUtils.convertDidKeyToVerkey("did:key:QebetUz1jUJQqaEFFsY9E54BH5onZSYfzmwuCcCBf3pdUzsg") }
    }

    @Test
    @DisplayName("ConvertDidKeyToVerkey method returns proper verkey if given actual didKey.")
    fun convertDidKeyToVerkey_actualKey() {
        val expected = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        val actual =
            DidUtils.convertDidKeyToVerkey("did:key:zQebetUz1jUJQqaEFFsY9E54BH5onZSYfzmwuCcCBf3pdUzsg")

        assertEquals(expected, actual)
    }
    //endregion
}