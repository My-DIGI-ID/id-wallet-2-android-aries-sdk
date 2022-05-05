package com.digital_enabling.android_aries_sdk.koinTests

import com.digital_enabling.android_aries_sdk.agents.DefaultAgent
import com.digital_enabling.android_aries_sdk.agents.DefaultAgentProvider
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgent
import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentProvider
import com.digital_enabling.android_aries_sdk.basicmessage.DefaultBasicMessageHandler
import com.digital_enabling.android_aries_sdk.common.EventAggregator
import com.digital_enabling.android_aries_sdk.common.IEventAggregator
import com.digital_enabling.android_aries_sdk.configuration.AgentOptions
import com.digital_enabling.android_aries_sdk.configuration.DefaultProvisioningService
import com.digital_enabling.android_aries_sdk.configuration.IProvisioningService
import com.digital_enabling.android_aries_sdk.credential.DefaultCredentialHandler
import com.digital_enabling.android_aries_sdk.credential.DefaultCredentialService
import com.digital_enabling.android_aries_sdk.credential.DefaultSchemaService
import com.digital_enabling.android_aries_sdk.credential.DefaultTailsService
import com.digital_enabling.android_aries_sdk.credential.abstractions.ICredentialService
import com.digital_enabling.android_aries_sdk.credential.abstractions.ISchemaService
import com.digital_enabling.android_aries_sdk.credential.abstractions.ITailsService
import com.digital_enabling.android_aries_sdk.didexchange.DefaultConnectionHandler
import com.digital_enabling.android_aries_sdk.didexchange.DefaultConnectionService
import com.digital_enabling.android_aries_sdk.didexchange.IConnectionService
import com.digital_enabling.android_aries_sdk.discovery.DefaultDiscoveryHandler
import com.digital_enabling.android_aries_sdk.discovery.DefaultDiscoveryService
import com.digital_enabling.android_aries_sdk.discovery.IDiscoveryService
import com.digital_enabling.android_aries_sdk.koin.DiInitializier
import com.digital_enabling.android_aries_sdk.koin.KoinInteractionProvider
import com.digital_enabling.android_aries_sdk.ledger.DefaultLedgerService
import com.digital_enabling.android_aries_sdk.ledger.DefaultLedgerSigningService
import com.digital_enabling.android_aries_sdk.ledger.DefaultPoolService
import com.digital_enabling.android_aries_sdk.ledger.abstractions.ILedgerService
import com.digital_enabling.android_aries_sdk.ledger.abstractions.ILedgerSigningService
import com.digital_enabling.android_aries_sdk.ledger.abstractions.IPoolService
import com.digital_enabling.android_aries_sdk.messagedispatcher.DefaultMessageService
import com.digital_enabling.android_aries_sdk.messagedispatcher.HttpMessageDispatcher
import com.digital_enabling.android_aries_sdk.messagedispatcher.IMessageDispatcher
import com.digital_enabling.android_aries_sdk.messagedispatcher.IMessageService
import com.digital_enabling.android_aries_sdk.payments.DefaultPaymentService
import com.digital_enabling.android_aries_sdk.payments.IPaymentService
import com.digital_enabling.android_aries_sdk.proof.DefaultProofHandler
import com.digital_enabling.android_aries_sdk.proof.DefaultProofService
import com.digital_enabling.android_aries_sdk.proof.IProofService
import com.digital_enabling.android_aries_sdk.routing.DefaultForwardHandler
import com.digital_enabling.android_aries_sdk.trustping.DefaultTrustPingMessageHandler
import com.digital_enabling.android_aries_sdk.wallet.DefaultWalletRecordService
import com.digital_enabling.android_aries_sdk.wallet.DefaultWalletService
import com.digital_enabling.android_aries_sdk.wallet.IWalletRecordService
import com.digital_enabling.android_aries_sdk.wallet.IWalletService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.component.get

class KoinComponentTests {
    @Test
    @DisplayName("DI can resolve IEventAggregator.")
    fun dependencyInjection_IEventAggregator(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<IEventAggregator>()

        Assertions.assertTrue(testComponent is EventAggregator)
    }

    @Test
    @DisplayName("DI can resolve IConnectionService.")
    fun dependencyInjection_IConnectionService(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<IConnectionService>()

        Assertions.assertTrue(testComponent is DefaultConnectionService)
    }

    @Test
    @DisplayName("DI can resolve ICredentialService.")
    fun dependencyInjection_ICredentialService(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<ICredentialService>()

        Assertions.assertTrue(testComponent is DefaultCredentialService)
    }

    @Test
    @DisplayName("DI can resolve ILedgerService.")
    fun dependencyInjection_ILedgerService(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<ILedgerService>()

        Assertions.assertTrue(testComponent is DefaultLedgerService)
    }

    @Test
    @DisplayName("DI can resolve ILedgerSigningService.")
    fun dependencyInjection_ILedgerSigningService(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<ILedgerSigningService>()

        Assertions.assertTrue(testComponent is DefaultLedgerSigningService)
    }

    @Test
    @DisplayName("DI can resolve IPoolService.")
    fun dependencyInjection_IPoolService(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<IPoolService>()

        Assertions.assertTrue(testComponent is DefaultPoolService)
    }

