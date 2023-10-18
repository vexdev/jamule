package jamule.request

import jamule.ec.ECOpCode
import jamule.ec.packet.Packet

internal class SearchStopRequest : Request {
    override fun packet() = Packet(
        ECOpCode.EC_OP_SEARCH_STOP,
        listOf()
    )
}