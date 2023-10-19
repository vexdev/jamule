package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.packet.Packet

data object NoopResponse : Response {
    internal object NoopResponseDeserializer : ResponseDeserializer() {
        override fun canDeserialize(packet: Packet) =
            packet.opCode == ECOpCode.EC_OP_NOOP

        override fun deserialize(packet: Packet) =
            NoopResponse
    }
}