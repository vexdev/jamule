package jamule

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import jamule.ec.packet.PacketParserTest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.images.builder.ImageFromDockerfile
import org.testcontainers.images.builder.dockerfile.DockerfileBuilder


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
    val amuleClient = AmuleClient(amule.host, 4712, logger = logger)
    amuleClient.authenticate("amule")

    test("should get server stats") {
        amuleClient.getStats()
    }

    test("should search for a file") {
        amuleClient.searchAsync("test")
    }

    test("should get search status") {
        amuleClient.searchAsync("linux")
        amuleClient.searchStatus()
    }

    test("should get search results") {
        amuleClient.searchSync("linux")
        amuleClient.searchResults()
    }

})