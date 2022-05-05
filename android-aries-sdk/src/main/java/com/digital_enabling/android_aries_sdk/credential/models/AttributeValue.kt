package com.digital_enabling.android_aries_sdk.credential.models

import kotlinx.serialization.SerialName

class AttributeValue {
    @SerialName("raw")
    var raw: String? = null

    @SerialName("encoded")
    var encoded: String? = null
}