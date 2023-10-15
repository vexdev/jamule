package jamule

import AmuleClient
import io.kotest.core.spec.style.FunSpec
import jamule.ec.packet.PacketParserTest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
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
    val amuleClient = AmuleClient(amule.host, 4712, logger = logger)

    test("authenticate should not fail") {
        amuleClient.authenticate("password")
    }
})