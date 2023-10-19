package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.packet.Packet

data object MiscDataResponse : Response {
    internal object MiscDataResponseDeserializer : ResponseDeserializer() {
        override fun canDeserialize(packet: Packet) =
            packet.opCode == ECOpCode.EC_OP_MISC_DATA

        override fun deserialize(packet: Packet) =
            MiscDataResponse
    }
}