package jamule.response

import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class AuthOkResponse(val version: String) : Response {
    companion object {
        internal fun fromPacket(packet: Packet) =
            AuthOkResponse(packet.string(ECTagName.EC_TAG_SERVER_VERSION)!!.getValue())
    }
}