package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class AuthOkResponse(val version: String) : Response {
    internal object AuthOkResponseDeserializer : ResponseDeserializer() {
        override fun canDeserialize(packet: Packet) =
            packet.opCode == ECOpCode.EC_OP_AUTH_OK

        override fun deserialize(packet: Packet) =
            AuthOkResponse(packet.string(ECTagName.EC_TAG_SERVER_VERSION)!!.getValue())

    }
}