package jamule

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import jamule.ec.packet.PacketParserTest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.images.builder.ImageFromDockerfile
import org.testcontainers.images.builder.dockerfile.DockerfileBuilder

class AmuleFailedConnectionTest : FunSpec({
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
    val amuleClient = AmuleClient(amule.host, 4712, "invalid", logger = logger)

    test("authenticate should fail") {
        val exception = shouldThrow<Exception> { amuleClient.reconnect() }
        exception shouldNotBe null
        exception.message shouldBe "Authentication failed: wrong password."
    }
})