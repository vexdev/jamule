package jamule.ec.tag

import jamule.ec.*
import jamule.exception.InvalidECException
import org.slf4j.Logger

@ExperimentalUnsignedTypes
class TagParser(
    private val logger: Logger
) {

    /**
     * Parses a tag from the given [payload], starting at the given [index]. If [utf] is true, headers are parsed as
     * UTF-8 numbers, otherwise they are parsed as binary numbers.
     * Returns a pair of the parsed tag and the index of the last byte of the tag.
     */
    fun parse(payload: UByteArray, index: Int, utf: Boolean): Pair<Tag<out Any>, Int> =
        parseWithMetadata(payload, index, utf)
            .let { (tag, meta) -> Pair(tag, meta.endIndex) }

    @OptIn(ExperimentalStdlibApi::class)
    private fun parseWithMetadata(payload: UByteArray, tagNameIndex: Int, utf: Boolean): Pair<Tag<out Any>, TagMeta> {
        // First part is the tag name and the flag indicating if it has subtags
        // As per docs, it's the last bit of the tag name
        val tagNameAndHasSubtags = payload.readUint16(utf, tagNameIndex)
        val tagNameRaw = (payload.readUint16(utf, tagNameIndex) shr 1).toUShort()
        val tagName = ECTagName.fromValue(tagNameRaw)
        val hasSubtags = tagNameAndHasSubtags and 0x01u == 0x01u
        logger.trace("Tag name: {}, Has subtags: {}", tagName, hasSubtags)

        // Then is the tag type
        val tagTypeIndex = tagNameIndex + payload[tagNameIndex].numberLength(utf, TAG_NAME_SIZE)
        val tagType = ECTagType.fromValue(payload[tagTypeIndex])
        logger.trace("Tag type: {}", tagType)

        // Then is the tag length, indicating the tag's own content length + length of children (with headers)
        val tagLengthIndex = tagTypeIndex + TAG_TYPE_SIZE
        val tagLength = payload.readUInt32(utf, tagLengthIndex).toInt()
        logger.trace("Tag length: {}", tagLength)

        // The next byte is the first byte of the tag's value
        // This is mutable because it will be updated if the tag has subtags
        var valueStartIndex = tagLengthIndex + payload[tagLengthIndex].numberLength(utf, TAG_LENGTH_SIZE)
        logger.trace("Tag type idx: {}, len idx: {}, subtag idx: {}", tagTypeIndex, tagLengthIndex, valueStartIndex)

        // If the tag has subtags, we need to parse them
        val subTags = mutableListOf<Tag<out Any>>()

        // Theoretical length is the length of the tag, including the length of the subtags (with headers)
        // All numbers are considered to be non-UTF-8
        var theoreticalLength = 0

        // The last byte of the tag's value
        val valueEndIndex: Int
        if (!hasSubtags) {
            valueEndIndex = valueStartIndex + tagLength - 1
        } else {
            // The first part to read is the subtags count
            val subTagCount = payload.readUint16(utf, valueStartIndex).toInt()
            logger.trace("Tag has {} subtags", subTagCount)

            // We reuse the valueStartIndex variable to store the index of the first subtag
            valueStartIndex += payload[valueStartIndex].numberLength(utf, SUBTAG_COUNT_SIZE)
            for (i in 0..<subTagCount) {
                logger.trace("Parsing subtag {} starting at {}", i, valueStartIndex)
                val (subTag: Tag<out Any>, tagMeta) = parseWithMetadata(payload, valueStartIndex, utf)
                subTags.add(subTag)
                valueStartIndex = tagMeta.endIndex + 1
                theoreticalLength += tagMeta.theoreticalLength
            }

            if (subTags.size > subTagCount) {
                throw InvalidECException("Error parsing subtags list - Expected subtags $subTagCount found subtags ${subTags.size}")
            }
            valueEndIndex = valueStartIndex + ((tagLength - theoreticalLength) - 1)
            theoreticalLength += SUBTAG_COUNT_SIZE
        }

        val tagValue = payload.sliceArray(valueStartIndex..valueEndIndex)
        logger.trace("Tag value: {}", tagValue.toHexString())
        theoreticalLength += tagValue.size
        theoreticalLength += TAG_NAME_SIZE
        theoreticalLength += TAG_TYPE_SIZE
        theoreticalLength += TAG_LENGTH_SIZE

        // We now map the tag type to the correct tag class, parsing the value in the process
        return Pair(when (tagType) {
            ECTagType.EC_TAGTYPE_CUSTOM -> CustomTag(tagName, subTags, tagNameRaw)
            ECTagType.EC_TAGTYPE_UINT8 -> UByteTag(tagName, subTags, tagNameRaw)
            ECTagType.EC_TAGTYPE_UINT16 -> UShortTag(tagName, subTags, tagNameRaw)
            ECTagType.EC_TAGTYPE_UINT32 -> UIntTag(tagName, subTags, tagNameRaw)
            ECTagType.EC_TAGTYPE_UINT64 -> ULongTag(tagName, subTags, tagNameRaw)
            ECTagType.EC_TAGTYPE_UINT128 -> UInt128Tag(tagName, subTags, tagNameRaw)
            ECTagType.EC_TAGTYPE_DOUBLE -> DoubleTag(tagName, subTags, tagNameRaw)
            ECTagType.EC_TAGTYPE_IPV4 -> Ipv4Tag(tagName, subTags, tagNameRaw)
            ECTagType.EC_TAGTYPE_HASH16 -> Hash16Tag(tagName, subTags, tagNameRaw)
            ECTagType.EC_TAGTYPE_STRING -> StringTag(tagName, subTags, tagNameRaw)
            else -> throw InvalidECException("Unknown tag type: $tagType")
        }.apply { parseValue(tagValue) }.also {
            logger.debug("Parsed tag: {}={} [{}]", it.name, it.getValue(), tagValue.toHexString())
        }, TagMeta(theoreticalLength, valueEndIndex)
        )

    }

    companion object {
        const val TAG_NAME_SIZE = LEN_USHORT
        const val TAG_TYPE_SIZE = LEN_UBYTE
        const val TAG_LENGTH_SIZE = LEN_UINT
        const val SUBTAG_COUNT_SIZE = LEN_USHORT
    }

    private data class TagMeta(
        /**
         * The length of the tag, including the length of the subtags (with headers). All numbers are considered to be
         * non-UTF-8.
         */
        val theoreticalLength: Int,
        val endIndex: Int,
    )

}