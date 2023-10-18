package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet
import jamule.ec.tag.special.PartFileTag

internal data class DownloadQueueResponse(val partFiles: List<PartFileTag>): Response {
    companion object {
        @ResponseDeserializer(ECOpCode.EC_OP_DLOAD_QUEUE)
        @JvmStatic
        internal fun fromPacket(packet: Packet) =
            DownloadQueueResponse(
                partFiles = packet.tags.filter { it.name == ECTagName.EC_TAG_PARTFILE }
                    .map { partfile -> PartFileTag.fromSubtags(partfile.subtags) }
            )
    }
}