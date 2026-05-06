package jamule

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jamule.ec.tag.special.PartFileTag
import jamule.ec.tag.special.SharedFileTag
import jamule.model.AmuleCategory
import jamule.model.DownloadCommand
import jamule.request.*
import jamule.response.*
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.seconds

class AmuleClientTest : FunSpec({
    val connection = mockk<AmuleConnection>()
    val logger = LoggerFactory.getLogger(this::class.java)
    val client = AmuleClient(connection, logger) { }

    beforeTest {
        clearMocks(connection)
    }

    test("should get server stats") {
        val stats = StatsResponse(
            connectionState = null,
            uploadOverhead = 0,
            downloadOverhead = 0,
            bannedCount = 0,
            loggerMessage = emptyList(),
            totalSentBytes = 0,
            totalReceivedBytes = 0,
            sharedFileCount = 0,
            uploadSpeed = 0,
            downloadSpeed = 0,
            uploadSpeedLimit = 0,
            downloadSpeedLimit = 0,
            uploadQueueLength = 0,
            totalSourceCount = 0,
            ed2kUsers = 0,
            kadUsers = 0,
            ed2kFiles = 0,
            kadFiles = 0,
            kadNodes = 0,
            kadFirewalledUdp = null,
            kadIndexedSources = null,
            kadIndexedKeywords = null,
            kadIndexedNotes = null,
            kadIndexedLoad = null,
            kadIpAddress = null,
            kadIsRunningInLanMode = null,
            buddyStatus = null,
            buddyIp = null,
            buddyPort = null,
        )
        every { connection.sendRequest(match { it is StatsRequest }) } returns stats

        client.getStats().getOrThrow() shouldBe stats
    }

    test("should search for a file") {
        every { connection.sendRequest(match { it is SearchRequest && it.query == "test" }) } returns StringsResponse("ok")

        client.searchAsync("test").getOrThrow() shouldBe "ok"
    }

    test("should get search status") {
        every { connection.sendRequest(match { it is SearchStatusRequest }) } returns SearchStatusResponse(0.75f)

        client.searchStatus().getOrThrow() shouldBe 0.75f
    }

    test("should get search results") {
        val results = SearchResultsResponse(emptyList())
        every { connection.sendRequest(match { it is SearchResultsRequest }) } returns results

        client.searchResults().getOrThrow() shouldBe results
    }

    test("should stop searches") {
        every { connection.sendRequest(match { it is SearchStopRequest }) } returns MiscDataResponse

        client.searchStop().isSuccess shouldBe true
    }

    test("should get download queue") {
        val file = mockk<PartFileTag>()
        every { connection.sendRequest(match { it is DownloadQueueRequest }) } returns DownloadQueueResponse(listOf(file))

        client.getDownloadQueue().getOrThrow() shouldContainExactly listOf(file)
    }

    test("should get shared files list") {
        val file = mockk<SharedFileTag>()
        every { connection.sendRequest(match { it is SharedFilesRequest }) } returns SharedFilesResponse(listOf(file))

        client.getSharedFiles().getOrThrow() shouldContainExactly listOf(file)
    }

    test("should create category") {
        val category = AmuleCategory(1, "test", "/finished", "Some Comment", 10, 1)
        every { connection.sendRequest(match { it is CreateCategoryRequest && it.amuleCategory == category }) } returns NoopResponse

        client.createCategory(category).isSuccess shouldBe true
    }

    test("should get list of categories") {
        val categories = listOf(AmuleCategory(1, "cat"))
        every { connection.sendRequest(match { it is GetPreferencesRequest }) } returns PrefsCategoriesResponse(categories)

        client.getCategories().getOrThrow() shouldBe categories
    }

    test("should download then pause then delete") {
        val hash = byteArrayOf(1, 2, 3)
        every { connection.sendRequest(match { it is DownloadSearchResultRequest && it.hash.contentEquals(hash) }) } returns StringsResponse("queued")
        every {
            connection.sendRequest(
                match {
                    it is DownloadCommandRequest &&
                        it.fileHash.contentEquals(hash) &&
                        it.status == DownloadCommand.PAUSE
                }
            )
        } returns NoopResponse
        every {
            connection.sendRequest(
                match {
                    it is DownloadCommandRequest &&
                        it.fileHash.contentEquals(hash) &&
                        it.status == DownloadCommand.DELETE
                }
            )
        } returns NoopResponse

        client.downloadSearchResult(hash).getOrThrow()
        client.sendDownloadCommand(hash, DownloadCommand.PAUSE).getOrThrow()
        client.sendDownloadCommand(hash, DownloadCommand.DELETE).getOrThrow()
    }

    test("should wait for search completion before fetching results") {
        val results = SearchResultsResponse(emptyList())
        every { connection.sendRequest(match { it is SearchRequest && it.query == "linux" }) } returns StringsResponse("started")
        every { connection.sendRequest(match { it is SearchStatusRequest }) } returnsMany
            (List(15) { SearchStatusResponse(0f) } + SearchStatusResponse(1f))
        every { connection.sendRequest(match { it is SearchResultsRequest }) } returns results

        client.searchSync("linux", timeout = 1.seconds).getOrThrow() shouldBe results
        verify(exactly = 16) { connection.sendRequest(match { it is SearchStatusRequest }) }
    }
})
