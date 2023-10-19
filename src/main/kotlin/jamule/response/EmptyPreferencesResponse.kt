package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.packet.Packet

data object EmptyPreferencesResponse : Response {
    internal object EmptyPreferencesResponseDeserializer : ResponseDeserializer() {
        @OptIn(ExperimentalUnsignedTypes::class)
        override fun canDeserialize(packet: Packet) =
            packet.opCode == ECOpCode.EC_OP_SET_PREFERENCES && packet.tags.isEmpty()

        @OptIn(ExperimentalUnsignedTypes::class)
        override fun deserialize(packet: Packet) = EmptyPreferencesResponse
    }
}
