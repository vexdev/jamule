package jamule.request

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Flags
import jamule.ec.packet.Packet
import jamule.ec.tag.Hash16Tag

@ExperimentalUnsignedTypes
data class AuthRequest(val hashedPassword: UByteArray) : Request {
    override fun packet(): Packet = Packet(
        ECOpCode.EC_OP_AUTH_PASSWD,
        listOf(
            Hash16Tag(ECTagName.EC_TAG_PASSWD_HASH, hashedPassword),
        ),
        Flags()
    )
}