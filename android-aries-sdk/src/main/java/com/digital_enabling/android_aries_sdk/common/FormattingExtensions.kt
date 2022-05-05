package com.digital_enabling.android_aries_sdk.common

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

/**
 * Serializes an object to byte array using UTF8 byte order of the JSON graph.
 * @return The byte array.
 */
inline fun <reified T> T.toByteArray(): ByteArray{
    return if(this is String){
        throw Exception("Use kotlin .encodeToByteArray() function for string types")
    } else {
        Json.encodeToString(this).encodeToByteArray()
    }
}