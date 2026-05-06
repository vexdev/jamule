package jamule

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.shouldNotBe
import jamule.ec.packet.PacketParserTest
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AmuleFailedConnectionTest : FunSpec({
    val logger: Logger = LoggerFactory.getLogger(PacketParserTest::class.java)
    val amule = newAmuleContainer(logger)
    lateinit var amuleClient: AmuleClient

    beforeSpec {
        amule.start()
        amuleClient = AmuleClient(amule.host, amule.amuleEcPort(), "invalid", logger = logger)
    }

    afterSpec {
        amule.stop()
    }

    test("authenticate should fail") {
        val exception = shouldThrow<Exception> { amuleClient.reconnect() }
        exception shouldNotBe null
        exception.message shouldContain "Authentication failed"
    }
})
