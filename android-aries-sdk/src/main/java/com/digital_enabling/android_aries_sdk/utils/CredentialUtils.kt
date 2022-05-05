package com.digital_enabling.android_aries_sdk.utils

import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.credential.models.CredentialMimeTypes
import com.digital_enabling.android_aries_sdk.credential.models.CredentialPreviewAttribute
import java.lang.Exception
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Credential utilities
 */
class CredentialUtils() {

    //Todo finish methods
    companion object {
        /**
         * Formats the credential values into a JSON usable with the [AnonCreds] API.
         * @param credentialAttributes The credential attributes.
         * @return The credential values.
         */
        fun formatCredentialValues(
            credentialAttributes: List<CredentialPreviewAttribute?>
        ): String {
            return ""
        }

        fun formatStringCredentialAttribute(
            attribute: CredentialPreviewAttribute
        ): HashMap<String, String> {
            return HashMap<String, String>()
        }

        /**
         * Checks if the value is encoded correctly.
         * @param raw
         * @param encoded
         */
        fun checkValidEncoding(raw: String?, encoded: String?): Boolean {
            var varRaw = raw
            if (varRaw.isNullOrEmpty()) {
                varRaw = ""
            }

            var varEncoded = encoded
            if (varEncoded.isNullOrEmpty()) {
                varEncoded = ""
            }

            try {
                varRaw.toInt()
                return compareOrdinal(varRaw, varEncoded) == 0
            } catch (e: Exception) {
                return compareOrdinal(varEncoded, getEncoded(varRaw)) == 0
            }
        }

        fun compareOrdinal(string1: String, string2: String): Int {
            if (string1.length == string2.length) {
                for (pos in string1.indices) {
                    if (string1[pos].code - string2[pos].code != 0) {
                        return string1[pos].code - string2[pos].code
                    }
                }
                return 0
            } else if (string1.length < string2.length) {
                val cut = string2.length - string1.length
                return (-1 * (string2[string2.length - cut]).code)
            } else {
                val cut = string1.length - string2.length
                return ((string1[string1.length - cut]).code)
            }
        }

        fun getEncoded(value: String?): String {
            var newValue = value
            if (newValue.isNullOrEmpty()) {
                newValue = ""
            }
            try {
                val result = newValue.toInt()
                return result.toString()
            } catch (e: Exception) {
                val data = mutableListOf<Byte>(0)
                val newValueBytes = newValue.sha256().toByteArray(Charsets.UTF_8)
                data.addAll(newValueBytes.toTypedArray())
                data.reverse()
                val result = data.toByteArray()
                return BigInteger(result).toString()
            }
        }

        fun String.sha256(): String {
            return hashString(this, "SHA-256")
        }

        private fun hashString(input: String, algorithm: String): String {
            return MessageDigest
                .getInstance(algorithm)
                .digest(input.toByteArray())
                .fold("", { str, it -> str + "%02x".format(it) })
        }

        /**
         * Validates if the credential preview attribute is valid.
         * @param attribute Credential preview attribute.
         */
        private fun validateCredentialPreviewAttribute(attribute: CredentialPreviewAttribute) {
            when (attribute.mimeType) {
                CredentialMimeTypes.TEXT_MIME_TYPE -> {
                    return
                }
                CredentialMimeTypes.APPLICATION_JSON_MIME_TYPE -> {
                    return
                }
                else -> {
                    throw AriesFrameworkException(
                        ErrorCode.INVALID_PARAMETER_FORMAT,
                        "'${attribute.name}' mime type of '${attribute.mimeType}' not supported"
                    )
                }
            }

        }

        /**
         * Validates if the credential preview attributes are valid.
         * @param attributes Credential preview attributes.
         */
        fun validateCredentialPreviewAttributes(
            attributes: Iterable<CredentialPreviewAttribute>
        ) {
            var validationErrors = listOf<String>()
            for ((index, attribute) in attributes.withIndex()) {
                try {
                    validateCredentialPreviewAttribute(attribute);
                } catch (e: AriesFrameworkException) {
                    validationErrors.plus(e.message)
                }
            }

            if (validationErrors.any())
                throw AriesFrameworkException(
                    ErrorCode.INVALID_PARAMETER_FORMAT,
                    validationErrors.toTypedArray()
                )

        }

        //fun castAttribute(attributeValue : object, mimeType : String ) : object {
        //}

        //fun castAttribute(attributeValue : JToken , mimeType : String ) : object {
        //}

        /**
         * Gets the attributes.
         * @param jsonAttributeValues The json attribute values.
         */
        fun getAttributes(jsonAttributeValues: String): HashMap<String, String> {
            return HashMap<String, String>()
        }
    }
}