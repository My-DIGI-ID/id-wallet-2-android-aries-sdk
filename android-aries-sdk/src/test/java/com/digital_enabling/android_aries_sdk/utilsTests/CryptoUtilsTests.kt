package com.digital_enabling.android_aries_sdk.utilsTests

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.AgentMessage
import com.digital_enabling.android_aries_sdk.common.toByteArray
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionInvitationMessage
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRequestMessage
import com.digital_enabling.android_aries_sdk.koin.DiInitializier
import com.digital_enabling.android_aries_sdk.routing.ForwardMessage
import com.digital_enabling.android_aries_sdk.utils.CryptoUtils
import com.digital_enabling.android_aries_sdk.utils.DidUtils
import com.digital_enabling.android_aries_sdk.utils.IndyWrapper
import com.digital_enabling.android_aries_sdk.utils.UnpackResult
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.hyperledger.indy.sdk.did.Did
import org.hyperledger.indy.sdk.wallet.Wallet
import org.junit.jupiter.api.*
import org.mockito.Mockito.mock
import java.lang.Exception
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CryptoUtilsTests {
    private val mockAgentContext = mock(IAgentContext::class.java)
    private val mockAgentMessage = mock(AgentMessage::class.java)
    private val mockWallet = mock(Wallet::class.java)

    private val config: String = "{\"id\":\"" + UUID.randomUUID() + "\"}";
    private val walletCredentials: String = "{\"key\":\"test_wallet_key\"}";
    private lateinit var wallet: Wallet

    @BeforeAll
    fun setup(): Unit = runBlocking {
        Wallet.createWallet(config, walletCredentials).get()
        wallet = Wallet.openWallet(config, walletCredentials).get()
    }

    @AfterAll
    fun dispose(): Unit = runBlocking {
        try {
            wallet.closeWallet()
            Wallet.deleteWallet(config, walletCredentials)
        } catch (e: Exception) {
        }
    }

    @Test
    @DisplayName("Test1.")
    fun pack_normalCall(): Unit = runBlocking {
        //Assign
        val message = ConnectionInvitationMessage()
        message.recipientKeys = listOf("123")
        val messageBytes = message.toByteArray()
        var my = Did.createAndStoreMyDid(wallet, "{}").get()
        var anotherMy = Did.createAndStoreMyDid(wallet, "{}").get()
        var packed = CryptoUtils().pack(wallet, anotherMy.verkey, message, null)
        val indyPack =
            IndyWrapper().cryptoPack(wallet, arrayOf(anotherMy.verkey), message.asByteArray(), null)


        //Act
        val result = IndyWrapper().cryptoUnpack(wallet, indyPack!!)
        if (result != null) {
            val resultAsString = String(result, Charsets.UTF_8)
            val y = Json.decodeFromString<UnpackResult>(resultAsString)
        }

        //Assert
        Assertions.assertDoesNotThrow { CryptoUtils().unpack<UnpackResult>(wallet, packed!!) }
    }

    @Test
    @DisplayName("getUniqueKey works properly and returns a random key which is alphanumeric and has the target length.")
    fun getUniqueKey_checkLengthAlphanumeric(): Unit = runBlocking {
        //Assign
        val testObject = CryptoUtils()
        val testLength = 100

        //Act
        val actual = testObject.getUniqueKey(testLength)
        val onlyAlphanumericChars = "[A-Za-z0-9]+".toRegex().matches(actual)

        //Assert
        Assertions.assertEquals(actual.length, testLength)
        Assertions.assertTrue(onlyAlphanumericChars)
    }
}