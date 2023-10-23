package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class StringsResponse(val string: String) : Response {
    internal object StringsResponseDeserializer : ResponseDeserializer() {
        override fun canDeserialize(packet: Packet) =
            packet.opCode == ECOpCode.EC_OP_STRINGS

        override fun deserialize(packet: Packet) =
            StringsResponse(packet.string(ECTagName.EC_TAG_STRING)?.getValue() ?: "")

    }
}