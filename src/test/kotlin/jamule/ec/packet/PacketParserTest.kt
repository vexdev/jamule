package jamule.ec.packet

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.tag.TagParser
import jamule.ec.tag.UShortTag
import jamule.exception.InvalidECException
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalUnsignedTypes::class)
class PacketParserTest {
    private val logger: Logger = LoggerFactory.getLogger(PacketParserTest::class.java)

    @Test
    fun `parses sample packets`() {
        val parser = PacketParser(TagParser(logger), logger)

        for ((byteArray, expectedPacket) in SamplePackets.packetMap) {
            val parsedPacket = parser.parse(byteArray.inputStream())

            assertEquals(expectedPacket, parsedPacket)
        }
    }

    @Test
    fun `parses status response`() {
        val parser = PacketParser(TagParser(logger), logger)

        val packet = parser.parse(SamplePackets.statusResponse.inputStream())

        assertEquals(ECOpCode.EC_OP_STATS, packet.opCode)
        assertEquals(16, packet.tags.size)
        assertEquals(UShortTag(ECTagName.EC_TAG_STATS_UL_SPEED, 1664u), packet.tags[0])
    }

    @Test
    fun `rejects malformed compressed payloads instead of spinning forever`() {
        val parser = PacketParser(TagParser(logger), logger)

        assertFailsWith<InvalidECException> {
            parser.parse(SamplePackets.malformedCompressedPacket.inputStream())
        }
    }

}
