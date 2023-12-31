package jamule

import jamule.ec.EcPrefs
import jamule.exception.CommunicationException
import jamule.model.AmuleCategory
import jamule.model.AmuleFile
import jamule.model.AmuleTransferringFile
import jamule.model.DownloadCommand
import jamule.request.*
import jamule.response.*
import org.slf4j.Logger
import org.slf4j.helpers.NOPLogger
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class AmuleClient(
    host: String,
    port: Int,
    password: String,
    timeout: Int = 0,
    private val logger: Logger = NOPLogger.NOP_LOGGER
) {
    private val amuleConnection = AmuleConnection(host, port, timeout, password, logger)

    /**
     * Sets up a new connection to the server.
     */
    fun reconnect() = amuleConnection.reconnect()

    /**
     * Queries the server for statistics.
     */
    fun getStats(): Result<StatsResponse> {
        logger.info("Getting stats...")
        return when (val response = amuleConnection.sendRequest(StatsRequest())) {
            is StatsResponse -> Result.success(response).also { logger.info("Stats: $response") }
            else -> Result.failure(CommunicationException("Unable to get stats, got response: $response"))
        }
    }

    /**
     * Starts a search for the given query. This is an asynchronous operation.
     */
    fun searchAsync(
        query: String,
        searchType: SearchType = SearchType.GLOBAL,
        filters: SearchFilters = SearchFilters(),
    ): Result<String> {
        logger.info("Searching for $query...")
        return when (val response = amuleConnection.sendRequest(SearchRequest(query, searchType, filters))) {
            is StringsResponse -> Result.success(response.string).also { logger.info("Search started") }
            is ErrorResponse -> Result.failure(response)
            else -> Result.failure(CommunicationException("Unable to start search, got response: $response"))
        }
    }

    /**
     * Gets the status of a search, as a float 0-1.
     */
    fun searchStatus(): Result<Float> {
        logger.info("Getting search status...")
        return when (val response = amuleConnection.sendRequest(SearchStatusRequest())) {
            is SearchStatusResponse -> Result.success(response.status)
                .also { logger.info("Search status: ${response.status}") }

            else -> Result.failure(CommunicationException("Unable to get search status, got response: $response"))
        }
    }

    /**
     * Gets the results of a search.
     */
    fun searchResults(): Result<SearchResultsResponse> {
        logger.info("Getting search results...")
        return when (val response = amuleConnection.sendRequest(SearchResultsRequest())) {
            is SearchResultsResponse -> Result.success(response)
                .also { logger.info("Found ${response.files} results") }

            else -> Result.failure(CommunicationException("Unable to get search results, got response: $response"))
        }
    }

    /**
     * Performs a search and waits for the results.
     */
    fun searchSync(
        query: String,
        searchType: SearchType = SearchType.GLOBAL,
        filters: SearchFilters = SearchFilters(),
        timeout: Duration = 5.seconds,
    ): Result<SearchResultsResponse> {
        searchAsync(query, searchType, filters).getOrElse { return Result.failure(it) }
        // For some reason, the server returns always 100 if we don't wait a bit
        for (i in 0..<15) {
            searchStatus().getOrElse { return Result.failure(it) }
            Thread.sleep(200)
        }
        val start = System.currentTimeMillis()
        while (searchStatus().getOrElse { return Result.failure(it) } < 1f) {
            if (System.currentTimeMillis() - start > timeout.inWholeMilliseconds) {
                logger.error("Search timed out")
                break
                // Let's not return an error here, as the search might have finished
            }
            Thread.sleep(100)
        }
        return searchResults()
    }

    /**
     * Stops a search that is in progress.
     */
    fun searchStop(): Result<Unit> {
        logger.info("Stopping search...")
        return when (val response = amuleConnection.sendRequest(SearchStopRequest())) {
            is MiscDataResponse -> Result.success(Unit).also { logger.info("Search stopped") }
            else -> Result.failure(CommunicationException("Unable to stop search, got response: $response"))
        }
    }

    /**
     * Downloads a search result through its hash.
     */
    fun downloadSearchResult(hash: ByteArray): Result<Unit> {
        logger.info("Downloading search result...")
        return when (val response = amuleConnection.sendRequest(DownloadSearchResultRequest(hash))) {
            is StringsResponse -> Result.success(Unit).also { logger.info("Search result downloaded") }
            else -> Result.failure(CommunicationException("Unable to download search result, got response: $response"))
        }
    }

    /**
     * Downloads an ed2k link in the following format:
     * ed2k://|file|<filename>|<size>|<hash>|/
     */
    fun downloadEd2kLink(link: String): Result<Unit> {
        logger.info("Downloading ed2k link...")
        return when (val response = amuleConnection.sendRequest(AddLinkRequest(link))) {
            is NoopResponse -> Result.success(Unit).also { logger.info("Ed2k link downloaded") }
            is ErrorResponse -> Result.failure(response)
            else -> Result.failure(CommunicationException("Unable to download ed2k link, got response: $response"))
        }
    }

    /**
     * Returns the full download queue of the server.
     */
    fun getDownloadQueue(): Result<List<AmuleTransferringFile>> {
        logger.info("Getting download queue...")
        return when (val response = amuleConnection.sendRequest(DownloadQueueRequest())) {
            is DownloadQueueResponse -> Result.success(response.partFiles)
            else -> Result.failure(CommunicationException("Unable to get download queue, got response: $response"))
        }
    }

    /**
     * Returns the list of all files managed by amule.
     */
    fun getSharedFiles(): Result<List<AmuleFile>> {
        logger.info("Getting shared files list...")
        return when (val response = amuleConnection.sendRequest(SharedFilesRequest())) {
            is SharedFilesResponse -> Result.success(response.sharedFiles)
            else -> Result.failure(CommunicationException("Unable to get shared files list, got response: $response"))
        }
    }

    /**
     * Creates a category in amule.
     */
    fun createCategory(category: AmuleCategory): Result<Unit> {
        logger.info("Creating a category: $category")
        return when (val response = amuleConnection.sendRequest(CreateCategoryRequest(category))) {
            is NoopResponse -> Result.success(Unit)
            is ErrorResponse -> Result.failure(response)
            else -> Result.failure(CommunicationException("Unable to create category, got response: $response"))
        }
    }

    /**
     * Returns the list of all categories in amule.
     */
    fun getCategories(): Result<List<AmuleCategory>> {
        logger.info("Getting categories...")
        return when (val response = amuleConnection.sendRequest(GetPreferencesRequest(EcPrefs.EC_PREFS_CATEGORIES))) {
            is PrefsCategoriesResponse -> Result.success(response.categories)
            is EmptyPreferencesResponse -> Result.success(emptyList())
            else -> Result.failure(CommunicationException("Unable to get categories, got response: $response"))
        }
    }

    /**
     * Sets the category of a file.
     */
    @OptIn(ExperimentalStdlibApi::class)
    fun setFileCategory(hash: ByteArray, categoryId: Long): Result<Unit> {
        logger.info("Setting file category...")
        // Check that the file is being downloaded
        return runCatching {
            val downloadQueue = getDownloadQueue().getOrThrow()
            val file = downloadQueue.firstOrNull { it.fileHashHexString == hash.toHexString() }
            if (file == null) {
                logger.warn("File ${hash.toHexString()} not found in download queue")
                throw CommunicationException("File ${hash.toHexString()} not found in download queue")
            }
            when (val response = amuleConnection.sendRequest(SetFileCategoryRequest(hash, categoryId))) {
                is NoopResponse -> Unit
                is ErrorResponse -> throw response
                else -> throw CommunicationException("Unable to set file category, got response: $response")
            }
        }
    }

    /**
     * Sends a download command to a file, such as pause, resume, etc.
     */
    fun sendDownloadCommand(hash: ByteArray, command: DownloadCommand): Result<Unit> {
        logger.info("Sending download command...")
        return when (val response = amuleConnection.sendRequest(DownloadCommandRequest(hash, command))) {
            is NoopResponse -> Result.success(Unit)
            is ErrorResponse -> Result.failure(response)
            else -> Result.failure(CommunicationException("Unable to send download command, got response: $response"))
        }
    }

    companion object {
        const val CLIENT_NAME = "jAmule"
    }

}