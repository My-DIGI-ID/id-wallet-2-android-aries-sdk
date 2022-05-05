package com.digital_enabling.android_aries_sdk.proof

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.common.*
import com.digital_enabling.android_aries_sdk.configuration.IProvisioningService
import com.digital_enabling.android_aries_sdk.credential.abstractions.ITailsService
import com.digital_enabling.android_aries_sdk.credential.models.Credential
import com.digital_enabling.android_aries_sdk.credential.models.CredentialInfo
import com.digital_enabling.android_aries_sdk.credential.models.CredentialMimeTypes
import com.digital_enabling.android_aries_sdk.credential.records.CredentialRecord
import com.digital_enabling.android_aries_sdk.credential.records.CredentialState
import com.digital_enabling.android_aries_sdk.credential.records.CredentialTrigger
import com.digital_enabling.android_aries_sdk.decorators.DecoratorNames
import com.digital_enabling.android_aries_sdk.decorators.ServiceDecorator
import com.digital_enabling.android_aries_sdk.decorators.attachments.Attachment
import com.digital_enabling.android_aries_sdk.decorators.attachments.AttachmentContent
import com.digital_enabling.android_aries_sdk.decorators.threading.getThreadId
import com.digital_enabling.android_aries_sdk.decorators.threading.threadFrom
import com.digital_enabling.android_aries_sdk.didexchange.IConnectionService
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionRecord
import com.digital_enabling.android_aries_sdk.didexchange.models.ConnectionState
import com.digital_enabling.android_aries_sdk.ledger.abstractions.ILedgerService
import com.digital_enabling.android_aries_sdk.messagedispatcher.IMessageService
import com.digital_enabling.android_aries_sdk.proof.messages.*
import com.digital_enabling.android_aries_sdk.proof.models.*
import com.digital_enabling.android_aries_sdk.utils.*
import com.digital_enabling.android_aries_sdk.wallet.IWalletRecordService
import com.digital_enabling.android_aries_sdk.wallet.records.RecordBase
import com.digital_enabling.android_aries_sdk.wallet.records.search.SearchQuery
import com.digital_enabling.android_aries_sdk.wallet.records.search.searchExpression.subqueries.ISearchQuery
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperledger.indy.sdk.anoncreds.CredentialsSearchForProofReq
import java.util.*
import kotlin.collections.HashMap

/**
 * Proof service. Initializes a new instance of the [DefaultProofService] class
 * @param eventAggregator The event aggregator
 * @param connectionService The connection service
 * @param recordService The record service
 * @param provisioningService The provisioning service
 * @param ledgerService The ledger service
 * @param tailsService The tails service
 * @param messageService The message service
 *
 * @see IProofService
 */
