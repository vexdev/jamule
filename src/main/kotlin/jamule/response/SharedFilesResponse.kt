package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet
import jamule.ec.tag.special.SharedFileTag

internal data class SharedFilesResponse(val sharedFiles: List<SharedFileTag>) : Response {
    internal object SharedFilesResponseDeserializer : ResponseDeserializer() {
        override fun canDeserialize(packet: Packet) =
            packet.opCode == ECOpCode.EC_OP_SHARED_FILES

        override fun deserialize(packet: Packet) =
            SharedFilesResponse(
                sharedFiles = packet.tags.filter { it.name == ECTagName.EC_TAG_KNOWNFILE }
                    .map { partfile -> SharedFileTag.fromSubtags(partfile.subtags) }
            )

    }
}