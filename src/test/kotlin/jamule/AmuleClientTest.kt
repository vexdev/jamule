package jamule

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import jamule.ec.packet.PacketParserTest
import jamule.model.AmuleCategory
import jamule.model.DownloadCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.images.builder.ImageFromDockerfile
import org.testcontainers.images.builder.dockerfile.DockerfileBuilder
import kotlin.math.roundToLong


class AmuleClientTest : FunSpec({
    val logger: Logger = LoggerFactory.getLogger(PacketParserTest::class.java)
    val amule = GenericContainer(
        ImageFromDockerfile()
            .withDockerfileFromBuilder { builder: DockerfileBuilder ->
                builder
                    .from("m4dfry/amule-adunanza")
                    .expose(4712)
                    .build()
            })
        .withExposedPorts(4712)
        .withLogConsumer(Slf4jLogConsumer(logger))
    val amuleClient = AmuleClient(amule.host, 4712, "amule", logger = logger)

    test("should get server stats") {
        amuleClient.getStats().isSuccess shouldBe true
    }

    test("should search for a file") {
        amuleClient.searchAsync("test").isSuccess shouldBe true
    }

    test("should get search status") {
        amuleClient.searchAsync("linux").isSuccess shouldBe true
        amuleClient.searchStatus().isSuccess shouldBe true
    }

    test("should get search results") {
        amuleClient.searchSync("linux").isSuccess shouldBe true
        amuleClient.searchResults().isSuccess shouldBe true
    }

    test("should stop searches") {
        amuleClient.searchAsync("linux").isSuccess shouldBe true
        amuleClient.searchStop().isSuccess shouldBe true
    }

    test("should get download queue") {
        val files = amuleClient.getDownloadQueue()
        files.isSuccess shouldBe true
        logger.info("Download queue: $files")
    }

    test("should get shared files list") {
        val files = amuleClient.getSharedFiles()
        files.isSuccess shouldBe true
        logger.info("Shared files found ${files.getOrNull()!!.size}")
    }

    test("should create category") {
        val category =
            AmuleCategory((Math.random() * Int.MAX_VALUE).roundToLong(), "test", "/finished", "Some Comment", 1, 10)
        val result = amuleClient.createCategory(category)
        result.isSuccess shouldBe true
    }

    test("should get list of categories") {
        val categories = amuleClient.getCategories().getOrThrow()
        logger.info("Categories found ${categories.size}")
    }

    test("should download then pause then delete") {
        val toDownload = amuleClient.searchSync("linux").getOrThrow().files.first()
        amuleClient.downloadSearchResult(toDownload.hash).getOrThrow()
        amuleClient.sendDownloadCommand(toDownload.hash, DownloadCommand.PAUSE).getOrThrow()
        amuleClient.sendDownloadCommand(toDownload.hash, DownloadCommand.DELETE).getOrThrow()
    }

})