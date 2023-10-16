package jamule.response

import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class ErrorResponse(val message: String) : Response {
    companion object {
        internal fun fromPacket(packet: Packet) = ErrorResponse(packet.string(ECTagName.EC_TAG_STRING)!!.getValue())
    }
}