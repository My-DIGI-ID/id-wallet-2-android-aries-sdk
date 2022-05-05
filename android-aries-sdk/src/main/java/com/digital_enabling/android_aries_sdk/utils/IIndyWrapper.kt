package com.digital_enabling.android_aries_sdk.utils

import org.hyperledger.indy.sdk.anoncreds.AnoncredsResults
import org.hyperledger.indy.sdk.blob_storage.BlobStorageWriter
import org.hyperledger.indy.sdk.did.DidResults
import org.hyperledger.indy.sdk.ledger.LedgerResults
import org.hyperledger.indy.sdk.non_secrets.WalletSearch
import org.hyperledger.indy.sdk.payments.PaymentsResults
import org.hyperledger.indy.sdk.pool.Pool
import org.hyperledger.indy.sdk.wallet.Wallet

interface IIndyWrapper {


    //region AnonCreds
    fun issuerCreateSchema(
        issuerDid: String?,
        name: String?,
        version: String?,
        attrs: String?
    ): AnoncredsResults.IssuerCreateSchemaResult

    fun issuerCreateAndStoreCredentialDef(
        wallet: Wallet,
        issuerDid: String?,
        schemaJson: String?,
        tag: String?,
        signatureType: String?,
        configJson: String?
    ): AnoncredsResults.IssuerCreateAndStoreCredentialDefResult

    fun issuerCreateAndStoreRevocReg(
        wallet: Wallet,
        issuerDid: String?,
        revoc_dev_type: String?,
        tag: String?,
        credDefId: String?,
        configJson: String?,
        tailsWriter: BlobStorageWriter
    ): AnoncredsResults.IssuerCreateAndStoreRevocRegResult

    fun proverCreateMasterSecret(wallet: Wallet, masterSecretId: String?): String

    fun proverGetCredential(wallet: Wallet, credId: String?): String

    fun proverCreateProof(
        wallet: Wallet,
        proofRequest: String?,
        requestedCredentials: String?,
        masterSecret: String?,
        schemas: String?,
        credentialDefs: String?,
        revStates: String?
    ): String

    fun verifierVerifyProof(
        proofRequest: String?,
        proof: String?,
        schemas: String?,
        credentialDefs: String?,
        revocRegDefs: String?,
        revocRegs: String?
    ): Boolean

    fun generateNonce(): String

    fun createRevocationState(
        blobStorageReaderHandle: Int,
        revRegDef: String?,
        revRegDelta: String?,
        timestamp: Long,
        credRevId: String?
    ): String
    //endregion

    //region Crypto
    fun cryptoUnpack(wallet: Wallet, message: ByteArray): ByteArray?

    fun cryptoPack(
        wallet: Wallet,
        recipientKeys: Array<String>,
        message: ByteArray,
        senderKey: String? = null
    ): ByteArray?
    //endregion

    //region Ledger
    fun buildGetAuthRuleRequest(
        submitterDid: String?,
        txnType: String?,
        action: String?,
        field: String?,
        oldValue: String?,
        newValue: String?
    ): String

    fun submitRequest(pool: Pool, requestJson: String): String

    fun buildGetAttribRequest(
        submitterDid: String?,
        targetDid: String?,
        raw: String?,
        hash: String?,
        enc: String?
    ): String


    fun buildAttribRequest(
        submitterDid: String?,
        targetDid: String?,
        hash: String?,
        raw: String?,
        enc: String?
    ): String


    fun buildGetSchemaRequest(submitterDid: String?, id: String?): String

    fun parseGetSchemaResponse(getSchemaResponse: String): LedgerResults.ParseResponseResult

    fun buildGetNymRequest(submitterDid: String?, targetDid: String?): String

    fun buildGetTxnRequest(submitterDid: String?, ledgerType: String?, seqNo: Int): String

    fun buildGetCredDefRequest(submitterDid: String?, id: String?): String

