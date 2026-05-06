package jamule

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import jamule.ec.packet.PacketParserTest
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AmuleClientIntegrationTest : FunSpec({
    val logger: Logger = LoggerFactory.getLogger(PacketParserTest::class.java)
    val amule = newAmuleContainer(logger)
    lateinit var amuleClient: AmuleClient

    beforeSpec {
        amule.start()
        amuleClient = AmuleClient(amule.host, amule.amuleEcPort(), "amule", logger = logger)
    }

    afterSpec {
        amule.stop()
    }

    test("should get server stats from a running amule container") {
        amuleClient.getStats().isSuccess shouldBe true
    }
})