open class DefaultProofService(
    protected val eventAggregator: IEventAggregator,
    protected val connectionService: IConnectionService,
    protected val recordService: IWalletRecordService,
    protected val provisioningService: IProvisioningService,
    protected val ledgerService: ILedgerService,
    protected val tailsService: ITailsService,
    protected val messageService: IMessageService,
    private val indyWrapper: IIndyWrapper = IndyWrapper()
) : IProofService {
    /**
     * @see IProofService
     */
    override suspend fun createProposal(
        agentContext: IAgentContext,
        proofProposal: ProofProposal?,
        connectionId: String?
    ): Pair<ProposePresentationMessage, ProofRecord> {
        if (proofProposal == null) {
            throw Exception("You must provide a presentation preview")
        }
        if (connectionId != null) {
            val connection = connectionService.get(agentContext, connectionId)

            if (connection.state != ConnectionState.CONNECTED) {
                throw AriesFrameworkException(
                    ErrorCode.RECORD_IN_INVALID_STATE,
                    "Connection state was invalid. Expected CONNECTED, found ${connection.state}"
                )
            }
        }
        checkProofProposalParameters(proofProposal)

        val wallet = agentContext.wallet ?: throw IllegalArgumentException("Wallet is null")

        val threadId = UUID.randomUUID().toString()
        val proofRecord = ProofRecord(UUID.randomUUID().toString(), true)
        proofRecord.connectionId = connectionId
        proofRecord.proposalJson = Json.encodeToString(proofProposal)

        proofRecord.setTag(TagConstants.ROLE, TagConstants.HOLDER)
        proofRecord.setTag(TagConstants.LAST_THREAD_ID, threadId)

        recordService.add(wallet, proofRecord)

        val message = ProposePresentationMessage(agentContext.useMessageTypesHttps)
        message.id = threadId
        message.comment = proofProposal.comment
        val presentationPreviewMessage =
            PresentationPreviewMessage(agentContext.useMessageTypesHttps)
        presentationPreviewMessage.proposedAttributes =
            proofProposal.proposedAttributes?.toTypedArray()
        presentationPreviewMessage.proposedPredicates =
            proofProposal.proposedPredicates?.toTypedArray()
        message.presentationPreviewMessage = presentationPreviewMessage

        message.threadFrom(threadId)
        return (Pair(message, proofRecord))
    }

    /**
     * @see IProofService
     */
    override suspend fun createRequestFromProposal(
        agentContext: IAgentContext,
        requestParameters: ProofRequestParameters,
        proofRecordId: String?,
        connectionId: String?
    ): Pair<RequestPresentationMessage, ProofRecord> {
        if (proofRecordId == null) {
            throw Exception("You must provide proof record Id")
        }

        if (connectionId != null) {
            val connection = connectionService.get(agentContext, connectionId)

            if (connection.state != ConnectionState.CONNECTED) {
                throw AriesFrameworkException(
                    ErrorCode.RECORD_IN_INVALID_STATE,
                    "Connection state was invalid. Expected CONNECTED, found ${connection.state}"
                )
            }
        }

        val wallet = agentContext.wallet ?: throw IllegalArgumentException("Wallet is null")

        val proofRecord = recordService.get<ProofRecord>(
            wallet,
            proofRecordId,
            RecordType.PROOF_RECORD
        ) as ProofRecord
        val proofProposal =
            proofRecord.proposalJson?.let { Json.decodeFromString<ProofProposal>(it) }
                ?: throw AriesFrameworkException(
                    ErrorCode.INVALID_RECORD_DATA,
                    "PropoposalJson was invalid"
                )

        val proofRequest =
            ProofRequest(
                name = requestParameters.name,
                version = requestParameters.version,
                nonce = indyWrapper.generateNonce(),
                nonRevoked = requestParameters.nonRevoked
            )

        val attributesByReferent = HashMap<String, MutableList<ProposedAttribute>>()
        proofProposal.proposedAttributes?.forEach { it ->
            if (it.referent == null) {
                it.referent = UUID.randomUUID().toString()
            }

            if (attributesByReferent.containsKey(it.referent)) {
                attributesByReferent[it.referent]?.add(it)
            } else {
                attributesByReferent[it.referent!!] = mutableListOf(it)
            }
        }

        attributesByReferent.forEach { (key, value) ->
            val requestedAttribute = ProofAttributeInfo()
            if (value.count() == 1) {
                requestedAttribute.name = value.single().name
            } else if (value.count() > 1) {
                val nameList = mutableListOf<String>()
                value.forEach {
                    if (it.name != null) {
                        nameList.add(it.name!!)
                    }
                }
                requestedAttribute.names = nameList.toTypedArray()
            }

            val attributeFilter = AttributeFilter(
                credentialDefintionId = value.first().credentialDefintionId,
                schemaId = value.first().schemaId,
                issuerDid = value.first().issuerDid
            )

            requestedAttribute.restrictions = mutableListOf(attributeFilter)

            proofRequest.requestedAttributes!![key] = requestedAttribute
        }

        proofProposal.proposedPredicates?.forEach { it ->
            if (it.referent == null) {
                it.referent = UUID.randomUUID().toString()
            }

            val attributeFilter = AttributeFilter(
                credentialDefintionId = it.credentialDefintionId,
                schemaId = it.schemaId,
                issuerDid = it.issuerDid
            )

            val predicate = ProofPredicateInfo(
                predicateType = it.predicate,
                predicateValue = it.threshold?.toInt()
            )
            predicate.name = it.name
            predicate.restrictions = mutableListOf(attributeFilter)

            proofRequest.requestedPredicates!![it.referent!!] = predicate
        }

        proofRecord.requestJson = Json.encodeToString(proofRequest)
        proofRecord.trigger(ProofTrigger.REQUEST)
        recordService.update(wallet, proofRecord)

        val message = RequestPresentationMessage()
        message.id = proofRecord.id
        val attachment = Attachment("libindy-request-presentation-0")
        attachment.mimeType = CredentialMimeTypes.APPLICATION_JSON_MIME_TYPE
        val content = AttachmentContent()
        content.base64 = Base64.getEncoder()
            .encodeToString(Json.encodeToString(proofRequest).toByteArray(Charsets.UTF_8))
        attachment.data = content
        message.requests = arrayOf(attachment)

        proofRecord.getTag(TagConstants.LAST_THREAD_ID)?.let { message.threadFrom(it) }

        return Pair(message, proofRecord)
    }

    /**
     * @see IProofService
     */
    override suspend fun processProposal(
        agentContext: IAgentContext,
        proposePresentationMessage: ProposePresentationMessage,
        connection: ConnectionRecord
    ): RecordBase {
        val wallet = agentContext.wallet ?: throw IllegalArgumentException("Wallet is null")

        val proofProposal = ProofProposal(
            comment = proposePresentationMessage.comment,
            proposedAttributes =
            proposePresentationMessage.presentationPreviewMessage?.proposedAttributes?.toMutableList(),
            proposedPredicates =
            proposePresentationMessage.presentationPreviewMessage?.proposedPredicates?.toMutableList()
        )

        val proofRecord = ProofRecord(UUID.randomUUID().toString(), true)
        proofRecord.proposalJson = Json.encodeToString(proofProposal)
        proofRecord.connectionId = connection.id

        val threadId = proposePresentationMessage.getThreadId()
            ?: throw IllegalArgumentException("Presentation thread id not found")

        proofRecord.setTag(TagConstants.LAST_THREAD_ID, threadId)
        proofRecord.setTag(TagConstants.ROLE, TagConstants.REQUESTOR)
        recordService.add(wallet, proofRecord)

        eventAggregator.publish(
            ServiceMessageProcessingEvent(
                proofRecord.id,
                proposePresentationMessage.type ?: "",
                threadId
            )
        )

        return proofRecord
    }

    /**
     * @see IProofService
     */
    override suspend fun createRequest(
        agentContext: IAgentContext,
        proofRequest: ProofRequest,
        connectionId: String?
    ): Pair<RequestPresentationMessage, ProofRecord> {
        return createRequest(agentContext, Json.encodeToString(proofRequest), connectionId)
    }

    /**
     * @see IProofService
     */
    override suspend fun createRequest(
        agentContext: IAgentContext,
        proofRequest: ProofRequest
    ): Pair<RequestPresentationMessage, ProofRecord> {
        val (message, record) = createRequest(agentContext, proofRequest, null)
        val wallet = agentContext.wallet ?: throw IllegalArgumentException("Wallet is null")
        val provisioning = provisioningService.getProvisioning(wallet)

        message.addDecorator(provisioning.toServiceDecorator(), DecoratorNames.SERVICE_DECORATOR)
        record.setTag("RequestData", Base64.getEncoder().encodeToString(message.toByteArray()))

        return (Pair(message, record))
    }

    /**
     * @see IProofService
     */
    override suspend fun createRequest(
        agentContext: IAgentContext,
        proofRequestJson: String?,
        connectionId: String?
    ): Pair<RequestPresentationMessage, ProofRecord> {
        if (proofRequestJson == null) {
            throw Exception("You must provide a proof request")
        }

        if (connectionId != null) {
            val connection = connectionService.get(agentContext, connectionId)

            if (connection.state != ConnectionState.CONNECTED) {
                throw AriesFrameworkException(
                    ErrorCode.RECORD_IN_INVALID_STATE,
                    "Connection state was invalid. Expected CONNECTED, found ${connection.state}"
                )
            }
        }

        val wallet = agentContext.wallet ?: throw IllegalArgumentException("Wallet is null")

        val threadId = UUID.randomUUID().toString()
        val proofRecord = ProofRecord(threadId)
        proofRecord.connectionId = connectionId
        proofRecord.requestJson = proofRequestJson

        proofRecord.setTag(TagConstants.ROLE, TagConstants.REQUESTOR)
        proofRecord.setTag(TagConstants.LAST_THREAD_ID, threadId)
        recordService.add(wallet, proofRecord)

        val message = RequestPresentationMessage()
        message.id = threadId
        val attachment = Attachment("libindy-request-presentation-0")
        attachment.mimeType = CredentialMimeTypes.APPLICATION_JSON_MIME_TYPE
        val content = AttachmentContent()
        content.base64 = Base64.getEncoder()
            .encodeToString(Json.encodeToString(proofRequestJson).toByteArray(Charsets.UTF_8))
        attachment.data = content
        message.requests = arrayOf(attachment)
        message.threadFrom(threadId)

        return Pair(message, proofRecord)
    }

    /**
     * @see IProofService
     */
    override suspend fun processRequest(
        agentContext: IAgentContext,
        proofRequest: RequestPresentationMessage,
        connection: ConnectionRecord?
    ): ProofRecord {
        val requestAttachment =
            proofRequest.requests?.firstOrNull { it -> it.id == "libindy-request-presentation-0" }
                ?: throw AriesFrameworkException(
                    ErrorCode.INVALID_MESSAGE,
                    "Presentation request attachment not found."
                )

        val wallet = agentContext.wallet ?: throw IllegalArgumentException("Wallet is null")

        val requestJson = String(requestAttachment.data?.base64.toByteArray(), Charsets.UTF_8)
        var proofRecord: ProofRecord? = null

        try {
            val threadId = proofRequest.getThreadId()
                ?: throw IllegalArgumentException("Request thread id not found")

            proofRecord = getByThreadId(agentContext, threadId)

            proofRecord.trigger(ProofTrigger.REQUEST)
            proofRecord.requestJson = requestJson
            recordService.update(wallet, proofRecord)
        } catch (e: AriesFrameworkException) {
            if (e.errorCode != ErrorCode.RECORD_NOT_FOUND) {
                throw e
            }
        }

        val threadId = proofRequest.getThreadId()
            ?: throw IllegalArgumentException("Request thread id not found")

        if (proofRecord != null) {
            proofRecord = ProofRecord(UUID.randomUUID().toString())
            proofRecord.connectionId = connection?.id
            proofRecord.requestJson = requestJson

            proofRecord.setTag(TagConstants.ROLE, TagConstants.HOLDER)
            proofRecord.setTag(TagConstants.LAST_THREAD_ID, threadId)
            recordService.add(wallet, proofRecord)
        }

        eventAggregator.publish(
            ServiceMessageProcessingEvent(
                proofRecord!!.id,
                proofRequest.type ?: "",
                threadId
            )
        )

        return proofRecord
    }

    /**
     * @see IProofService
     */
    override suspend fun processPresentation(
        agentContext: IAgentContext,
        proof: PresentationMessage
    ): ProofRecord {
        val threadId = proof.getThreadId()
            ?: throw IllegalArgumentException("Request thread id not found")

        val proofRecord = getByThreadId(agentContext, threadId)

        val requestAttachment =
            proof.presentations?.firstOrNull { it -> it.id == "libindy-presentation-0" }
                ?: throw AriesFrameworkException(
                    ErrorCode.INVALID_MESSAGE,
                    "Presentation attachment not found."
                )

        val wallet = agentContext.wallet ?: throw IllegalArgumentException("Wallet is null")

        val proofJson = String(requestAttachment.data?.base64.toByteArray(), Charsets.UTF_8)

        if (proofRecord.state != ProofState.REQUESTED) {
            throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Proof state was invalid. Expected REQUESTED, found ${proofRecord.state}"
            )
        }

        proofRecord.proofJson = proofJson
        proofRecord.trigger(ProofTrigger.ACCEPT)
        recordService.update(wallet, proofRecord)

        eventAggregator.publish(
            ServiceMessageProcessingEvent(
                proofRecord.id,
                proof.type ?: "",
                threadId
            )
        )

        return proofRecord
    }

    /**
     * @see IProofService
     */
    override suspend fun createPresentation(
        agentContext: IAgentContext,
        proofRequest: ProofRequest,
        requestedCredentials: RequestedCredentials
    ): String {
        return createProof(agentContext, proofRequest, requestedCredentials)
    }

    /**
     * @see IProofService
     */
    private suspend fun createProof(
        agentContext: IAgentContext,
        proofRequest: ProofRequest,
        requestedCredentials: RequestedCredentials
    ): String {
        val wallet = agentContext.wallet ?: throw IllegalArgumentException("Wallet is null")
        val provisioningRecord = provisioningService.getProvisioning(wallet)

        val credentialObjects = mutableListOf<CredentialInfo>()
        requestedCredentials.getCredentialIdentifiers().forEach { credId ->
            val infoJson = indyWrapper.proverGetCredential(wallet, credId)
            val credentialInfo = Json.decodeFromString<CredentialInfo>(infoJson)

            credentialObjects.add(credentialInfo)
        }


        val schemas = buildSchema(
            agentContext,
            credentialObjects.map { it.schemaId }.distinct()
        )

        val definitions = buildCredentialDefinition(
            agentContext,
            credentialObjects.map { it.credentialDefinitionId }.distinct()
        )

        val revocationStates = buildRevocationStates(
            agentContext,
            credentialObjects,
            proofRequest,
            requestedCredentials
        )

        return indyWrapper.proverCreateProof(
            wallet,
            Json.encodeToString(proofRequest),
            Json.encodeToString(requestedCredentials),
            provisioningRecord.masterSecretId,
            schemas,
            definitions,
            revocationStates
        )
    }

    /**
     * @see IProofService
     */
    override suspend fun createPresentation(
        agentContext: IAgentContext,
        requestPresentation: RequestPresentationMessage,
        requestedCredentials: RequestedCredentials
    ): ProofRecord {
        val service =
            requestPresentation.getDecorator<ServiceDecorator>(DecoratorNames.SERVICE_DECORATOR)

        val record = processRequest(agentContext, requestPresentation, null)
        val (presentationMessage, proofRecord) = createPresentation(
            agentContext,
            record.id,
            requestedCredentials
        )

        if (service.recipientKeys == null || service.recipientKeys?.isEmpty() == true || service.serviceEndpoint == null) {
            throw AriesFrameworkException(
                ErrorCode.INVALID_MESSAGE,
                "You must provide a message with recipientKeys and serviceEndpoint"
            )
        }

        messageService.send(
            agentContext,
            presentationMessage,
            service.recipientKeys?.first()!!,
            service.serviceEndpoint!!,
            service.routingKeys?.toTypedArray(),
            null
        )

        return proofRecord
    }

    /**
     * @see IProofService
     */
    override suspend fun createPresentation(
        agentContext: IAgentContext,
        proofRecordId: String,
        requestedCredentials: RequestedCredentials
    ): Pair<PresentationMessage, ProofRecord> {
        val record = get(agentContext, proofRecordId)

        if (record.state != ProofState.REQUESTED) {
            throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Proof state was invalid. Expected ${ProofState.REQUESTED}, found ${record.state}"
            )
        }
        if (record.requestJson == null) {
            throw AriesFrameworkException(ErrorCode.INVALID_RECORD_DATA, "RequestJson not found")
        }

        val wallet = agentContext.wallet ?: throw IllegalArgumentException("Wallet is null")

        record.proofJson = createPresentation(
            agentContext,
            Json.decodeFromString<ProofRequest>(record.requestJson!!),
            requestedCredentials
        )

        record.trigger(ProofTrigger.ACCEPT)
        recordService.update(wallet, record)

        var threadId = record.getTag(TagConstants.LAST_THREAD_ID)

        if (threadId == null) {
            threadId = UUID.randomUUID().toString()
            record.setTag(TagConstants.LAST_THREAD_ID, threadId)
        }

        val proofMsg = PresentationMessage(agentContext.useMessageTypesHttps)
        proofMsg.id = UUID.randomUUID().toString()
        val attachment = Attachment("libindy-presentation-0")
        attachment.mimeType = CredentialMimeTypes.APPLICATION_JSON_MIME_TYPE
        val content = AttachmentContent()
        content.base64 = Base64.getEncoder()
            .encodeToString(Json.encodeToString(record.proofJson).toByteArray(Charsets.UTF_8))
        attachment.data = content
        proofMsg.presentations = arrayOf(attachment)

        proofMsg.threadFrom(threadId)

        return Pair(proofMsg, record)
    }

    /**
     * @see IProofService
     */
    override suspend fun rejectProofRequest(
        agentContext: IAgentContext,
        proofRecordId: String
    ) {
        val record = get(agentContext, proofRecordId)

        if (record.state != ProofState.REQUESTED) {
            throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Proof state was invalid. Expected ${ProofState.REQUESTED}, found ${record.state}"
            )
        }

        val wallet = agentContext.wallet ?: throw IllegalArgumentException("Wallet is null")

        record.trigger(ProofTrigger.REJECT)
        recordService.update(wallet, record)
    }

    /**
     * @see IProofService
     */
    override suspend fun verifyProof(
        agentContext: IAgentContext,
        proofRecordId: String
    ): Boolean {
        val record = get(agentContext, proofRecordId)

        if (record.state != ProofState.ACCEPTED) {
            throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Proof state was invalid. Expected ${ProofState.ACCEPTED}, found ${record.state}"
            )
        }

        if (record.requestJson == null || record.proofJson == null) {
            throw AriesFrameworkException(
                ErrorCode.INVALID_RECORD_DATA,
                "Found record has no RequestJson or ProofJson"
            )
        }

        return verifyProof(agentContext, record.requestJson!!, record.proofJson!!)
    }

    /**
     * @see IProofService
     */
    override suspend fun verifyProof(
        agentContext: IAgentContext,
        requestJson: String,
        proofJson: String,
        validateEncoding: Boolean
    ): Boolean {
        val proof = Json.decodeFromString<PartialProof>(proofJson)

        if (validateEncoding) {
            proof.requestedProof.revealedAttributes?.forEach { key, value ->
                if (!CredentialUtils.checkValidEncoding(value?.raw, value?.encoded)) {
                    throw AriesFrameworkException(
                        ErrorCode.INVALID_PROOF_ENCODING,
                        "The encoded value for '$key' is invalid. Expected '${
                            CredentialUtils.getEncoded(value?.raw)
                        }'. Actual '${value?.encoded}'"
                    )
                }
            }
        }

        val schemas = buildSchema(
            agentContext,
            proof.identifiers?.map { it.schemaId ?: "" }?.distinct() ?: emptyList()
        )

        val definitions = buildCredentialDefinition(
            agentContext,
            proof.identifiers?.map { it.credentialDefintionId ?: "" }?.distinct() ?: emptyList()
        )

        val revocationDefinitions = buildRevocationRegistryDefinition(
            agentContext,
            proof.identifiers?.map { it.revocationRegistryId ?: "" } ?: emptyList())

        val revocationRegistries =
            buildRevocationRegistries(agentContext, proof.identifiers ?: emptyList())

        return indyWrapper.verifierVerifyProof(
            requestJson,
            proofJson,
            schemas,
            definitions,
            revocationDefinitions,
            revocationRegistries
        )
    }

    /**
     * @see IProofService
     */
    override suspend fun isRevoked(
        agentContext: IAgentContext,
        record: CredentialRecord
    ): Boolean {
        if (record.state == CredentialState.OFFERED || record.state == CredentialState.REQUESTED) {
            return false
        }
        if (record.state == CredentialState.REVOKED || record.state == CredentialState.REJECTED) {
            return true
        }

        val wallet = agentContext.wallet ?: throw IllegalArgumentException("Wallet is null")

        val now = System.currentTimeMillis() / 1000;
        val proofRequest = ProofRequest(
            name = "revocation check",
            version = "1.0",
            nonce = indyWrapper.generateNonce()
        )
        val requestedAttributes = HashMap<String, ProofAttributeInfo>()
        val attr = ProofAttributeInfo()
        attr.name = record.credentialAttributesValues?.first()?.name
        requestedAttributes["referent1"] = attr
        proofRequest.requestedAttributes = requestedAttributes
        val nonRevoked = RevocationInterval(now.toUInt(), now.toUInt())
        proofRequest.nonRevoked = nonRevoked

        val reqCredentials = RequestedCredentials()
        val reqAttributes = HashMap<String, RequestedAttribute>()
        val reqAttr = RequestedAttribute(record.credentialId, now, true)

        reqAttributes["referent1"] = reqAttr
        reqCredentials.requestedAttributes = reqAttributes

        val proof = createProof(agentContext, proofRequest, reqCredentials)

        val isValid = verifyProof(agentContext, Json.encodeToString(proofRequest), proof)

        if (!isValid) {
            record.trigger(CredentialTrigger.REVOKE)

            record.setTag("LastRevocationCheck", now.toString())
            recordService.update(wallet, record)
        }

        return !isValid
    }

    /**
     * @see IProofService
     */
    override suspend fun isRevoked(
        agentContext: IAgentContext,
        credentialRecordId: String
    ): Boolean {
        val wallet = agentContext.wallet ?: throw IllegalArgumentException("Wallet is null")
        return isRevoked(
            agentContext,
            recordService.get<CredentialRecord>(
                wallet,
                credentialRecordId,
                RecordType.CREDENTIAL_RECORD
            ) as CredentialRecord
        )
    }

    /**
     * @see IProofService
     */
    override suspend fun list(
        agentContext: IAgentContext,
        query: ISearchQuery?,
        count: Int
    ): List<ProofRecord> {
        val wallet = agentContext.wallet ?: throw IllegalArgumentException("Wallet is null")
        return recordService.search<ProofRecord>(
            wallet,
            query,
            null,
            count,
            recordBaseType = RecordType.PROOF_RECORD
        )
    }

    /**
     * @see IProofService
     */
    override suspend fun get(agentContext: IAgentContext, proofRecordId: String): ProofRecord {
        val wallet = agentContext.wallet ?: throw IllegalArgumentException("Wallet is null")
        try {
            return recordService.get<ProofRecord>(
                wallet,
                proofRecordId,
                RecordType.PROOF_RECORD
            ) as ProofRecord
        } catch (e: Exception) {
            throw AriesFrameworkException(ErrorCode.RECORD_NOT_FOUND, "Proofrecord not found")
        }
    }

    /**
     * @see IProofService
     */
    override suspend fun listCredentialsForProofRequest(
        agentContext: IAgentContext,
        proofRequest: ProofRequest,
        attributeReferent: String
    ): List<Credential> {
        val search = CredentialsSearchForProofReq.open(
            agentContext.wallet,
            Json.encodeToString(proofRequest),
            null
        ).get()
        val searchResult = search.fetchNextCredentials(attributeReferent, 100).get()

        return Json.decodeFromString(searchResult)
    }

    private suspend fun getByThreadId(context: IAgentContext, threadId: String): ProofRecord {
        val search =
            this.list(context, SearchQuery.equal(TagConstants.LAST_THREAD_ID, threadId), 1)

        when (search.count()) {
            0 -> throw AriesFrameworkException(
                ErrorCode.RECORD_NOT_FOUND,
                "Proof record not found by thread id : $threadId"
            )
            1 -> return search.single()
            else -> throw AriesFrameworkException(
                ErrorCode.RECORD_IN_INVALID_STATE,
                "Multiple proof records found by thread id : $threadId"
            )
        }
    }

    private fun buildSchema(agentContext: IAgentContext, schemaIds: List<String>): String {
        val result = HashMap<String, String>()

        schemaIds.forEach { schemaId ->
            val ledgerSchema = ledgerService.lookupSchema(agentContext, schemaId)
            result[schemaId] = Json.encodeToString(ledgerSchema)
        }

        return Json.encodeToString(result)
    }

    private fun buildCredentialDefinition(
        agentContext: IAgentContext,
        credDefIds: List<String>
    ): String {
        val result = HashMap<String, String>()

        credDefIds.forEach { credDefId ->
            val ledgerSchema =
                ledgerService.lookupDefinition(agentContext, credDefId)
            result[credDefId] = Json.encodeToString(ledgerSchema)
        }

        return Json.encodeToString(result)
    }

    private suspend fun buildRevocationStates(
        agentContext: IAgentContext,
        credentialObjects: List<CredentialInfo>,
        proofRequest: ProofRequest,
        requestedCredentials: RequestedCredentials
    ): String {
        val allCredentials = mutableListOf<RequestedAttribute?>()
        requestedCredentials.requestedAttributes?.values?.let {
            allCredentials.addAll(it)
        }
        requestedCredentials.requestedPredicates?.values?.let {
            allCredentials.addAll(it)
        }

        val result = HashMap<String, HashMap<String, String>>()
        for (reqCred in allCredentials) {
            val credential = credentialObjects.first { it -> it.referent == reqCred?.credentialId }
            if (proofRequest.nonRevoked == null || proofRequest.nonRevoked?.to == null) {
                continue
            }

            val registryDefinition = ledgerService.lookupRevocationRegistryDefinition(
                agentContext,
                credential.revocationRegistryId
            )

            val delta = ledgerService.lookupRevocationRegistryDelta(
                agentContext,
                credential.revocationRegistryId,
                0, proofRequest.nonRevoked!!.to?.toLong() ?: 0L
            )

            val tailsFile =
                tailsService.ensureTailsExists(
                    agentContext,
                    credential.revocationRegistryId
                )
            val tailsReader = tailsService.openTails(tailsFile)

            val state = indyWrapper.createRevocationState(
                tailsReader.blobStorageReaderHandle,
                registryDefinition.objectJson,
                delta.objectJson,
                delta.timestamp,
                credential.credentialRevocationId
            )

            if (!result.contains(credential.revocationRegistryId)) {
                result[credential.revocationRegistryId] = HashMap()
            }

            reqCred?.timestamp = delta.timestamp
            if (!(result[credential.revocationRegistryId]?.contains("${delta.timestamp}"))!!) {
                result[credential.revocationRegistryId]?.set(
                    "${delta.timestamp}",
                    Json.encodeToString(state)
                )
            }
        }

        return Json.encodeToString(result)
    }

    private fun buildRevocationRegistryDefinition(
        agentContext: IAgentContext,
        revocationRegistryIds: List<String>
    ): String {
        val result = HashMap<String, String>()

        revocationRegistryIds.forEach { revocId ->
            val ledgerSchema =
                ledgerService.lookupRevocationRegistryDefinition(agentContext, revocId)
            result[revocId] = Json.encodeToString(ledgerSchema)
        }

        return Json.encodeToString(result)
    }

    private fun buildRevocationRegistries(
        agentContext: IAgentContext,
        proofIdentifiers: List<ProofIdentifier>
    ): String {
        val result = HashMap<String, HashMap<String, String>>()

        proofIdentifiers.forEach { identifier ->
            if (identifier.timestamp != null && identifier.revocationRegistryId != null) {
                val timestamp = identifier.timestamp!!
                val revocationRegistryId = identifier.revocationRegistryId!!
                val revocationRegistry = ledgerService.lookupRevocationRegistry(
                    agentContext,
                    revocationRegistryId,
                    timestamp.toLong()
                )
                val identValue = HashMap<String, String>()
                identValue[timestamp] = Json.encodeToString(revocationRegistry)
                result[revocationRegistryId] = identValue
            }
        }

        return Json.encodeToString(result)
    }

    private fun checkProofProposalParameters(proofProposal: ProofProposal) {
        if (proofProposal.proposedAttributes != null) {
            if (proofProposal.proposedAttributes?.count()!! > 1) {
                val attrList = proofProposal.proposedAttributes
                val referents = HashMap<String, ProposedAttribute>()

                for (ind in attrList?.indices!!) {
                    val attr = attrList[ind]
                    if (attr.referent.isNullOrEmpty()) {
                        throw AriesFrameworkException(
                            ErrorCode.INVALID_PARAMETER_FORMAT,
                            "All attributes must have a referent"
                        )
                    }
                    if (referents.containsKey(attr.referent)) {
                        if (referents[attr.referent]?.issuerDid != attr.issuerDid ||
                            referents[attr.referent]?.schemaId != attr.schemaId ||
                            referents[attr.referent]?.credentialDefintionId != attr.credentialDefintionId
                        ) {
                            throw AriesFrameworkException(
                                ErrorCode.INVALID_PARAMETER_FORMAT,
                                "All attributes that share a referent must have identical requirements"
                            )
                        } else {
                            continue
                        }
                    } else {
                        referents[attr.referent!!] = attr
                    }
                }
            }
            if (proofProposal.proposedPredicates != null) {
                if (proofProposal.proposedPredicates?.count()!! > 1) {
                    val predList = proofProposal.proposedPredicates
                    val referents = HashMap<String, ProposedPredicate>()

                    for (ind in predList?.indices!!) {
                        val pred = predList[ind]
                        if (pred.referent.isNullOrEmpty()) {
                            throw AriesFrameworkException(
                                ErrorCode.INVALID_PARAMETER_FORMAT,
                                "All predicates must have a referent"
                            )
                        }
                        if (referents.containsKey(pred.referent)) {
                            throw AriesFrameworkException(
                                ErrorCode.INVALID_PARAMETER_FORMAT,
                                "Proposed predicates must all have unique referents"
                            )
                        } else {
                            referents[pred.referent!!] = pred
                        }
                    }
                }
            }
        }
    }
}