package com.digital_enabling.android_aries_sdk.proof

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.credential.models.Credential
import com.digital_enabling.android_aries_sdk.credential.records.CredentialRecord
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.proof.messages.PresentationMessage
import com.digital_enabling.android_aries_sdk.proof.messages.ProposePresentationMessage
import com.digital_enabling.android_aries_sdk.proof.messages.RequestPresentationMessage
import com.digital_enabling.android_aries_sdk.proof.models.*
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery

/**
 * Proof Service.
 */
interface IProofService {
    /**
     * Creates a proof proposal.
     * @param agentContext Agent Context.
     * @param proofProposal The proof proposal that will be created
     * @param connectionId Connection identifier of who the proof request will be sent to.
     * @return Proof Request message and identifier.
     */
    suspend fun createProposal(
        agentContext: IAgentContext,
        proofProposal: ProofProposal?,
        connectionId: String?
    ): Pair<ProposePresentationMessage, ProofRecord>

    /**
     * Creates a proof request from a proof proposal.
     * @param agentContext Agent Context.
     * @param requestParameters The parameters requested to be proven
     * @param proofRecordId Proposal Id
     * @param connectionId Connection identifier of who the proof proposal will be sent to.
     * @return Proof Request message and identifier.
     */
    suspend fun createRequestFromProposal(
        agentContext: IAgentContext,
        requestParameters: ProofRequestParameters,
        proofRecordId: String?,
        connectionId: String?
    ): Pair<RequestPresentationMessage, ProofRecord>

    /**
     * Processes a proof proposal and stores it for a given connection.
     * @param agentContext Agent Context.
     * @param proposePresentationMessage A proof proposal.
     * @param connection Connection.
     * @return The identifier for the stored proof proposal.
     */
    suspend fun processProposal(
        agentContext: IAgentContext,
        proposePresentationMessage: ProposePresentationMessage,
        connection: ConnectionRecord
    ): RecordBase

    /**
     * Creates a proof request.
     * @param agentContext Agent Context.
     * @param proofRequest An enumeration of attribute we wish the prover to disclose.
     * @param connectionId Connection identifier of who the proof request will be sent to.
     * @return Proof Request message and identifier.
     */
    suspend fun createRequest(
        agentContext: IAgentContext,
        proofRequest: ProofRequest,
        connectionId: String?
    ): Pair<RequestPresentationMessage, ProofRecord>

    /**
     * Creates a new [RequestPresentationMessage] and associated [ProofRecord]
     * for use with connectionless transport.
     * @param agentContext Agent Context.
     * @param proofRequest An enumeration of attribute we wish the prover to disclose.
     * @return Proof Request message and identifier.
     */
    suspend fun createRequest(
        agentContext: IAgentContext,
        proofRequest: ProofRequest
    ): Pair<RequestPresentationMessage, ProofRecord>

    /**
     * Creates a proof request.
     * @param agentContext Agent Context.
     * @param proofRequestJson A string representation of proof request json object.
     * @param connectionId Connection identifier of who the proof request will be sent to.
     * @return Proof Request message and identifier.
     */
    suspend fun createRequest(
        agentContext: IAgentContext,
        proofRequestJson: String?,
        connectionId: String?
    ): Pair<RequestPresentationMessage, ProofRecord>

    /**
     * Processes a proof request and stores it for a given connection.
     * @param agentContext Agent Context.
     * @param proofRequest A proof request.
     * @param connection Connection.
     * @return The identifier for the stored proof request.
     */
    suspend fun processRequest(
        agentContext: IAgentContext,
        proofRequest: RequestPresentationMessage,
        connection: ConnectionRecord?
    ): ProofRecord

    /**
     * Processes a proof and stores it for a given connection.
     * @param agentContext Agent Context.
     * @param proof A proof.
     * @return The identifier for the stored proof.
     */
    suspend fun processPresentation(
        agentContext: IAgentContext,
        proof: PresentationMessage
    ): ProofRecord

