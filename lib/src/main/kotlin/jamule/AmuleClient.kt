package jamule

import jamule.auth.PasswordHasher
import jamule.exception.AuthFailedException
import jamule.exception.CommunicationException
import jamule.request.*
import jamule.response.*
import org.slf4j.Logger
import org.slf4j.helpers.NOPLogger
import java.net.Socket
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class AmuleClient(
    ipv4: String,
    port: Int,
    private val timeout: Int = 0,
    private val logger: Logger = NOPLogger.NOP_LOGGER
) : AutoCloseable {
    private val socket: Socket = Socket(ipv4, port).apply { soTimeout = timeout }
    private val amuleConnection = AmuleConnection(socket, logger)

    override fun close() = amuleConnection.close()

    @OptIn(ExperimentalUnsignedTypes::class)
    fun authenticate(password: String) {
        logger.info("Authenticating...")
        val authSalt = amuleConnection.sendRequest(SaltRequest())
        if (authSalt !is AuthSaltResponse) {
            throw CommunicationException("Unable to get auth salt")
        }
        val saltedPassword = PasswordHasher.hash(password, authSalt.salt)
        val authResponse = amuleConnection.sendRequest(AuthRequest(saltedPassword))
        if (authResponse !is AuthOkResponse) {
            if (authResponse is AuthFailedResponse)
                throw AuthFailedException("Auth failed: ${authResponse.reason}")
            else
                throw CommunicationException("Unable to authenticate")
        }
        logger.info("Authenticated with server version ${authResponse.version}")
    }

    /**
     * Queries the server for statistics.
     */
    fun getStats(): StatsResponse {
        logger.info("Getting stats...")
        val statsResponse = amuleConnection.sendRequest(StatsRequest())
        if (statsResponse !is StatsResponse) {
            throw CommunicationException("Unable to get stats")
        }
        logger.info("Stats: $statsResponse")
        return statsResponse
    }

    /**
     * Starts a search for the given query. This is an asynchronous operation.
     */
    fun searchAsync(
        query: String,
        searchType: SearchType = SearchType.GLOBAL,
        filters: SearchFilters = SearchFilters(),
    ) {
        logger.info("Searching for $query...")
        val searchResponse = amuleConnection.sendRequest(SearchRequest(query, searchType, filters))
        if (searchResponse !is StringsResponse) {
            throw CommunicationException("Unable to search")
        }
        logger.info("Search: ${searchResponse.string}")
    }

    /**
     * Gets the status of a search, as a float 0-1.
     */
    fun searchStatus(): Float {
        logger.info("Getting search status...")
        val searchStatusResponse = amuleConnection.sendRequest(SearchStatusRequest())
        if (searchStatusResponse !is SearchStatusResponse) {
            throw CommunicationException("Unable to get search status")
        }
        logger.info("Search status: ${searchStatusResponse.status}")
        return searchStatusResponse.status
    }

    /**
     * Gets the results of a search.
     */
    fun searchResults(): SearchResultsResponse {
        logger.info("Getting search results...")
        val searchResultsResponse = amuleConnection.sendRequest(SearchResultsRequest())
        if (searchResultsResponse !is SearchResultsResponse) {
            throw CommunicationException("Unable to get search results")
        }
        logger.info("Search results: ${searchResultsResponse.files}")
        return searchResultsResponse
    }

    /**
     * Performs a search and waits for the results.
     */
    fun searchSync(
        query: String,
        searchType: SearchType = SearchType.GLOBAL,
        filters: SearchFilters = SearchFilters(),
        timeout: Duration = 5.seconds,
    ): SearchResultsResponse {
        searchAsync(query, searchType, filters)
        // For some reason, the server returns always 100 if we don't wait a bit
        for (i in 0..<15) {
            searchStatus()
            Thread.sleep(200)
        }
        val start = System.currentTimeMillis()
        while (searchStatus() < 1f) {
            if (System.currentTimeMillis() - start > timeout.inWholeMilliseconds) {
                throw CommunicationException("Search timed out")
            }
            Thread.sleep(100)
        }
        return searchResults()
    }

    /**
     * Stops a search that is in progress.
     */
    fun searchStop() {
        logger.info("Stopping search...")
        val searchStopResponse = amuleConnection.sendRequest(SearchStopRequest())
        if (searchStopResponse !is MiscDataResponse) {
            throw CommunicationException("Unable to stop search")
        }
        logger.info("Search stopped")
    }

    companion object {
        const val CLIENT_NAME = "jAmule"
    }

}