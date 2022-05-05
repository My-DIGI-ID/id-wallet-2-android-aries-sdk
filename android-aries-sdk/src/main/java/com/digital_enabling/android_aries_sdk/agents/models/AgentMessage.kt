package com.digital_enabling.android_aries_sdk.agents.models

import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.common.ErrorCode.INVALID_MESSAGE
import com.digital_enabling.android_aries_sdk.common.toByteArray
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.lang.Exception
import java.util.HashMap

/**
 * Represents an abstract base class of a content message.
 */
@Serializable
open class AgentMessage() {
    /**
     * If to use https messages.
     */
    protected var useMessageTypesHttps: Boolean = false

    /**
     * Internal representation of an agent message.
     */
    @kotlinx.serialization.Transient
    private var decorators: MutableList<JsonElement> = ArrayList()

    /**
     * The message id.
     */
    @SerialName("@id")
    var id: String? = null

    /**
     * The type.
     */
    @SerialName("@type")
    var type: String? = null

    constructor(useMessageTypes: Boolean) : this() {
        this.useMessageTypesHttps = useMessageTypes
    }

    fun getDecorators(): JsonArray {
        return JsonArray(decorators)
    }

    /**
     * Internal set method for setting the collection of decorators attached to the message.
     * @param decorators List of JsonElements.
     */
    fun setDecorators(decorators: List<JsonElement>) {
        this.decorators = decorators as MutableList<JsonElement>
    }

    /**
     * Internal set method for setting the collection of decorators attached to the message.
     * @param decorators JsonArray
     */
    fun setDecorators(decorators: JsonArray){
        this.decorators = decorators.toMutableList()
    }

    /**
     * Gets the decorator.
     *
     * @param name The name.
     * @return
     * @throws ErrorCode.InvalidMessage Cannot deserialize packed message or unable to find decorator on message.
     */
    inline fun <reified T> getDecorator(name: String): T {
        try {
            val decorator = getDecorators().first { deco -> (deco as JsonObject).containsKey("~$name") }
            val map = Json.decodeFromJsonElement<Map<String, T>>(decorator)
            return map.getValue("~$name")
        } catch (e: Exception) {
            throw AriesFrameworkException(
                INVALID_MESSAGE,
                "Failed to extract decorator from message",
                e
            )
        }
    }

    inline fun <reified T> findDecorator(name: String): T? {
        val decorator = getDecorators().firstOrNull { deco -> (deco as JsonObject).containsKey("~$name") }
        return try {
            if(decorator != null){
                Json.decodeFromJsonElement<Map<String, T>>(decorator).getValue("~$name")
            }
            else{
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Finds the decorator with the specified name or returns [null].
     *
     * @param decorator [T]
     * @param name The name.
     * @return The requested decorator or [null].
     * @throws AriesFrameworkException [ErrorCode.INVALID_PARAMETER_FORMAT]
     */
    inline fun <reified T> addDecorator(decorator: T, name: String) {
        var newJsonArray = JsonArray(getDecorators())
        val newMap = LinkedHashMap<String, JsonElement>()
        try {
            newMap["~$name"] = Json.encodeToJsonElement(decorator)
            newJsonArray = JsonArray(newJsonArray.plus(JsonObject(newMap)))
            setDecorators(newJsonArray)
        }
        catch (e:Exception) {
            throw AriesFrameworkException(
                ErrorCode.INVALID_PARAMETER_FORMAT,
                "Failed to add decorator",
                e
            )
        }

    }

    /**
     * Sets the decorator overriding any existing values.
     * @param decorator The decorator of type [T].
     * @param name The name.
     */
    inline fun <reified T> setDecorator(decorator: T, name: String){
        val oldDecorator = getDecorators().firstOrNull { deco -> (deco as JsonObject).containsKey("~$name") }
        if(oldDecorator != null){
            var newJsonArray = JsonArray(getDecorators())
            newJsonArray = JsonArray(newJsonArray.minus(oldDecorator))
            setDecorators(newJsonArray)
        }
        addDecorator(decorator, name)
    }

    open fun toJsonObject(): JsonObject {
        val json = Json.encodeToJsonElement(this) as JsonObject
        val newMap = HashMap<String, JsonElement>()
        json.forEach { (key, value) ->
            newMap[key] = value
        }
        this.getDecorators().forEach { decorator ->
            val map = Json.decodeFromJsonElement<Map<String, JsonElement>>(decorator)
            map.forEach{(key, value) ->
                newMap[key] = value
            }
        }
        return JsonObject(newMap)
    }

    open fun asByteArray(): ByteArray{
        return this.toJsonObject().toByteArray()
    }
}