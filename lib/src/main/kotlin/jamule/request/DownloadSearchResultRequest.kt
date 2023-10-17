package jamule.request

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet
import jamule.ec.tag.Hash16Tag
import jamule.ec.tag.Tag

internal data class DownloadSearchResultRequest(val hash: ByteArray) : Request {

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun packet(): Packet = Packet(
        ECOpCode.EC_OP_DOWNLOAD_SEARCH_RESULT,
        listOf<Tag<out Any>>(
            Hash16Tag(ECTagName.EC_TAG_PARTFILE, hash.toUByteArray())
        )
    )

}