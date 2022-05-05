package com.digital_enabling.android_aries_sdk.utils

import java.lang.RuntimeException
import java.util.*

class Base58Utils {

    companion object {
        /**
         * Converts a list of Ints into a ByteArray.
         */
        fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { pos -> ints[pos].toByte() }

        /**
         *
         */
        fun decodeBase58(value: String, flavor: Flavor = Flavor.BITCOIN): ByteArray {
            if (value.isEmpty()) {
                return ByteArray(0)
            }

            val indices = IntArray(128)
            Arrays.fill(indices, -1)
            for (i in flavor.alphabet.indices) {
                indices[flavor.alphabet[i].code] = i
            }

            val value58 = ByteArray(value.length)
            for (index in value.indices) {
                val char = value[index]
                val digit = if (char.code.toByte() < 128) {
                    indices[char.code]
                } else {
                    throw RuntimeException("Invalid Character {$char}")
                }
                value58[index] = digit.toByte()
            }

            var leadingZeros = 0
            while (leadingZeros < value58.size && value58[leadingZeros].toInt() == 0) {
                leadingZeros++
            }

            val decoded = ByteArray(value.length)
            var outputStart = decoded.size
            var inputStart = leadingZeros
            while (inputStart < value58.size) {
                val remaining = divmod(value58, inputStart, 58, 256)
                decoded[--outputStart] = remaining.toByte()
                if (value58[inputStart].toInt() == 0) {
                    inputStart++
                }
            }

            while (outputStart < decoded.size && decoded[outputStart].toInt() == 0) {
                outputStart++
            }

            return Arrays.copyOfRange(decoded, outputStart - leadingZeros, decoded.size)
        }

        /**
         *
         */
        fun encodeBase58(value: ByteArray, flavor: Flavor = Flavor.BITCOIN): String {
            if (value.isEmpty()) {
                return ""
            }

            val encodedZero = flavor.alphabet[0]
            val indices = IntArray(128)
            Arrays.fill(indices, -1)
            for (i in flavor.alphabet.indices) {
                indices[flavor.alphabet[i].code] = i
            }

            var leadingZeros = 0
            while (leadingZeros < value.size && value[leadingZeros].toInt() == 0) {
                ++leadingZeros
            }

            val encoded = CharArray(value.size * 2)
            var outputStart = encoded.size
            var inputStart = leadingZeros
            while (inputStart < value.size) {
                val remaining = divmod(value, inputStart, 256, 58)
                encoded[--outputStart] = flavor.alphabet[remaining]
                if (value[inputStart].toInt() == 0) {
                    ++inputStart
                }
            }

            while (outputStart < encoded.size && encoded[outputStart] == encodedZero) {
                ++outputStart
            }

            while (--leadingZeros >= 0) {
                encoded[--outputStart] = encodedZero
            }

            return String(encoded, outputStart, encoded.size - outputStart)
        }

        /**
         *
         */
        private fun divmod(number: ByteArray, first: Int, base: Int, divisor: Int): Int {
            var remaining = 0

            for (i in first until number.size) {
                val digit = number[i].toUByte() and 0xFF.toUByte()
                val temp = remaining * base + digit.toInt()
                number[i] = (temp / divisor).toByte()
                remaining = temp % divisor
            }

            return remaining
        }
    }

    enum class Flavor(val alphabet: CharArray){
        BITCOIN("123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray()),
        RIPPLE("rpshnaf39wBUDNEGHJKLM4PQRST7VWXYZ2bcdeCg65jkm8oFqi1tuvAxyz".toCharArray()),
        FLICKR("123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray())
    }
}