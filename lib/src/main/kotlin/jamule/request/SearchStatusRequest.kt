package jamule.request

import jamule.ec.ECOpCode
import jamule.ec.packet.Packet
import jamule.ec.tag.Tag

internal class SearchStatusRequest : Request {

    override fun packet(): Packet = Packet(
        ECOpCode.EC_OP_SEARCH_PROGRESS,
        listOf<Tag<out Any>>()
    )

}