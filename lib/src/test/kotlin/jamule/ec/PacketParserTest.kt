package jamule.ec

import jamule.ec.packet.PacketParser
import jamule.ec.tag.TagParser
import jamule.ec.tag.UByteTag
import jamule.ec.tag.UShortTag
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals

class PacketParserTest {
    private val logger: Logger = LoggerFactory.getLogger(PacketParserTest::class.java)

    @Test
    fun `parses status request`() {
        val parser = PacketParser(TagParser(logger), logger)

        val packet = parser.parse(SamplePackets.statusRequest.inputStream())

        assertEquals(ECOpCode.EC_OP_STAT_REQ, packet.opCode)
        assertEquals(1, packet.tags.size)
        assertEquals(UByteTag(ECTagName.EC_TAG_DETAIL_LEVEL, ECDetailLevel.EC_DETAIL_FULL.value), packet.tags[0])
    }

    @Test
    fun `parses status response`() {
        val parser = PacketParser(TagParser(logger), logger)

        val packet = parser.parse(SamplePackets.statusResponse.inputStream())

        assertEquals(ECOpCode.EC_OP_STATS, packet.opCode)
        assertEquals(16, packet.tags.size)
        assertEquals(UShortTag(ECTagName.EC_TAG_STATS_UL_SPEED, 1664u), packet.tags[0])
    }

}