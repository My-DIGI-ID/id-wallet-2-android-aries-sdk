package com.digital_enabling.android_aries_sdk.utils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperledger.indy.sdk.anoncreds.Anoncreds
import org.hyperledger.indy.sdk.anoncreds.AnoncredsResults
import org.hyperledger.indy.sdk.blob_storage.BlobStorageWriter
import org.hyperledger.indy.sdk.crypto.Crypto
import org.hyperledger.indy.sdk.did.Did
import org.hyperledger.indy.sdk.did.DidResults
import org.hyperledger.indy.sdk.ledger.Ledger
import org.hyperledger.indy.sdk.ledger.LedgerResults
import org.hyperledger.indy.sdk.payments.Payments
import org.hyperledger.indy.sdk.payments.PaymentsResults
import org.hyperledger.indy.sdk.pool.Pool
import org.hyperledger.indy.sdk.non_secrets.WalletRecord
import org.hyperledger.indy.sdk.non_secrets.WalletSearch
import org.hyperledger.indy.sdk.wallet.Wallet

class IndyWrapper : IIndyWrapper {

    //region Anoncreds
    override fun issuerCreateSchema(
        issuerDid: String?,
        name: String?,
        version: String?,
        attrs: String?
    ): AnoncredsResults.IssuerCreateSchemaResult {
        return Anoncreds.issuerCreateSchema(issuerDid, name, version, attrs).get()
    }

    override fun issuerCreateAndStoreCredentialDef(
        wallet: Wallet,
        issuerDid: String?,
        schemaJson: String?,
        tag: String?,
        signatureType: String?,
        configJson: String?
    ): AnoncredsResults.IssuerCreateAndStoreCredentialDefResult {
        return Anoncreds.issuerCreateAndStoreCredentialDef(
            wallet,
            issuerDid,
            schemaJson,
            tag,
            signatureType,
            configJson
        ).get()
    }

    override fun issuerCreateAndStoreRevocReg(
        wallet: Wallet,
        issuerDid: String?,
        revoc_dev_type: String?,
        tag: String?,
        credDefId: String?,
        configJson: String?,
        tailsWriter: BlobStorageWriter
    ): AnoncredsResults.IssuerCreateAndStoreRevocRegResult {
        return Anoncreds.issuerCreateAndStoreRevocReg(
            wallet,
            issuerDid,
            revoc_dev_type,
            tag,
            credDefId,
            configJson,
            tailsWriter
        ).get()
    }

    override fun proverCreateMasterSecret(wallet: Wallet, masterSecretId: String?): String {
        return Anoncreds.proverCreateMasterSecret(wallet, masterSecretId).get()
    }

    override fun proverGetCredential(wallet: Wallet, credId: String?): String {
        return Anoncreds.proverGetCredential(wallet, credId).get()
    }

    override fun proverCreateProof(
        wallet: Wallet,
        proofRequest: String?,
        requestedCredentials: String?,
        masterSecret: String?,
        schemas: String?,
        credentialDefs: String?,
        revStates: String?
    ): String {
        return Anoncreds.proverCreateProof(
            wallet,
            proofRequest,
            requestedCredentials,
            masterSecret,
            schemas,
            credentialDefs,
            revStates
        ).get()
    }

    override fun verifierVerifyProof(
        proofRequest: String?,
        proof: String?,
        schemas: String?,
        credentialDefs: String?,
        revocRegDefs: String?,
        revocRegs: String?
    ): Boolean {
        return Anoncreds.verifierVerifyProof(
            proofRequest,
            proof,
            schemas,
            credentialDefs,
            revocRegDefs,
            revocRegs
        ).get()
    }

    override fun generateNonce(): String {
        return Anoncreds.generateNonce().get()
    }

    override fun createRevocationState(
        blobStorageReaderHandle: Int,
        revRegDef: String?,
        revRegDelta: String?,
        timestamp: Long,
        credRevId: String?
    ): String {
        return Anoncreds.createRevocationState(
            blobStorageReaderHandle,
            revRegDef,
            revRegDelta,
            timestamp,
            credRevId
        ).get()
    }

    //endregion

    //region Crypto
    override fun cryptoUnpack(wallet: Wallet, message: ByteArray): ByteArray? {
        return Crypto.unpackMessage(wallet, message).get()
    }

    override fun cryptoPack(
        wallet: Wallet,
        recipientKeys: Array<String>,
        message: ByteArray,
        senderKey: String?
    ): ByteArray? {
        return Crypto.packMessage(
            wallet,
            Json.encodeToString(recipientKeys),
            senderKey,
            message
        ).get()
    }
    //endregion

