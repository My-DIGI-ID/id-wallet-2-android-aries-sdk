package com.digital_enabling.android_aries_sdk.credential

import com.digital_enabling.android_aries_sdk.agents.abstractions.IAgentContext
import com.digital_enabling.android_aries_sdk.configuration.AgentOptions
import com.digital_enabling.android_aries_sdk.credential.abstractions.ITailsService
import com.digital_enabling.android_aries_sdk.credential.models.TailsWriterConfig
import com.digital_enabling.android_aries_sdk.ledger.abstractions.ILedgerService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperledger.indy.sdk.blob_storage.BlobStorageReader
import org.hyperledger.indy.sdk.blob_storage.BlobStorageWriter
import java.util.concurrent.ConcurrentHashMap
import kotlin.Exception


class DefaultTailsService (
    /**
     * The ledger service
     */
    private val ledgerService: ILedgerService,
    /**
     * The agent options
     */
    private val agentOptions: AgentOptions) : ITailsService {

    /**
     * The BLOB readers
     */
    companion object {
        var blobReaders: ConcurrentHashMap<String, BlobStorageReader> = ConcurrentHashMap()
    }

    /**
     * @see ITailsService
     */
    override suspend fun openTails(filename: String): BlobStorageReader {
        val baseDir = agentOptions.revocationRegistryDirectory;

        val tailsWriterConfig = TailsWriterConfig(baseDir, "", filename)

        var blobReader : BlobStorageReader
        if(blobReaders.containsKey(filename)) {
            try{
                blobReader = blobReaders.getValue(filename)
                return blobReader
            }
            catch (ex: Exception) {
                throw ex
            }
        }
        blobReader = BlobStorageReader.openReader("default", Json.encodeToString(tailsWriterConfig)).get()
        blobReaders[filename] = blobReader
        return blobReader
    }

    /**
     * @see ITailsService
     */
    override suspend fun createTails(): BlobStorageWriter {
        val tailsWriterConfig = TailsWriterConfig(agentOptions.revocationRegistryDirectory, "")
        val blobWriter = BlobStorageWriter.openWriter("default", Json.encodeToString(tailsWriterConfig)).get()
        return blobWriter

    }


    /**
     * @see ITailsService
     */
    //TODO: finish method
    override suspend fun ensureTailsExists(
        agentContext: IAgentContext,
        revocationRegistryId: String
    ): String {
        /*
        val revocationRegistry = ledgerService.lookupRevocationRegistryDefinition(agentContext, revocationRegistryId)
        //var tailsUri = JObject.Parse(revocationRegistry.objectJson)["value"]["tailsLocation"].ToObject<string>()
        val tailsUri = Json.encodeToString(revocationRegistry.objectJson)

        //val tailsFileName = JObject.Parse(revocationRegistry.objectJson)["value"]["tailsHash"].ToObject<string>()
        val tailsFileName = Json.encodeToString(revocationRegistry.objectJson)

        val tailsfile = Path.Combine(agentOptions.revocationRegistryDirectory, tailsFileName)
        var hash = Multibase.Base58.Decode(tailsFileName)

        if (!Directory.Exists(agentOptions.revocationRegistryDirectory))
        {
            Directory.CreateDirectory(agentOptions.revocationRegistryDirectory)
        }

        try
        {
            if (!File.Exists(tailsfile))
            {
                var bytes = httpClient.getByteArray(new Uri(tailsUri));

                // Check hash
                using var sha256 = SHA256.Create();
                var computedHash = sha256.ComputeHash(bytes);

                if (!computedHash.SequenceEqual(hash))
                {
                    throw new Exception("Tails file hash didn't match");
                }

                File.WriteAllBytes(
                    path: tailsfile,
                    bytes: bytes);
            }
        }
        catch (e:Exception)
        {
            throw AriesFrameworkException(ErrorCode.valueOf(
                "Unable to retrieve revocation registry from the specified URL '{${tailsUri}'. Error: {${e.message}}"
            ))
        }

        return Path.GetFileName(tailsfile);

         */
        return ""
    }


}
