package jamule.response

import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class StringsResponse(val string: String) : Response {
    companion object {
        internal fun fromPacket(packet: Packet) = StringsResponse(packet.string(ECTagName.EC_TAG_STRING)!!.getValue())
    }
}