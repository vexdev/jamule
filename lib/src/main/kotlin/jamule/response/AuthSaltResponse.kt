package jamule.response

import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class AuthSaltResponse(val salt: ULong) : Response {
    companion object {
        internal fun fromPacket(packet: Packet) =
            AuthSaltResponse(packet.long(ECTagName.EC_TAG_PASSWD_SALT)!!.getValue())
    }
}