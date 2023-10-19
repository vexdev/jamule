package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class AuthSaltResponse(val salt: ULong) : Response {
    internal object AuthSaltResponseDeserializer : ResponseDeserializer() {
        override fun canDeserialize(packet: Packet) =
            packet.opCode == ECOpCode.EC_OP_AUTH_SALT

        override fun deserialize(packet: Packet) =
            AuthSaltResponse(packet.long(ECTagName.EC_TAG_PASSWD_SALT)!!.getValue())

    }
}