    fun parseGetCredDefResponse(getCredDefResponse: String): LedgerResults.ParseResponseResult

    fun buildGetRevocRegDefRequest(submitterDid: String?, id: String?): String

    fun parseGetRevocRegDefResponse(getRevocRegDefResponse: String): LedgerResults.ParseResponseResult

    fun buildGetRevocRegDeltaRequest(
        submitterDid: String?,
        revocRegDefId: String?,
        from: Long,
        to: Long
    ): String

    fun parseGetRevocRegDeltaResponse(getRevocRegDeltaResponse: String): LedgerResults.ParseRegistryResponseResult

    fun buildGetRevocRegRequest(
        submitterDid: String?,
        revocRegDefId: String?,
        timestamp: Long
    ): String

    fun parseGetRevocRegResponse(getRevocRegResponse: String?): LedgerResults.ParseRegistryResponseResult

    fun buildNymRequest(
        submitterDid: String?,
        targetDid: String?,
        verkey: String?,
        alias: String?,
        role: String?
    ): String

    fun buildCredDefRequest(submitterDid: String?, data: String?): String

    fun buildRevocRegDefRequest(submitterDid: String?, data: String?): String

    fun buildRevocRegEntryRequest(
        submitterDid: String?,
        revocRegDefId: String?,
        revDefType: String?,
        value: String?
    ): String

    fun appendTxnAuthorAgreementAcceptanceToRequest(
        requestJson: String?,
        text: String?,
        version: String?,
        taaDigest: String?,
        mechanism: String?,
        time: Long
    ): String

    fun signRequest(wallet: Wallet, submitterDid: String?, requestJson: String?): String
    //endregion

    //region Payments
    fun addRequestFees(
        wallet: Wallet,
        submitterDid: String?,
        reqJson: String?,
        inputsJson: String?,
        outputsJson: String?,
        extra: String?
    ): PaymentsResults.AddRequestFeesResult

    fun parseResponseWithFees(paymentMethod: String?, respJson: String?): String
    //endregion

    //region Did
    fun createAndStoreMyDid(wallet: Wallet, didJson: String?): DidResults.CreateAndStoreMyDidResult

    fun abbreviateVerkey(did: String?, verkey: String?): String
    //endregion

    //region Wallet

    fun createWallet(
        config: String,
        credential: String
    )

    fun openWallet(
        config: String,
        credential: String
    ): Wallet?

    fun closeWallet(
        wallet: Wallet
    )

    fun deleteWallet(
        config: String,
        credential: String
    )

    fun exportWallet(
        wallet: Wallet,
        exportConfigJson: String
    )

    fun importWallet(
        config: String,
        credential: String,
        importConfigJson: String
    )

    fun generateWalletKey(
        config: String
    ): String

    //endregion Wallet

    //region WalletRecord

    fun add(
        wallet: Wallet,
        type: String,
        id: String,
        value: String,
        tagsJson: String
    )

    fun updateValue(
        wallet: Wallet,
        type: String,
        id: String,
        tagsJson: String
    )

    fun updateTags(
        wallet: Wallet,
        type: String,
        id: String,
        tagsJson: String
    )

    fun addTags(
        wallet: Wallet,
        type: String,
        id: String,
        tagsJson: String
    )

    fun deleteTags(
        wallet: Wallet,
        type: String,
        id: String,
        tagNamesJson: String
    )

    fun delete(
        wallet: Wallet,
        type: String,
        id: String
    )

    fun get(
        wallet: Wallet,
        type: String,
        id: String,
        optionsJson: String
    ): String
    //endregion WalletRecord

    //region WalletSearch

    fun open(
        wallet: Wallet,
        type: String,
        queryJson: String,
        optionsJson: String
    ): WalletSearch

    fun searchFetchNextRecords(
        wallet: Wallet,
        walletSearch: WalletSearch,
        count: Int
    ): String

    fun closeSearch(
        walletSearch: WalletSearch
    )

    //endregion WalletSearch

}