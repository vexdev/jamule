package jamule.ec.packet

import jamule.ec.tag.TagEncoder
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalStdlibApi::class)
class PacketWriterTest {
    private val logger: Logger = LoggerFactory.getLogger(PacketWriterTest::class.java)

    @Test
    fun `writes sample packets`() {
        val writer = PacketWriter(TagEncoder(logger), logger)

        for ((expectedPacket, packet) in SamplePackets.packetMap) {
            val outputStream = ByteArrayOutputStream()

            writer.write(packet, outputStream)

            assertEquals(expectedPacket.toHexString(), outputStream.toByteArray().toHexString())
        }
    }

}