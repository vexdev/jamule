package jamule.request

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet
import jamule.ec.tag.StringTag
import jamule.ec.tag.UByteTag
import jamule.ec.tag.UIntTag
import jamule.model.AmuleCategory

internal data class CreateCategoryRequest(val amuleCategory: AmuleCategory) : Request {

    override fun packet(): Packet = Packet(
        ECOpCode.EC_OP_CREATE_CATEGORY,
        listOf(
            UIntTag(
                ECTagName.EC_TAG_CATEGORY, amuleCategory.id.toUInt(), listOf(
                    StringTag(ECTagName.EC_TAG_CATEGORY_TITLE, amuleCategory.name),
                    StringTag(ECTagName.EC_TAG_CATEGORY_PATH, amuleCategory.path),
                    StringTag(ECTagName.EC_TAG_CATEGORY_COMMENT, amuleCategory.comment),
                    UByteTag(ECTagName.EC_TAG_CATEGORY_COLOR, amuleCategory.color.toUByte()),
                    UIntTag(ECTagName.EC_TAG_CATEGORY_PRIO, amuleCategory.priority.toUInt()),
                )
            ),
        )
    )

}