package com.digital_enabling.android_aries_sdk.common

import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import java.lang.Exception

class AriesFrameworkException: Exception {

    /**
     * The error code
     */
    var errorCode: ErrorCode
        private set

    /**
     * The message context record.
     *
     * May be [null].
     */
    var contextRecord: RecordBase? = null
        private set

    /**
     * The context record ID.
     */
    var contextRecordId: String? = null
        private set

    /**
     * The connection record.
     *
     * May be [null].
     */
    var connectionRecord: ConnectionRecord? = null
        private set

    /**
     * Initializes a new instance of the [AriesFrameworkException] class.
     *
     *@param errorCode The error code.
     */
    constructor(errorCode: ErrorCode): super("Framework error occurred. Code: {$errorCode}"){
        this.errorCode = errorCode
    }

    /**
     * Initializes a new instance of the [AriesFrameworkException] class.
     * @param errorCode The error code.
     * @param message The message.
     */
    constructor(errorCode: ErrorCode, message: String): super(message){
        this.errorCode = errorCode
    }

    /**
     * Initializes a new instance of the [AriesFrameworkException] class.
     * @param errorCode The error code.
     * @param message The message.
     * @param innerException The inner exception.
     */
    constructor(errorCode: ErrorCode, message: String, innerException: Throwable): super(message, innerException){
        this.errorCode = errorCode
    }

    /**
    * Initializes a new instance of the [AriesFrameworkException] class.
    * @param errorCode The error code.
    * @param messages The message to concatenate together.
    */
    constructor(errorCode: ErrorCode, messages: Array<String>): super(messages.joinToString("\n")){
        this.errorCode = errorCode
    }

    /**
     * Initializes a new instance of the [AriesFrameworkException] class.
     * @param errorCode The error code.
     * @param message The message.
     * @param contextRecord
     * @param connectionRecord
     */
    constructor(errorCode: ErrorCode, message: String, contextRecord: RecordBase, connectionRecord: ConnectionRecord): super(message){
        this.errorCode = errorCode
        this.contextRecord = contextRecord
        this.connectionRecord = connectionRecord
    }

    /**
     * Initializes a new instance of the [AriesFrameworkException] class.
     * @param errorCode The error code.
     * @param message The message.
     * @param contextRecordId The record ID.
     */
    constructor(errorCode: ErrorCode, message: String, contextRecordId: String): super(message){
        this.errorCode = errorCode
        this.contextRecordId = contextRecordId
    }
}