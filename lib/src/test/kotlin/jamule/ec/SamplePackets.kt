@file:OptIn(ExperimentalUnsignedTypes::class)

package jamule.ec

import jamule.ec.packet.Flags
import jamule.ec.packet.Packet
import jamule.ec.tag.CustomTag
import jamule.ec.tag.StringTag
import jamule.ec.tag.UByteTag
import jamule.ec.tag.UShortTag

@OptIn(ExperimentalStdlibApi::class)
class SamplePackets {

    companion object {

        val packetMap: Map<ByteArray, Packet> = mapOf(
            // Status Request
            "00000022000000060a0108020100".hexToByteArray() to Packet(
                ECOpCode.EC_OP_STAT_REQ,
                listOf(
                    UByteTag(ECTagName.EC_TAG_DETAIL_LEVEL, ECDetailLevel.EC_DETAIL_CMD.value),
                ),
                Flags(zlibCompressed = false, utf8 = true, hasId = false, accepts = false)
            ),
            // Auth Request
            ("00000022000000240205c8800609614d756c65636d6400c8820606322e33" +
                    "2e330004030202041801001a0100").hexToByteArray() to Packet(
                ECOpCode.EC_OP_AUTH_REQ,
                listOf(
                    StringTag(ECTagName.EC_TAG_CLIENT_NAME, "aMulecmd"),
                    StringTag(ECTagName.EC_TAG_CLIENT_VERSION, "2.3.3"),
                    UShortTag(ECTagName.EC_TAG_PROTOCOL_VERSION, 0x0204u),
                    CustomTag(ECTagName.EC_TAG_CAN_ZLIB, UByteArray(0)),
                    CustomTag(ECTagName.EC_TAG_CAN_UTF8_NUMBERS, UByteArray(0)),
                ),
                Flags(zlibCompressed = false, utf8 = true, hasId = false, accepts = false)
            )
        )

        val statusResponse =
            "000000220000008c0c10d08003021664d082020100d484020100d48603021664d488020100d48a020100d084020100d086020100d090020100d08c020100d092040400017cbbd09402010ad096040402e2740fd09803020438d0b60201000b023f03e0a881081f01e0a8820612416b74656f6e20536572766572204e6f3200b07de76247b50c04041d4e48541404041d4e485419".hexToByteArray()
        val authResponse = "000000220000000d4f0116050855099a4aea510c43".hexToByteArray()

        val authPasswdRequest = "00000022000000155001020910ca9026415e1a7df7ec0f7ec69678c150".hexToByteArray()
        val authOkResponse =
            "000000220000001d0401e0a8960616322e332e31204164756e616e7a4120323031322e3100".hexToByteArray()

    }
}