    /**
     * Processes a proof request and generates an accompanying proof.
     * @param agentContext Agent Context.
     * @param proofRequest Proof Request.
     * @param requestedCredentials Requested Credentials.
     * @return The proof.
     */
    suspend fun createPresentation(
        agentContext: IAgentContext,
        proofRequest: ProofRequest,
        requestedCredentials: RequestedCredentials
    ): String

    /**
     * Creates a presentation message based on a request over connectionless transport.
     * @param agentContext Agent Context.
     * @param requestPresentation Requested Presentation.
     * @param requestedCredentials Requested Credentials.
     * @return The identifier for the stored proof.
     */
    suspend fun createPresentation(
        agentContext: IAgentContext,
        requestPresentation: RequestPresentationMessage,
        requestedCredentials: RequestedCredentials
    ): ProofRecord

    /**
     * Creates a proof.
     * @param agentContext Agent Context.
     * @param proofRecordId Identifier of the proof request.
     * @param requestedCredentials Requested Credentials.
     * @return The proof.
     */
    suspend fun createPresentation(
        agentContext: IAgentContext,
        proofRecordId: String,
        requestedCredentials: RequestedCredentials
    ): Pair<PresentationMessage, ProofRecord>

    /**
     * Rejects a proof request.
     * @param agentContext Agent Context.
     * @param proofRecordId Identifier of the proof request.
     * @return The proof.
     */
    suspend fun rejectProofRequest(
        agentContext: IAgentContext,
        proofRecordId: String
    )

    /**
     * Verifies a proof.
     * @param agentContext Agent Context.
     * @param proofRecordId Identifier of the proof record.
     * @return Status indicating validity of proof.
     */
    suspend fun verifyProof(
        agentContext: IAgentContext,
        proofRecordId: String
    ): Boolean

    /**
     * Verifies a proof.
     * @param agentContext Agent Context.
     * @param proofRequestJson The proof request.
     * @param proofJson The proof.
     * @param validateEncoding If true, validate the encoded raw values against standard encoding.
     * It is recommended to enable encoding validation to detect raw value tampering.
     * @return Status indicating the validity of proof.
     */
    suspend fun verifyProof(
        agentContext: IAgentContext,
        proofRequestJson: String,
        proofJson: String,
        validateEncoding: Boolean = true
    ): Boolean

    /**
     * Check if a credential has been revoked by validating against the revocation state
     * on the ledger.
     * @param agentContext Agent context.
     * @param record Credential record.
     * @return This method can be used as a holder, to check if a credential they own has been
     * revoked by the issuer. If a credential doesn't support revocation, this method will
     * always return false
     */
    suspend fun isRevoked(
        agentContext: IAgentContext,
        record: CredentialRecord
    ): Boolean

    /**
     * Check if a credential has been revoked by validating against the revocation state
     * on the ledger.
     * @param agentContext Agent context.
     * @param credentialRecordId Credential record identifier.
     * @return This method can be used as a holder, to check if a credential they own has been
     * revoked by the issuer. If a credential doesn't support revocation, this method will
     * always return false
     */
    suspend fun isRevoked(
        agentContext: IAgentContext,
        credentialRecordId: String
    ): Boolean

    /**
     * Gets an enumeration of proofs stored in the wallet.
     * @param agentContext Agent Context.
     * @param query The query.
     * @param count The count.
     * @return A list of proofs.
     */
    suspend fun list(
        agentContext: IAgentContext,
        query: ISearchQuery? = null,
        count: Int = 100
    ): List<ProofRecord>

    /**
     * Gets a particular proof stored in the wallet.
     * @param agentContext Agent Context.
     * @param proofRecordId Identifier of the proof record.
     * @return The proof.
     */
    suspend fun get(
        agentContext: IAgentContext,
        proofRecordId: String
    ): ProofRecord

    /**
     * Lists the credentials available for the given proof request.
     * @param agentContext Agent Context.
     * @param proofRequest The proof request object.
     * @param attributeReferent The attribute referent.
     * @return A collection of [Credential] that are available
     * for building a proof for the given proof request
     */
    suspend fun listCredentialsForProofRequest(
        agentContext: IAgentContext,
        proofRequest: ProofRequest,
        attributeReferent: String
    ): List<Credential>
}