    @Test
    @DisplayName("DI can resolve IProofService.")
    fun dependencyInjection_IProofService(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<IProofService>()

        Assertions.assertTrue(testComponent is DefaultProofService)
    }

    @Test
    @DisplayName("DI can resolve IDiscoveryService.")
    fun dependencyInjection_IDiscoveryService(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<IDiscoveryService>()

        Assertions.assertTrue(testComponent is DefaultDiscoveryService)
    }

    @Test
    @DisplayName("DI can resolve IProvisioningService.")
    fun dependencyInjection_IProvisioningService(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<IProvisioningService>()

        Assertions.assertTrue(testComponent is DefaultProvisioningService)
    }

    @Test
    @DisplayName("DI can resolve IMessageService.")
    fun dependencyInjection_IMessageService(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<IMessageService>()

        Assertions.assertTrue(testComponent is DefaultMessageService)
    }

    @Test
    @DisplayName("DI can resolve IMessageDispatcher.")
    fun dependencyInjection_IMessageDispatcher(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<IMessageDispatcher>()

        Assertions.assertTrue(testComponent is HttpMessageDispatcher)
    }

    @Test
    @DisplayName("DI can resolve ISchemaService.")
    fun dependencyInjection_ISchemaService(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<ISchemaService>()

        Assertions.assertTrue(testComponent is DefaultSchemaService)
    }

    @Test
    @DisplayName("DI can resolve ITailsService.")
    fun dependencyInjection_ITailsService(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<ITailsService>()

        Assertions.assertTrue(testComponent is DefaultTailsService)
    }

    @Test
    @DisplayName("DI can resolve IWalletRecordService.")
    fun dependencyInjection_IWalletRecordService(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<IWalletRecordService>()

        Assertions.assertTrue(testComponent is DefaultWalletRecordService)
    }

    @Test
    @DisplayName("DI can resolve IWalletService.")
    fun dependencyInjection_IWalletService(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<IWalletService>()

        Assertions.assertTrue(testComponent is DefaultWalletService)
    }

    @Test
    @DisplayName("DI can resolve IPaymentService.")
    fun dependencyInjection_IPaymentService(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<IPaymentService>()

        Assertions.assertTrue(testComponent is DefaultPaymentService)
    }

    @Test
    @DisplayName("DI can resolve IAgentProvider.")
    fun dependencyInjection_IAgentProvider(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<IAgentProvider>()

        Assertions.assertTrue(testComponent is DefaultAgentProvider)
    }

    @Test
    @DisplayName("DI can resolve IAgent.")
    fun dependencyInjection_IAgent(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<IAgent>()

        Assertions.assertTrue(testComponent is DefaultAgent)
    }

    @Test
    @DisplayName("DI can resolve DefaultConnectionHandler.")
    fun dependencyInjection_DefaultConnectionHandler(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<DefaultConnectionHandler>()

        Assertions.assertTrue(testComponent is DefaultConnectionHandler)
    }

    @Test
    @DisplayName("DI can resolve DefaultCredentialHandler.")
    fun dependencyInjection_DefaultCredentialHandler(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<DefaultCredentialHandler>()

        Assertions.assertTrue(testComponent is DefaultCredentialHandler)
    }

    @Test
    @DisplayName("DI can resolve DefaultTrustPingMessageHandler.")
    fun dependencyInjection_DefaultTrustPingMessageHandler(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<DefaultTrustPingMessageHandler>()

        Assertions.assertTrue(testComponent is DefaultTrustPingMessageHandler)
    }

    @Test
    @DisplayName("DI can resolve DefaultProofHandler.")
    fun dependencyInjection_DefaultProofHandler(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<DefaultProofHandler>()

        Assertions.assertTrue(testComponent is DefaultProofHandler)
    }

    @Test
    @DisplayName("DI can resolve DefaultForwardHandler.")
    fun dependencyInjection_DefaultForwardHandler(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<DefaultForwardHandler>()

        Assertions.assertTrue(testComponent is DefaultForwardHandler)
    }

    @Test
    @DisplayName("DI can resolve DefaultBasicMessageHandler.")
    fun dependencyInjection_DefaultBasicMessageHandler(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<DefaultBasicMessageHandler>()

        Assertions.assertTrue(testComponent is DefaultBasicMessageHandler)
    }

    @Test
    @DisplayName("DI can resolve DefaultDiscoveryHandler.")
    fun dependencyInjection_DefaultDiscoveryHandler(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<DefaultDiscoveryHandler>()

        Assertions.assertTrue(testComponent is DefaultDiscoveryHandler)
    }

    @Test
    @DisplayName("DI can resolve AgentOptions.")
    fun dependencyInjection_AgentOptions(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<AgentOptions>()

        Assertions.assertTrue(testComponent is AgentOptions)
    }

    @Test
    @DisplayName("DI can resolve List<IMessageDispatcher>.")
    fun dependencyInjection_ListIMessageDispatcher(){
        val koinProvider = KoinInteractionProvider()
        DiInitializier.setupDi()
        val testComponent = koinProvider.get<List<IMessageDispatcher>>()

        Assertions.assertTrue(testComponent is List<IMessageDispatcher>)
    }
}