package com.digital_enabling.android_aries_sdk.decorators.signature

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.common.AriesFrameworkException
import com.digital_enabling.android_aries_sdk.common.ErrorCode
import com.digital_enabling.android_aries_sdk.utils.Base64UrlEncoder

import kotlinx.serialization.*
import kotlinx.serialization.json.*

import org.hyperledger.indy.sdk.crypto.Crypto
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.Base64

/**
 * Utility class for signing data for the usage in signature decorators.
 */
class SignatureUtils {
    companion object {
        /**
         * Default signature type for signing data.
         */
        const val DEFAULT_SIGNATURE_TYPE: String =
            "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/signature/1.0/ed25519Sha512_single"

        /**
         * Sign data supplied and return a signature decorator.
         * @param agentContext Agent context.
         * @param data Data to sign.
         * @param signerKey Signers verkey.
         * @return Signature decorator.
         */
        fun signData(
            agentContext: IAgentContext,
            data: Any,
            signerKey: String?
        ): SignatureDecorator {
            val dataJson = Json.encodeToString(data)
            val epochData = OffsetDateTime.now(ZoneOffset.UTC).toEpochSecond().toByte()
            // TODO: create proper Byte array
            val sigData = ByteArray(0)

            val sig = Crypto.cryptoSign(agentContext.wallet, signerKey, sigData).get()

            val sigDecorator = SignatureDecorator()
            sigDecorator.signatureType = DEFAULT_SIGNATURE_TYPE
            sigDecorator.signatureData = Base64.getEncoder().encodeToString(sigData)
            sigDecorator.signature = Base64.getEncoder().encodeToString(sig)
            sigDecorator.signer = signerKey

            return sigDecorator
        }

        /**
         * Unpack and verify signed data before casting it to the supplied type.
         * @param decorator Signature decorator to unpack and verify
         * @return Resulting data cast to type T.
         */
        inline fun <reified T> unpackAndVerify(decorator: SignatureDecorator): T {
            val dataBytes = decorator.signatureData?.getBytesFromBase64()
            val sigBytes = decorator.signature?.getBytesFromBase64()

            if(dataBytes == null || sigBytes == null){
                throw AriesFrameworkException(
                    ErrorCode.INVALID_MESSAGE,
                    "The signed payload was invalid"
                )
            }

            if (Crypto.cryptoVerify(
                    decorator.signer,
                    dataBytes,
                    sigBytes
                ).get()
            ) {
                val sigDataString = String(dataBytes.drop(8).toByteArray(), Charsets.UTF_8)
                return Json.decodeFromString(sigDataString)
            }

            throw AriesFrameworkException(
                ErrorCode.INVALID_MESSAGE,
                "The signed payload was invalid"
            )
        }

        fun String.getBytesFromBase64(): ByteArray{
            return Base64UrlEncoder.decodeBytes(this)
        }
    }
}