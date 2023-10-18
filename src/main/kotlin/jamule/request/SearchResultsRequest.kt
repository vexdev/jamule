package jamule.request

import jamule.ec.ECOpCode
import jamule.ec.packet.Packet
import jamule.ec.tag.Tag

internal class SearchResultsRequest : Request {

    override fun packet(): Packet = Packet(
        ECOpCode.EC_OP_SEARCH_RESULTS,
        listOf<Tag<out Any>>()
    )

}