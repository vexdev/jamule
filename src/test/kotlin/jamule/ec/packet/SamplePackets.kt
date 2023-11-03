@file:OptIn(ExperimentalUnsignedTypes::class)

package jamule.ec.packet

import jamule.ec.ECDetailLevel
import jamule.ec.ECOpCode
import jamule.ec.ECSearchType
import jamule.ec.ECTagName.*
import jamule.ec.tag.*

@OptIn(ExperimentalStdlibApi::class)
internal class SamplePackets {

    companion object {

        val packetMap: Map<ByteArray, Packet> = mapOf(
            // Auth Request
            ("00000022000000240205c8800609614d756c65636d6400c8820606322e33" +
                    "2e330004030202041801001a0100").hexToByteArray() to Packet(
                ECOpCode.EC_OP_AUTH_REQ,
                listOf(
                    StringTag(EC_TAG_CLIENT_NAME, "aMulecmd"),
                    StringTag(EC_TAG_CLIENT_VERSION, "2.3.3"),
                    UShortTag(EC_TAG_PROTOCOL_VERSION, 0x0204u),
                    CustomTag(EC_TAG_CAN_ZLIB, UByteArray(0)),
                    CustomTag(EC_TAG_CAN_UTF8_NUMBERS, UByteArray(0)),
                )
            ),
            // Auth Response
            "000000220000000d4f0116050855099a4aea510c43".hexToByteArray() to Packet(
                ECOpCode.EC_OP_AUTH_SALT,
                listOf(
                    ULongTag(EC_TAG_PASSWD_SALT, "55099a4aea510c43".hexToULong()),
                )
            ),
            // Auth Passwd Request
            "00000022000000155001020910ca9026415e1a7df7ec0f7ec69678c150".hexToByteArray() to Packet(
                ECOpCode.EC_OP_AUTH_PASSWD,
                listOf(
                    Hash16Tag(EC_TAG_PASSWD_HASH, "ca9026415e1a7df7ec0f7ec69678c150".hexToUByteArray()),
                )
            ),
            // Auth Ok Response
            ("000000220000001d0401e0a8960616322e332e31204164756e616e7a4120" +
                    "323031322e3100").hexToByteArray() to Packet(
                ECOpCode.EC_OP_AUTH_OK,
                listOf(
                    StringTag(EC_TAG_SERVER_VERSION, "2.3.1 AdunanzA 2012.1"),
                )
            ),
            // Status Request
            "00000022000000060a0108020100".hexToByteArray() to Packet(
                ECOpCode.EC_OP_STAT_REQ,
                listOf(
                    UByteTag(EC_TAG_DETAIL_LEVEL, ECDetailLevel.EC_DETAIL_CMD.value),
                )
            ),
            // Auth Failed Response
            ("000000220000002c0301000627417574" +
                    "68656e7469636174696f6e206661696c" +
                    "65643a2077726f6e672070617373776f" +
                    "72642e00"
                    ).hexToByteArray() to Packet(
                ECOpCode.EC_OP_AUTH_FAIL,
                listOf(
                    StringTag(EC_TAG_STRING, "Authentication failed: wrong password."),
                )
            ),
            // Simple search request
            ("00000020000000192600010e03020000000d00010e040600000005746573740001").hexToByteArray()
                    to Packet(
                ECOpCode.EC_OP_SEARCH_START,
                listOf(
                    UByteTag(
                        EC_TAG_SEARCH_TYPE, ECSearchType.EC_SEARCH_GLOBAL.value,
                        listOf(
                            StringTag(EC_TAG_SEARCH_NAME, "test")
                        )
                    ),
                ),
                Flags(utf8 = false)
            ),
            // Failed response
            ("000000220000003805010006336544326b2073" +
                    "65617263682063616e2774206265206" +
                    "46f6e65206966206544326b20697320" +
                    "6e6f7420636f6e6e656374656400").hexToByteArray()
                    to Packet(
                ECOpCode.EC_OP_FAILED,
                listOf(
                    StringTag(EC_TAG_STRING, "eD2k search can't be done if eD2k is not connected"),
                )
            ),
        )

        val statusResponse = ("000000220000008c0c10d08003021664d082020100d484020100d4860302" +
                "1664d488020100d48a020100d084020100d086020100d09002010" +
                "0d08c020100d092040400017cbbd09402010ad096040402e2740f" +
                "d09803020438d0b60201000b023f03e0a881081f01e0a88206124" +
                "16b74656f6e20536572766572204e6f3200b07de76247b50c0404" +
                "1d4e48541404041d4e485419")
            .hexToByteArray()

    }
}