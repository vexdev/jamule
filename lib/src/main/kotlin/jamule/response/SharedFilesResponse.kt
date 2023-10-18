package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet
import jamule.ec.tag.special.SharedFileTag

internal data class SharedFilesResponse(val sharedFiles: List<SharedFileTag>) : Response {
    companion object {
        @ResponseDeserializer(ECOpCode.EC_OP_SHARED_FILES)
        @JvmStatic
        internal fun fromPacket(packet: Packet) =
            SharedFilesResponse(
                sharedFiles = packet.tags.filter { it.name == ECTagName.EC_TAG_KNOWNFILE }
                    .map { partfile -> SharedFileTag.fromSubtags(partfile.subtags) }
            )
    }
}