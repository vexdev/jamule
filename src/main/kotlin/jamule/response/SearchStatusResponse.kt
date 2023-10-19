package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class SearchStatusResponse(val status: Float) : Response {
    internal object SearchStatusResponseDeserializer : ResponseDeserializer() {
        override fun canDeserialize(packet: Packet) =
            packet.opCode == ECOpCode.EC_OP_SEARCH_PROGRESS

        override fun deserialize(packet: Packet) =
            SearchStatusResponse(packet.numeric(ECTagName.EC_TAG_SEARCH_STATUS)!!.getInt().let { num ->
                // Local searches always return 0xFFFF
                // Kad searches return 0xFFFE when they are finished, otherwise 0
                if (num == 0xFFFF || num == 0xFFFE) 1f else num / 100f
            })
    }
}