package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class AuthFailedResponse(val reason: String) : Exception(reason), Response {
    internal object AuthFailedResponseDeserializer : ResponseDeserializer() {
        override fun canDeserialize(packet: Packet) =
            packet.opCode == ECOpCode.EC_OP_AUTH_FAIL

        override fun deserialize(packet: Packet) =
            AuthFailedResponse(packet.string(ECTagName.EC_TAG_STRING)!!.getValue())

    }
}