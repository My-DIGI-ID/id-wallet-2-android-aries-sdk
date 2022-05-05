package com.digital_enabling.android_aries_sdk.didexchangeTests

import com.digital_enabling.android_aries_sdk.agents.DefaultAgentContext
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.agents.models.AgentEndpoint
import com.digital_enabling.android_aries_sdk.common.IEventAggregator
import com.digital_enabling.android_aries_sdk.configuration.IProvisioningService
import com.digital_enabling.android_aries_sdk.didexchange.DefaultConnectionService
import com.digital_enabling.android_aries_sdk.didexchange.IConnectionService
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionAlias
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionInvitationMessage
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.didexchange.models.InviteConfiguration
import com.digital_enabling.android_aries_sdk.koin.DiInitializier
import com.digital_enabling.android_aries_sdk.koin.KoinInteractionProvider
import com.digital_enabling.android_aries_sdk.utils.RecordType
import com.digital_enabling.android_aries_sdk.wallet.DefaultWalletRecordService
import com.digital_enabling.android_aries_sdk.wallet.IWalletRecordService
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperledger.indy.sdk.wallet.Wallet
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.koin.core.component.get
import org.mockito.Mockito.mock
import org.slf4j.LoggerFactory
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultConnectionServiceTests {

    private val mockEventAggregator = mock(IEventAggregator::class.java)
    private val mockWalletRecordService = mock(IWalletRecordService::class.java)
    private val mockProvisioningService = mock(IProvisioningService::class.java)
    private val mockLogger = LoggerFactory.getLogger(javaClass.simpleName)

    private val koinProvider = KoinInteractionProvider()
    private lateinit var walletRecordService: IWalletRecordService
    private lateinit var connectionService: IConnectionService
    private val config: String = "{\"id\":\"" + UUID.randomUUID() + "\"}";
    private val walletCredentials: String = "{\"key\":\"test_wallet_key\"}";
    private lateinit var agentContext: IAgentContext

    @BeforeAll
    fun setup(): Unit = runBlocking {
        Wallet.createWallet(config, walletCredentials).get()

        agentContext = DefaultAgentContext()
        agentContext.wallet = Wallet.openWallet(config, walletCredentials).get()

        DiInitializier.setupDi()
        walletRecordService = koinProvider.get()
        connectionService = koinProvider.get()
    }

    @AfterAll
    fun dispose(): Unit = runBlocking {
        agentContext.wallet!!.closeWallet()
        Wallet.deleteWallet(config, walletCredentials)
    }

    @Test
    @DisplayName("bla")
    fun testTags() = runBlocking {
        val testObject = ConnectionRecord("testId")
        testObject.myDid = "blabla"
        testObject.myVk = "bla"
        val endpoint = AgentEndpoint()
        endpoint.verkey = arrayOf("1")
        endpoint.did = "endpointBla"
        endpoint.uri = "uribla"
        testObject.endpoint = endpoint
        val alias = ConnectionAlias()
        alias.imageUrl = "imageurl"
        alias.name = "name"
        testObject.alias = alias
        testObject.setTag("testTag", "aösfjdl")
        val testWallet = agentContext.wallet ?: throw Exception()

        testAdd(testWallet, testObject)

        val x = walletRecordService.get<ConnectionRecord>(
            testWallet,
            "testId",
            RecordType.CONNECTION_RECORD
        )

        testObject.setTag("testTag2", "awier")
        walletRecordService.update(testWallet, testObject)

        val y = walletRecordService.get<ConnectionRecord>(
            testWallet,
            "testId",
            RecordType.CONNECTION_RECORD
        )
    }

    suspend fun testAdd(testWallet: Wallet, connectionRecord: ConnectionRecord) {
        deeperAdd(testWallet, connectionRecord)
    }

    suspend fun deeperAdd(wallet: Wallet, connectionRecord: ConnectionRecord) {
        walletRecordService.add(wallet, connectionRecord)
    }

    //region Tests for createInvitation

    //endregion

    //region Tests for revokeInvitation
    //endregion

    //region Tests for createRequest
    //endregion

    //region Tests for processRequest
    //endregion

    //region Tests for processResponse
    //endregion

    //region Tests for createResponse
    //endregion

    //region Tests for get
    //endregion

    //region Tests for list
    //endregion

    //region Tests for delete
    //endregion

}