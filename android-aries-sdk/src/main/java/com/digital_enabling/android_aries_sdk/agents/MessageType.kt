package com.digital_enabling.android_aries_sdk.agents

import com.digital_enabling.android_aries_sdk.utils.MessageUtils

/**
 * A representation of a message type.
 */
class MessageType{

    /**
     * Base uri the message type derives from.
     */
    var baseUri: String? = ""

    /**
     * Message family the message belongs to.
     */
    var messageFamilyName: String? = ""

    /**
     * Message version the message belongs to.
     */
    var messageVersion: String? = ""

    /**
     * Message family uri the message belongs to.
     */
    var messageFamilyUri: String? = "{${baseUri}}/{${messageFamilyName}}/{${messageVersion}}"

    /**
     * Message name of the message.
     */
    var messageName: String? = null

    /**
     * Full Uri of the message type.
     */
    var messageTypeUri: String? = null


    /**
     * Constructor for creating the representation from a message type uri.
     * @param messageType Message type.
     */
    constructor(messageType: String){
        val decodedMessageType = MessageUtils.decodeMessageTypeUri(messageType)

        baseUri = decodedMessageType.uri
        messageFamilyName = decodedMessageType.messageFamilyName
        messageVersion = decodedMessageType.messageVersion
        messageName = decodedMessageType.messageName
        messageTypeUri = messageType
    }
}