package com.digital_enabling.android_aries_sdk.decoratorsTests.signature

import com.digital_enabling.android_aries_sdk.decorators.signature.SignatureDecorator
import com.digital_enabling.android_aries_sdk.decorators.signature.SignatureUtils
import com.digital_enabling.android_aries_sdk.didexchange.models.Connection
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SignatureUtilsTests {
    @Test
    @DisplayName("unpackAndVerify works correctly")
    fun unpackAndVerify_works() {
        //Arrange
        val testObject = Json.decodeFromString<SignatureDecorator>(
            "{\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/signature/1.0/ed25519Sha512_single\",\"sig_data\":\"848gYgAAAAB7IkRJRCI6Ikh6bTNyV0dMSHlXNnFmZjM5Zk1nNWsiLCJESUREb2MiOnsiQGNvbnRleHQiOiJodHRwczovL3czaWQub3JnL2RpZC92MSIsImlkIjoiSHptM3JXR0xIeVc2cWZmMzlmTWc1ayIsInB1YmxpY0tleSI6W3siaWQiOiJIem0zcldHTEh5VzZxZmYzOWZNZzVrI2tleXMtMSIsInR5cGUiOiJFZDI1NTE5VmVyaWZpY2F0aW9uS2V5MjAxOCIsImNvbnRyb2xsZXIiOiJIem0zcldHTEh5VzZxZmYzOWZNZzVrIiwicHVibGljS2V5QmFzZTU4IjoiQUdHb1FkRjhQRUo1TmNlTWkyeHRIclExcVljdVVDdlpRNWJ4UkhvdUJ0eDQifV0sInNlcnZpY2UiOlt7ImlkIjoiSHptM3JXR0xIeVc2cWZmMzlmTWc1aztpbmR5IiwidHlwZSI6IkluZHlBZ2VudCIsInJlY2lwaWVudEtleXMiOlsiQUdHb1FkRjhQRUo1TmNlTWkyeHRIclExcVljdVVDdlpRNWJ4UkhvdUJ0eDQiXSwicm91dGluZ0tleXMiOlsiQ2k5NVJOV0djNE5TVmJtZ1lqR1ZURWFNUXpUcThFa2VpN1FEYVp1UW5wY0UiXSwic2VydmljZUVuZHBvaW50IjoiaHR0cHM6Ly8yMTM1LTEzMC0xODAtODYtMjE0Lm5ncm9rLmlvIn1dfX0=\",\"signer\":\"23VFagWi9asQfYXjwLYtwRT1ZskYh3QXE91ZuAXsFq3n\",\"signature\":\"NDKqWfJonB6IZgTQDz1W8bOz5keqT9nEjWB5jafI1a6UIHMBDcFx1sUYFTgB5ibf_JLlc1mgeLois4M1psJhCw==\"}"
        )

        //Act
        val actual = SignatureUtils.unpackAndVerify<Connection>(testObject)

        //Assert
        Assertions.assertTrue(actual is Connection)
    }
}