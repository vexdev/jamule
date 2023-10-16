package jamule.response

import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class AuthFailedResponse(val reason: String) : Response {
    companion object {
        internal fun fromPacket(packet: Packet) =
            AuthFailedResponse(packet.string(ECTagName.EC_TAG_STRING)!!.getValue())
    }
}