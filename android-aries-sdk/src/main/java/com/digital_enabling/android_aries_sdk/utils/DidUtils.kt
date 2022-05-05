package com.digital_enabling.android_aries_sdk.utils

/**
 * Did utilities
 */
object DidUtils {
    private const val FULL_VERKEY_REGEX = "^[1-9A-HJ-NP-Za-km-z]{43,44}$"
    private const val ABREVIATED_VERKEY_REGEX = "^~[1-9A-HJ-NP-Za-km-z]{22}$"
    private const val DID_REGEX = "^did:([a-z]+):([a-zA-z\\d]+)"
    private const val DID_KEY_REGEX = "^did:key:([1-9A-HJ-NP-Za-km-z]+)"
    private const val DIDKEY_PREFIX = "did:key"
    private const val BASE58_PREFIX = "z"
    private val MULTICODEC_PREFIX_ED25519 = Base58Utils.byteArrayOfInts(0xed, 0x01)

    /**
     * Sovrin DID method spec.
     */
    const val DID_SOV_METHOD_SPEC = "sov"

    /**
     * Constructs a DID from a method spec and identifier.
     * @param methodSpec DID method spec.
     * @param identifier Identifier to use in DID.
     * @return DID.
     */
    fun toDid(methodSpec: String, identifier: String) = "did:$methodSpec:$identifier"

    /**
     * Extracts the identifier from a DID.
     * @param did DID to extract the identifier from.
     * @return Identifier
     */
    fun identifierFromDid(did: String): String? {
        val regex = Regex(DID_REGEX)
        val matches = regex.findAll(did)

        return if (matches.count() != 1 || matches.first().groups.count() != 3) {
            null
        } else {
            matches.first().groups[2]?.value.toString()
        }
    }

    /**
     * Check a base58 encoded string against a regex expression to determine if it is a full valid verkey.
     * @param verkey Base58 encoded string representation of a verkey.
     * @return Boolean indicating if the string is a valid verkey.
     */
    fun isFullVerkey(verkey: String): Boolean {
        val regex = Regex(FULL_VERKEY_REGEX)
        return regex.matches(verkey)
    }

    /**
     * Check a base58 encoded string against a regex expression to determine if it is a abbreviated valid verkey.
     * @param verkey Base58 encoded string representation of a abbreviated verkey.
     * @return Boolean indicating if the string is a valid abbreviated verkey.
     */
    fun isAbbreviatedVerkey(verkey:String): Boolean{
        val regex = Regex(ABREVIATED_VERKEY_REGEX)
        return regex.matches(verkey)
    }

    /**
     * Check a base58 encoded string to determine if it is a valid verkey.
     * @param verkey Base58 encoded string representation of a verkey.
     * @return Boolean indicating if the string is a valid verkey.
     */
    fun isVerkey(verkey: String): Boolean{
        return isFullVerkey(verkey) || isAbbreviatedVerkey(verkey)
    }

    /**
     * Check if a given string is a valid did:key representation
     * @param didKey Given string to check for did:key
     * @return Boolean indicating if the string is a valid did:key
     */
    fun isDidKey(didKey: String): Boolean{
        val regex = Regex(DID_KEY_REGEX)
        return regex.matches(didKey)
    }

    /**
     * Converts a base58 encoded ed25519 verkey into its did:key representation.
     * @param verkey Base58 encoded string representation of a verkey.
     * @return The did:key representation of a verkey as string.
     */
    fun convertVerkeyToDidKey(verkey: String): String{
        if(!isFullVerkey(verkey)){
            throw IllegalArgumentException("Value $verkey is no verkey")
        }

        val base58PublicKey =  Base58Utils.encodeBase58(MULTICODEC_PREFIX_ED25519 +  Base58Utils.decodeBase58(verkey))

        return "$DIDKEY_PREFIX:$BASE58_PREFIX$base58PublicKey"
    }

    /**
     * Converts a did:key of a ed25519 public key into a plain base58 representation.
     * @param didKey A did:key representation of a ed25519 as string.
     * @return A plain base58 representation of that public key.
     */
    fun convertDidKeyToVerkey(didKey: String): String{
        if(!isDidKey(didKey)){
            throw IllegalArgumentException("Value {$didKey} is no did:key")
        }

        val base58EncodedKey = didKey.substring("$DIDKEY_PREFIX:$BASE58_PREFIX".length)
        var bytes =  Base58Utils.decodeBase58(base58EncodedKey)
        val codec = bytes.take(MULTICODEC_PREFIX_ED25519.size).toByteArray()

        if(codec.contentEquals(MULTICODEC_PREFIX_ED25519)){
            bytes = bytes.drop(MULTICODEC_PREFIX_ED25519.size).toByteArray()
            return  Base58Utils.encodeBase58(bytes)
        } else {
            throw IllegalArgumentException("Value $didKey has missing ED25519 multicodec prefix")
        }
    }
}