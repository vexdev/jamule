package jamule.request

import jamule.ec.ECTagName.EC_TAG_PARTFILE
import jamule.ec.ECTagName.EC_TAG_PARTFILE_CAT
import jamule.ec.packet.Packet
import jamule.ec.tag.Hash16Tag
import jamule.ec.tag.ULongTag

internal data class SetFileCategoryRequest(
    val fileHash: ByteArray,
    val category: Long,
) : Request {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun packet(): Packet =
        Packet(
            jamule.ec.ECOpCode.EC_OP_PARTFILE_SET_CAT,
            listOf(
                Hash16Tag(
                    EC_TAG_PARTFILE, fileHash.toUByteArray(), listOf(
                        ULongTag(EC_TAG_PARTFILE_CAT, category.toULong())
                    )
                ),
            )
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SetFileCategoryRequest

        if (!fileHash.contentEquals(other.fileHash)) return false
        if (category != other.category) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fileHash.contentHashCode()
        result = 31 * result + category.hashCode()
        return result
    }
}