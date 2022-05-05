package com.digital_enabling.android_aries_sdk.utils

import okio.internal.commonAsUtf8ToByteArray
import java.util.*
/**
 * Encodes and Decodes strings as Base64Url encoding.
 */
class Base64UrlEncoder {
    companion object {
        private const val base64PadCharacter: Char = '='
        private const val doubleBase64PadCharacter: String = "=="
        private const val base64Character62: Char = '+'
        private const val base64Character63: Char = '/'
        private const val base64UrlCharacter62: Char = '-'
        private const val base64UrlCharacter63: Char = '_'

        /**
         * The following functions perform base64url encoding which differs from regular base64 encoding as follows
         * padding is skipped so the pad character '=' doesn't have to be percent encoded
         * the 62nd and 63rd regular base64 encoding characters ('+' and '/') are replace with ('-' and '_')
         * The changes make the encoding alphabet file and URL safe.
         * @param arg string to encode
         * @return Base64Url encoding of the UTF8 bytes.
         */
        fun encode(arg: String): String {
            return encode(arg.commonAsUtf8ToByteArray())
        }

        /**
         * Converts a subset of an array of 8-bit unsigned integers to its equivalent string
         * representation that is encoded with base-64-url digits. Parameters specify the subset
         * as an offset in the input array, and the number of elements in the array to convert.
         * @param inArray An array of 8-bit unsigned integers.
         * @return The string representation in base 64 url encoding.
         */
        fun encode(inArray: ByteArray): String {
            var s = Base64.getEncoder().encodeToString(inArray)
            s = s.split(base64PadCharacter)[0]
            s = s.replace(base64Character62, base64UrlCharacter62)
            s = s.replace(base64Character63, base64UrlCharacter63)

            s = s.padEnd(s.length + (4 - s.length % 4) % 4, '=')

            return s
        }

        /**
         * Converts the specified string, which encodes binary data as base-64-url digits,
         * to an equivalent 8-bit unsigned integer array.
         * @param str base64Url encoded string.
         * @return UTF8 bytes.
         */
        fun decodeBytes(str: String): ByteArray {
            var newStr = str.replace(base64UrlCharacter62, base64Character62)
            newStr = newStr.replace(base64UrlCharacter63, base64Character63)

            when (newStr.length % 4) {
                2 -> newStr += doubleBase64PadCharacter
                3 -> newStr += base64PadCharacter
                1 -> throw IllegalArgumentException(str)
            }

            return Base64.getDecoder().decode(newStr)
        }

        /**
         * Decodes the string from Base64UrlEncoded to UTF8.
         * @param arg String to decode.
         * @return UTF8 string.
         */
        fun decode(arg: String): String{
            return String(decodeBytes(arg), Charsets.UTF_8)
        }
    }
}