    //region Ledger
    override fun buildGetAuthRuleRequest(
        submitterDid: String?,
        txnType: String?,
        action: String?,
        field: String?,
        oldValue: String?,
        newValue: String?
    ): String {
        return Ledger.buildGetAuthRuleRequest(
            submitterDid,
            txnType,
            action,
            field,
            oldValue,
            newValue
        ).get()
    }

    override fun submitRequest(pool: Pool, requestJson: String): String {
        return Ledger.submitRequest(pool, requestJson).get()
    }

    override fun buildGetAttribRequest(
        submitterDid: String?,
        targetDid: String?,
        raw: String?,
        hash: String?,
        enc: String?
    ): String {
        return Ledger.buildGetAttribRequest(submitterDid, targetDid, raw, hash, enc).get()
    }

    override fun buildAttribRequest(
        submitterDid: String?,
        targetDid: String?,
        hash: String?,
        raw: String?,
        enc: String?
    ): String {
        return Ledger.buildAttribRequest(submitterDid, targetDid, hash, raw, enc).get()
    }

    override fun buildGetSchemaRequest(submitterDid: String?, id: String?): String {
        return Ledger.buildGetSchemaRequest(submitterDid, id).get()
    }

    override fun parseGetSchemaResponse(getSchemaResponse: String): LedgerResults.ParseResponseResult {
        return Ledger.parseGetSchemaResponse(getSchemaResponse).get()
    }

    override fun buildGetNymRequest(submitterDid: String?, targetDid: String?): String {
        return Ledger.buildGetNymRequest(submitterDid, targetDid).get()
    }

    override fun buildGetTxnRequest(
        submitterDid: String?,
        ledgerType: String?,
        seqNo: Int
    ): String {
        return Ledger.buildGetTxnRequest(submitterDid, ledgerType, seqNo).get()
    }

    override fun buildGetCredDefRequest(submitterDid: String?, id: String?): String {
        return Ledger.buildGetCredDefRequest(submitterDid, id).get()
    }

    override fun parseGetCredDefResponse(getCredDefResponse: String): LedgerResults.ParseResponseResult {
        return Ledger.parseGetCredDefResponse(getCredDefResponse).get()
    }

    override fun buildGetRevocRegDefRequest(submitterDid: String?, id: String?): String {
        return Ledger.buildGetRevocRegDefRequest(submitterDid, id).get()
    }

    override fun parseGetRevocRegDefResponse(getRevocRegDefResponse: String): LedgerResults.ParseResponseResult {
        return Ledger.parseGetRevocRegDefResponse(getRevocRegDefResponse).get()
    }

    override fun buildGetRevocRegDeltaRequest(
        submitterDid: String?,
        revocRegDefId: String?,
        from: Long,
        to: Long
    ): String {
        return Ledger.buildGetRevocRegDeltaRequest(submitterDid, revocRegDefId, from, to).get()
    }

    override fun parseGetRevocRegDeltaResponse(getRevocRegDeltaResponse: String): LedgerResults.ParseRegistryResponseResult {
        return Ledger.parseGetRevocRegDeltaResponse(getRevocRegDeltaResponse).get()
    }

    override fun buildGetRevocRegRequest(
        submitterDid: String?,
        revocRegDefId: String?,
        timestamp: Long
    ): String {
        return Ledger.buildGetRevocRegRequest(submitterDid, revocRegDefId, timestamp).get()
    }

    override fun parseGetRevocRegResponse(getRevocRegResponse: String?): LedgerResults.ParseRegistryResponseResult {
        return Ledger.parseGetRevocRegResponse(getRevocRegResponse).get()
    }

    override fun buildNymRequest(
        submitterDid: String?,
        targetDid: String?,
        verkey: String?,
        alias: String?,
        role: String?
    ): String {
        return Ledger.buildNymRequest(submitterDid, targetDid, verkey, alias, role).get()
    }

    override fun buildCredDefRequest(submitterDid: String?, data: String?): String {
        return Ledger.buildCredDefRequest(submitterDid, data).get()
    }

    override fun buildRevocRegDefRequest(submitterDid: String?, data: String?): String {
        return Ledger.buildRevocRegDefRequest(submitterDid, data).get()
    }

    override fun buildRevocRegEntryRequest(
        submitterDid: String?,
        revocRegDefId: String?,
        revDefType: String?,
        value: String?
    ): String {
        return Ledger.buildRevocRegEntryRequest(submitterDid, revocRegDefId, revDefType, value)
            .get()
    }

