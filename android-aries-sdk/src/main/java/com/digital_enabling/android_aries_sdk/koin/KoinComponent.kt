package com.digital_enabling.android_aries_sdk.koin

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
import org.koin.core.component.KoinComponent
import org.koin.dsl.koinApplication
import org.koin.dsl.module

internal object SharedModuleKoinHolder {
    var koinApplication = koinApplication {}
}

abstract class SharedKoinComponent : KoinComponent {
    override fun getKoin() = SharedModuleKoinHolder.koinApplication.koin
}

internal object DiInitializier {
    internal fun setupDi() {
        SharedModuleKoinHolder.koinApplication = koinApplication {
            modules(module {
                single<IEventAggregator> { EventAggregator() }
                single<IConnectionService> { DefaultConnectionService(get(), get(), get()) }
                single<ICredentialService> { DefaultCredentialService(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
                single<ILedgerService> { DefaultLedgerService(get()) }
                single<ILedgerSigningService> { DefaultLedgerSigningService(get()) }
                single<IPoolService> { DefaultPoolService() }
                single<IProofService> { DefaultProofService(get(), get(), get(), get(), get(), get(), get()) }
                single<IDiscoveryService> { DefaultDiscoveryService(get()) }
                single<IProvisioningService> { DefaultProvisioningService(get(), get(), get()) }
                single<IMessageService> { DefaultMessageService(get()) }
                single<IMessageDispatcher> { HttpMessageDispatcher() }
                single<ISchemaService> { DefaultSchemaService(get(), get(), get(), get(), get(), get()) }
                single<ITailsService> { DefaultTailsService(get(), get()) }
                single<IWalletRecordService> { DefaultWalletRecordService() }
                single<IWalletService> { DefaultWalletService() }
                single<IPaymentService> { DefaultPaymentService() }
                single<IAgentProvider> {
                    DefaultAgentProvider(
                        get(),
                        get(),
                        get(),
                        get()
                    )
                }
                single<IAgent> { DefaultAgent(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
                single { DefaultConnectionHandler(get(), get()) }
                single { DefaultCredentialHandler(get(), get(), get(), get()) }
                single { DefaultTrustPingMessageHandler(get(), get()) }
                single { DefaultProofHandler(get()) }
                single { DefaultForwardHandler(get()) }
                single { DefaultBasicMessageHandler(get()) }
                single { DefaultDiscoveryHandler(get()) }
                single { AgentOptions() }
                single<List<IMessageDispatcher>> { listOf(get()) }
            })
        }
    }
}

class KoinInteractionProvider : SharedKoinComponent() {
    //internal inline fun <reified Type : Interactor> create(): Type = get()
}
