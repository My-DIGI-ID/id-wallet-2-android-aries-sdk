package com.digital_enabling.android_aries_sdk.ledgerTests

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.ledger.DefaultLedgerService
import com.digital_enabling.android_aries_sdk.ledger.abstractions.ILedgerSigningService
import com.digital_enabling.android_aries_sdk.ledger.models.PoolAwaitable
import com.digital_enabling.android_aries_sdk.payments.PaymentAddressRecord
import com.digital_enabling.android_aries_sdk.payments.models.TransactionCost
import com.digital_enabling.android_aries_sdk.utils.IIndyWrapper
import kotlinx.coroutines.runBlocking
import org.hyperledger.indy.sdk.payments.PaymentsResults
import org.hyperledger.indy.sdk.pool.Pool
import org.hyperledger.indy.sdk.wallet.Wallet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultLedgerServiceTests {

    private val mockLedgerSigningService = Mockito.mock(ILedgerSigningService::class.java)
    private val mockAgentContext = Mockito.mock(IAgentContext::class.java)
    private val mockIndyWrapper = Mockito.mock(IIndyWrapper::class.java)
    private val mockPool = Mockito.mock(Pool::class.java)
    private val mockWallet = Mockito.mock(Wallet::class.java)

    @BeforeAll
    fun setup() : Unit = runBlocking {
        Mockito.`when`(mockIndyWrapper.buildCredDefRequest(any(), any())).thenReturn("mockValue")
        Mockito.`when`(
            mockIndyWrapper.addRequestFees(
                any(),
                eq(null),
                any(),
                any(),
                any(),
                eq(null)
            )
        )
            .thenReturn(
                PaymentsResults.AddRequestFeesResult("mockReqWithFeesJson", "mockPaymentMethod")
            )
        Mockito.`when`(mockIndyWrapper.submitRequest(any(), any())).thenReturn("{\"op\":reply}")
        Mockito.`when`(mockIndyWrapper.parseResponseWithFees(any(), any()))
            .thenReturn("[{\"recipient\":\"testRecipient\",\"amount\":0,\"receipt\":\"testReceipt\",\"extra\":\"testExtra\"}]")
        Mockito.`when`(mockIndyWrapper.buildRevocRegEntryRequest(any(), any(), any(), any()))
            .thenReturn("mockEntryRequest")

        Mockito.`when`(
            mockLedgerSigningService.signRequest(
                eq(mockAgentContext),
                any(),
                any()
            )
        ).thenReturn("mockSignedRequest")

        Mockito.`when`(mockAgentContext.wallet).thenReturn(mockWallet)
    }

    //region Tests for lookupAuthorizationRules()
    //endregion

    //region Tests for lookupAttribute()
    //endregion

    //region Tests for registerAttribute()
    //endregion

    //region Tests for lookupSchemaAttribute()
    //endregion

    //region Tests for lookupNym()
    //endregion

    //region Tests for lookupTransaction()
    //endregion

    //region Tests for lookupDefinition()
    //endregion

    //region Tests for lookupRevocationRegistryDefinition()
    //endregion

    //region Tests for lookupRevocationRegistryDelta()
    //endregion

    //region Tests for lookupRevocationRegistry()
    //endregion

    //region Tests for registerNym()
    //endregion

    //region Tests for registerCredentialDefinition()
    //endregion

    //region Tests for registerRevocationRegistryDefinition()
    //endregion

    //region Tests for sendRevocationRegistryEntry()
    @Test
    @DisplayName("")
    fun sendRevocationRegistryEntry_allArgumentsSet() = runBlocking {
        val testPoolAwaitable = PoolAwaitable { mockPool }
        Mockito.`when`(mockAgentContext.pool).thenReturn(testPoolAwaitable)

        val testPaymentAddressRecord = PaymentAddressRecord()
        testPaymentAddressRecord.address = "testPaymentAddress"
        val testPaymentInfo = TransactionCost(testPaymentAddressRecord, 0u, "testPaymentMethod")

        val testObject = DefaultLedgerService(mockLedgerSigningService, mockIndyWrapper)

        verify(mockIndyWrapper.buildRevocRegEntryRequest(any(), any(), any(), any())) {
            testObject.sendRevocationRegistryEntry(
                mockAgentContext,
                "testIssuerDid",
                "testRevocationRegistry",
                "testRevocationDefinition",
                "testValue",
                testPaymentInfo
            )
        }
    }
    //endregion

    //region Tests for registerSchema()
    @Test
    @DisplayName("RegisterSchema method works with all properties set.")
    fun registerSchema_allArgumentsSet() = runBlocking {
        val testPoolAwaitable = PoolAwaitable { mockPool }
        Mockito.`when`(mockAgentContext.pool).thenReturn(testPoolAwaitable)

        val testPaymentAddressRecord = PaymentAddressRecord()
        testPaymentAddressRecord.address = "testPaymentAddress"
        val testPaymentInfo = TransactionCost(testPaymentAddressRecord, 0u, "testPaymentMethod")
        val testObject = DefaultLedgerService(mockLedgerSigningService, mockIndyWrapper)

        verify(mockIndyWrapper.buildCredDefRequest(any(), any())) {
            testObject.registerSchema(
                mockAgentContext,
                "testIssuerDid",
                "testSchemaJson",
                testPaymentInfo
            )
        }
    }

    @Test
    @DisplayName("")
    fun registerSchema_emptyIssuerDid() {
        //TODO: What does the Indy function do? Wait for integration test
    }

    @Test
    @DisplayName("")
    fun registerSchema_emptySchemaJson() {
        //TODO: What does the Indy function do? Wait for integration test
    }

    @Test
    @DisplayName("RegisterSchema method works if paymentInfo is missing.")
    fun registerSchema_noPaymentInfo() = runBlocking {
        val testPoolAwaitable = PoolAwaitable { mockPool }
        Mockito.`when`(mockAgentContext.pool).thenReturn(testPoolAwaitable)

        val testPaymentAddressRecord = PaymentAddressRecord()
        testPaymentAddressRecord.address = "testPaymentAddress"

        val testObject = DefaultLedgerService(mockLedgerSigningService, mockIndyWrapper)

        verify(mockIndyWrapper.buildCredDefRequest(any(), any())) {
            testObject.registerSchema(
                mockAgentContext,
                "testIssuerDid",
                "testSchema"
            )
        }
    }
    //endregion
}