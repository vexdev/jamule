package jamule.ec.tag

import jamule.ec.toByteArray
import org.slf4j.Logger

class TagEncoder(
    private val logger: Logger
) {

    fun encode(tag: Tag<out Any>, utf8: Boolean): ByteArray {
        val tagNameAndSubtags: UShort =
            ((tag.name.value.toUInt() shl 1) or if (tag.subtags.isEmpty()) 0u else 1u).toUShort()
        val tagNameAndSubtagsBytes = tagNameAndSubtags.toByteArray(utf8)
        val tagLengthBytes = computeTagLength(tag).toByteArray(utf8)
        val subtagPayload = computeSubTagPayload(tag, utf8)

        return tagNameAndSubtagsBytes +
                tag.type.value.toByte() +
                tagLengthBytes +
                subtagPayload +
                tag.encodeValue()
    }

    private fun computeTagLength(tag: Tag<out Any>): UInt {
        // TODO Merge with the computeSubTagPayload() method to avoid double recursion
        val currentTagLength = tag.encodeValue().size.toUInt()
        var subtagsLength = 0u
        for (subtag in tag.subtags) {
            subtagsLength += computeTagLength(subtag)
            subtagsLength += TagParser.TAG_NAME_SIZE.toUInt()
            subtagsLength += TagParser.TAG_TYPE_SIZE.toUInt()
            subtagsLength += TagParser.TAG_LENGTH_SIZE.toUInt()
            if (subtag.subtags.isNotEmpty())
                subtagsLength += TagParser.SUBTAG_COUNT_SIZE.toUInt()
        }
        return currentTagLength + subtagsLength
    }

    private fun computeSubTagPayload(tag: Tag<out Any>, utf8: Boolean): ByteArray {
        val subtagsPayload = mutableListOf<Byte>()
        for (subtag in tag.subtags) {
            encode(subtag, utf8).forEach { subtagsPayload.add(it) }
        }
        return subtagsPayload.toByteArray()
    }

}