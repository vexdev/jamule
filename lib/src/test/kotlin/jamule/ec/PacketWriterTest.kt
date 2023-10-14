package jamule.ec

import jamule.ec.packet.Flags
import jamule.ec.packet.Packet
import jamule.ec.packet.PacketWriter
import jamule.ec.tag.TagEncoder
import jamule.ec.tag.UByteTag
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class PacketWriterTest {
    private val logger: Logger = LoggerFactory.getLogger(PacketWriterTest::class.java)

    @Test
    fun `write status request`() {
        val tagEncoder = TagEncoder(logger)
        val writer = PacketWriter(logger, tagEncoder)
        val packet = Packet(
            ECOpCode.EC_OP_STAT_REQ,
            listOf(UByteTag(ECTagName.EC_TAG_DETAIL_LEVEL, ECDetailLevel.EC_DETAIL_CMD.value)),
            Flags(zlibCompressed = false, utf8 = true, hasId = false, accepts = false)
        )
        val outputStream = ByteArrayOutputStream()

        writer.write(packet, outputStream)

        assertContentEquals(SamplePackets.statusRequest, outputStream.toByteArray())
    }

}