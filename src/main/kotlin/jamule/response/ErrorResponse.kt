package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class ErrorResponse(val serverMessage: String) : Exception(serverMessage), Response {
    internal object ErrorResponseDeserializer : ResponseDeserializer() {
        override fun canDeserialize(packet: Packet) =
            packet.opCode == ECOpCode.EC_OP_FAILED

        override fun deserialize(packet: Packet) =
            ErrorResponse(packet.string(ECTagName.EC_TAG_STRING)?.getValue() ?: "Unknown error")

    }
}