    override fun appendTxnAuthorAgreementAcceptanceToRequest(
        requestJson: String?,
        text: String?,
        version: String?,
        taaDigest: String?,
        mechanism: String?,
        time: Long
    ): String {
        return Ledger.appendTxnAuthorAgreementAcceptanceToRequest(
            requestJson,
            text,
            version,
            taaDigest,
            mechanism,
            time
        ).get()
    }

    override fun signRequest(wallet: Wallet, submitterDid: String?, requestJson: String?): String {
        return Ledger.signRequest(wallet, submitterDid, requestJson).get()
    }
    //endregion

    //region Payments
    override fun addRequestFees(
        wallet: Wallet,
        submitterDid: String?,
        reqJson: String?,
        inputsJson: String?,
        outputsJson: String?,
        extra: String?
    ): PaymentsResults.AddRequestFeesResult {
        return Payments.addRequestFees(
            wallet,
            submitterDid,
            reqJson,
            inputsJson,
            outputsJson,
            extra
        ).get()
    }

    override fun parseResponseWithFees(paymentMethod: String?, respJson: String?): String {
        return Payments.parseResponseWithFees(paymentMethod, respJson).get()
    }
    //endregion

    //region Did
    override fun createAndStoreMyDid(
        wallet: Wallet,
        didJson: String?
    ): DidResults.CreateAndStoreMyDidResult {
        return Did.createAndStoreMyDid(wallet, didJson).get()
    }

    override fun abbreviateVerkey(did: String?, verkey: String?): String {
        return Did.AbbreviateVerkey(did, verkey).get()
    }
    //endregion

    //region Wallet
    override fun createWallet(
        config: String,
        credential: String
    ) {
        Wallet.createWallet(config, credential).get()
    }

    override fun openWallet(
        config: String,
        credential: String
    ): Wallet? {
        return Wallet.openWallet(config, credential).get()
    }

    override fun closeWallet(
        wallet: Wallet
    ) {
        wallet.closeWallet().get()
    }

    override fun deleteWallet(
        config: String,
        credential: String
    ) {
        Wallet.deleteWallet(config, credential).get()
    }

    override fun exportWallet(
        wallet: Wallet,
        exportConfigJson: String
    ) {
        Wallet.exportWallet(wallet, exportConfigJson).get()
    }

    override fun importWallet(
        config: String,
        credential: String,
        importConfigJson: String
    ) {
        Wallet.importWallet(config, credential, importConfigJson).get()
    }

    override fun generateWalletKey(
        config: String
    ): String {
        return Wallet.generateWalletKey(config).get()
    }
    //endregion Wallet

    //region WalletRecord
    override fun add(
        wallet: Wallet,
        type: String,
        id: String,
        value: String,
        tagsJson: String
    ) {
        WalletRecord.add(wallet, type, id, value, tagsJson).get()
    }

    override fun updateValue(
        wallet: Wallet,
        type: String,
        id: String,
        tagsJson: String
    ) {
        WalletRecord.updateValue(wallet, type, id, tagsJson).get()
    }

    override fun updateTags(
        wallet: Wallet,
        type: String,
        id: String,
        tagsJson: String
    ) {
        WalletRecord.updateTags(wallet, type, id, tagsJson).get()
    }

    override fun addTags(
        wallet: Wallet,
        type: String,
        id: String,
        tagsJson: String
    ) {
        WalletRecord.addTags(wallet, type, id, tagsJson).get()
    }

    override fun deleteTags(
        wallet: Wallet,
        type: String,
        id: String,
        tagNamesJson: String
    ) {
        WalletRecord.deleteTags(wallet, type, id, tagNamesJson).get()
    }

    override fun delete(
        wallet: Wallet,
        type: String,
        id: String
    ) {
        WalletRecord.delete(wallet, type, id).get()
    }

    override fun get(
        wallet: Wallet,
        type: String,
        id: String,
        optionsJson: String
    ): String {
        return WalletRecord.get(wallet, type, id, optionsJson).get()
    }
    //endregion WalletRecord

    //region WalletSearch

    override fun open(
        wallet: Wallet,
        type: String,
        queryJson: String,
        optionsJson: String
    ): WalletSearch {
        return WalletSearch.open(wallet, type, queryJson, optionsJson).get()
    }

    override fun searchFetchNextRecords(
        wallet: Wallet,
        walletSearch: WalletSearch,
        count: Int
    ): String {
        return WalletSearch.searchFetchNextRecords(wallet, walletSearch, count).get()
    }

    override fun closeSearch(
        walletSearch: WalletSearch
    ) {
        WalletSearch.closeSearch(walletSearch).get()
    }

    //endregion WalletSearch

}