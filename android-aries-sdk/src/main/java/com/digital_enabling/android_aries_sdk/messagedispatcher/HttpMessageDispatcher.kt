package com.digital_enabling.android_aries_sdk.messagedispatcher

import com.digital_enabling.android_aries_sdk.agents.models.PackedMessageContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.net.URI
import java.util.concurrent.TimeUnit

/**
 * Http message dispatcher.
 */
class HttpMessageDispatcher : IMessageDispatcher {

    /**
     * @see IMessageDispatcher
     */
    override val transportSchemes = listOf("http", "https")

    /**
     * HTTP Client.
     */
    private val client = OkHttpClient.Builder()
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .writeTimeout(100, TimeUnit.SECONDS)
        .build()

    /**
     * @see IMessageDispatcher
     */
    override suspend fun dispatch(
        uri: URI,
        message: PackedMessageContext,
        additionalHeaders: List<Pair<String, String>>
    ): PackedMessageContext? {
        val headerBuilder: Headers.Builder = Headers.Builder()
        additionalHeaders.forEach { pair ->
            headerBuilder.add(pair.first, pair.second)
        }

        val request = Request.Builder()
            .url(uri.toString())
            .post(message.payload.toRequestBody())
            .headers(headerBuilder.build())
            .addHeader("Content-Type", DefaultMessageService.AgentWireMessageMimeType)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful)
                throw AriesFrameworkException(
                    ErrorCode.A2A_MESSAGE_TRANSMISSION_ERROR,
                    "Dispatch Failure. Request: ${request.asString()} Response: ${response.body?.string()}"
                )
            val responseBody = response.body

            if(responseBody != null){
                if (responseBody.contentType().toString() == DefaultMessageService.AgentWireMessageMimeType
                ) {
                    val rawContent = responseBody.bytes()

                    if (rawContent.isNotEmpty()) {
                        return PackedMessageContext(rawContent)
                    }
                }
            }
        }
        return null
    }

    fun Request.asString(): String{
        var requestString = "Url:${this.url}, Headers:["
        this.headers.forEach { header ->
            requestString += "(${header.first},${header.second})"
        }
        requestString += "], Body:${this.body.toString()}"
        return requestString
    }
}