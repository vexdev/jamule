package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class AuthSaltResponse(val salt: ULong) : Response {
    companion object {
        @ResponseDeserializer(ECOpCode.EC_OP_AUTH_SALT)
        @JvmStatic
        internal fun fromPacket(packet: Packet) =
            AuthSaltResponse(packet.long(ECTagName.EC_TAG_PASSWD_SALT)!!.getValue())
    }
}