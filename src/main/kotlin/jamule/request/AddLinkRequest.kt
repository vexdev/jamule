package jamule.request

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet
import jamule.ec.tag.StringTag
import jamule.ec.tag.Tag

internal data class AddLinkRequest(val link: String) : Request {

    override fun packet(): Packet = Packet(
        ECOpCode.EC_OP_ADD_LINK,
        listOf<Tag<out Any>>(
            StringTag(ECTagName.EC_TAG_PARTFILE_ED2K_LINK, link),
        )
    )

}