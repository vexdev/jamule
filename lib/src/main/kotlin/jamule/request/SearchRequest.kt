package jamule.request

import jamule.ec.ECOpCode
import jamule.ec.ECSearchType
import jamule.ec.ECTagName
import jamule.ec.packet.Flags
import jamule.ec.packet.Packet
import jamule.ec.tag.StringTag
import jamule.ec.tag.Tag
import jamule.ec.tag.UByteTag
import jamule.ec.tag.ULongTag

data class SearchRequest(
    val query: String,
    val type: SearchType,
    val filters: SearchFilters,
) : Request {

    override fun packet(): Packet = Packet(
        ECOpCode.EC_OP_SEARCH_START,
        listOf(
            UByteTag(
                ECTagName.EC_TAG_SEARCH_TYPE, type.ecSearchType.value, listOf(
                    StringTag(ECTagName.EC_TAG_SEARCH_NAME, query),
                )
            ),
        )
                + if (filters.filetype != null)
            listOf(StringTag(ECTagName.EC_TAG_SEARCH_FILE_TYPE, filters.filetype)) else emptyList<Tag<*>>()
                + if (filters.extension != null)
            listOf(StringTag(ECTagName.EC_TAG_SEARCH_EXTENSION, filters.extension)) else emptyList<Tag<*>>()
                + if (filters.minSize != null)
            listOf(ULongTag(ECTagName.EC_TAG_SEARCH_MIN_SIZE, filters.minSize)) else emptyList<Tag<*>>()
                + if (filters.maxSize != null)
            listOf(ULongTag(ECTagName.EC_TAG_SEARCH_MAX_SIZE, filters.maxSize)) else emptyList<Tag<*>>()
                + if (filters.availability != null)
            listOf(ULongTag(ECTagName.EC_TAG_SEARCH_AVAILABILITY, filters.availability)) else emptyList(),
    )

    data class SearchFilters(
        val filetype: String? = null,
        val extension: String? = null,
        val minSize: ULong? = null,
        val maxSize: ULong? = null,
        val availability: ULong? = null,
    )

    enum class SearchType(val ecSearchType: ECSearchType) {
        GLOBAL(ecSearchType = ECSearchType.EC_SEARCH_GLOBAL),
        KAD(ecSearchType = ECSearchType.EC_SEARCH_KAD),
        LOCAL(ecSearchType = ECSearchType.EC_SEARCH_LOCAL),
        WEB(ecSearchType = ECSearchType.EC_SEARCH_WEB),
    }

}