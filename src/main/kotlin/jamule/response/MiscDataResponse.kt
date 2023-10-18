package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.packet.Packet

class MiscDataResponse : Response {
    companion object {
        @ResponseDeserializer(ECOpCode.EC_OP_MISC_DATA)
        @JvmStatic
        internal fun fromPacket(packet: Packet) = MiscDataResponse()
    }
}