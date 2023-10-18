package jamule.request

import jamule.ec.ECDetailLevel
import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet
import jamule.ec.tag.Tag
import jamule.ec.tag.UByteTag

internal class DownloadQueueRequest : Request {

    override fun packet(): Packet = Packet(
        ECOpCode.EC_OP_GET_DLOAD_QUEUE,
        listOf(
            UByteTag(ECTagName.EC_TAG_DETAIL_LEVEL, ECDetailLevel.EC_DETAIL_FULL.value)
        )
    )

}