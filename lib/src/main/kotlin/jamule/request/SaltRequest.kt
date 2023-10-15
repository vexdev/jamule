package jamule.request

import jamule.Build
import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.ProtocolVersion
import jamule.ec.packet.Flags
import jamule.ec.packet.Packet
import jamule.ec.tag.CustomTag
import jamule.ec.tag.StringTag
import jamule.ec.tag.UShortTag

class SaltRequest : Request {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun packet(): Packet = Packet(
        ECOpCode.EC_OP_AUTH_REQ,
        listOf(
            StringTag(ECTagName.EC_TAG_CLIENT_NAME, AmuleClient.CLIENT_NAME),
            StringTag(ECTagName.EC_TAG_CLIENT_VERSION, Build.version),
            UShortTag(ECTagName.EC_TAG_PROTOCOL_VERSION, ProtocolVersion.EC_CURRENT_PROTOCOL_VERSION.value),
            CustomTag(ECTagName.EC_TAG_CAN_ZLIB, UByteArray(0)),
            CustomTag(ECTagName.EC_TAG_CAN_UTF8_NUMBERS, UByteArray(0)),
        ),
        Flags()
    )
}