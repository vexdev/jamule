package jamule.response

import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class SearchStatusResponse(val status: Float) : Response {
    companion object {
        internal fun fromPacket(packet: Packet) = SearchStatusResponse(
            packet.numeric(ECTagName.EC_TAG_SEARCH_STATUS)!!.getNumber().let { num ->
                // Local searches always return 0xFFFF
                // Kad searches return 0xFFFE when they are finished, otherwise 0
                if (num == 0xFFFF || num == 0xFFFE) 1f else num / 100f
            }
        )
    }
}