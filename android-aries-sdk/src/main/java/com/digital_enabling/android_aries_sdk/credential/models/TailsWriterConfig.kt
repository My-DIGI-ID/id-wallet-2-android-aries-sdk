package com.digital_enabling.android_aries_sdk.credential.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TailsWriterConfig (
    @SerialName("base_dir")
    var baseDir: String,
    @SerialName("uri_pattern")
    var uriPattern: String,
    @SerialName("file")
    var file: String? = null,
)
{
}