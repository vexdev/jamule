package jamule.ec

import jamule.ec.packet.PacketWriter
import jamule.ec.tag.TagEncoder
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class PacketWriterTest {
    private val logger: Logger = LoggerFactory.getLogger(PacketWriterTest::class.java)

    @OptIn(ExperimentalUnsignedTypes::class, ExperimentalStdlibApi::class)
    @Test
    fun `writes sample packets`() {
        val writer = PacketWriter(logger, TagEncoder(logger))

        for ((expectedPacket, packet) in SamplePackets.packetMap) {
            val outputStream = ByteArrayOutputStream()

            writer.write(packet, outputStream)

            assertEquals(expectedPacket.toHexString(), outputStream.toByteArray().toHexString())
        }
    }

}