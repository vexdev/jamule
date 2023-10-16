package jamule.response

import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class SearchStatusResponse(val status: Float) : Response {
    companion object {
        internal fun fromPacket(packet: Packet) = SearchStatusResponse(
            packet.numeric(ECTagName.EC_TAG_SEARCH_STATUS)!!.getNumber().div(100f)
        )
    }
}