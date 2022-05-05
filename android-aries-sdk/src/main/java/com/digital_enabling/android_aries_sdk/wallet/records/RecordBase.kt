package com.digital_enabling.android_aries_sdk.wallet.records

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.time.LocalDateTime
/**
 * Wallet record.
 */
@Serializable
abstract class RecordBase : IRecordBase {

    // TODO implement callerMemberName for all relevant methods
    /**
     * The identifier.
     */
    abstract override val id: String

    /**
     * The created datetime of the record.
     */
    @Contextual
    override var createdAtUtc: LocalDateTime?
        get() = getDateTime("createdAtUtc")
        set(value) {
            set(value.toString(), false, "createdAtUtc")
        }

    /**
     * The last updated datetime of the record.
     */
    @Contextual
    override var updatedAtUtc: LocalDateTime?
        get() = getDateTime("updatedAtUtc")
        set(value) {
            set(value.toString(), false, "updatedAtUtc")
        }

    /**
     * The type name.
     */
    @kotlinx.serialization.Transient
    abstract override var typeName: String

    /**
     * The tags.
     */
    override var tags = mutableMapOf<String, String>()

    /**
     * Gets the attribute.
     * @param name The name.
     */
    fun getTag(name: String): String? {
        return get(name)
    }

    /**
     * Sets the attribute.
     * @param name The name.
     * @param value The value.
     * @param encrypted Controls if the tag is encrypted.
     */
    fun setTag(name: String, value: String, encrypted: Boolean = true) {
        set(value, encrypted, name)
    }

    /**
     * Removes a user attribute.
     * @param name The name.
     */
    fun removeTag(name: String) {
        set(null, name = name)
    }

    /**
     * Sets the specified value, field and name.
     * @param value The value.
     * @param encrypted Controls whether the stored attribute should be encrypted at rest.
     * @param name The name.
     */
    internal fun set(value: String?, encrypted: Boolean = true, name: String = "") {
        if (name.isEmpty()) {
            throw IllegalArgumentException("Attribute name must be specified. CallerMemberName = $name")
        }
        var cmb = name
        if (!encrypted) {
            cmb = "~${name}"
        }
        value?.let {
            tags[cmb] = value
        } ?: kotlin.run {
            if (tags.containsKey(cmb)) {
                tags.remove(cmb)
            }
        }
    }

    /**
     * Get the value of the specified tag name.
     * @param callerMemberName The name.
     * @return The value.
     */
    private fun get(callerMemberName: String = ""): String? {
        if (tags.containsKey(callerMemberName)) return tags[callerMemberName]
        if (tags.containsKey("~$callerMemberName")) return tags["~$callerMemberName"]
        return null
    }

    /**
     * Gets the date time.
     * @param callerMemberName The name.
     * @return The date time.
     */
    protected fun getDateTime(callerMemberName: String): LocalDateTime? {
        get(callerMemberName)?.let {
            return LocalDateTime.parse(it)
        } ?: return null
    }

    /**
     * Gets the boolean.
     * @param callerMemberName The name.
     * @return The boolean
     */
    internal fun getBool(callerMemberName: String = ""): Boolean {
        get(callerMemberName)?.let {
            return it.toBoolean()
        } ?: return false
    }

    override fun toString(): String =
        "${this::class.simpleName}: " +
                "Id=${id}, " +
                "TypeName=${typeName}, " +
                "CreatedAtUtc=${createdAtUtc}, " +
                "UpdatedAtUtc=${updatedAtUtc}"

    abstract fun toJson(): String
}