package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.packet.Packet

class NoopResponse : Response {
    companion object {
        @ResponseDeserializer(ECOpCode.EC_OP_NOOP)
        @JvmStatic
        internal fun fromPacket(packet: Packet) = NoopResponse()
    }
}