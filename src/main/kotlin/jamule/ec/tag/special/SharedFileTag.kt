package jamule.ec.tag.special

import jamule.ec.ECTagName.*
import jamule.ec.packet.Packet.Companion.byte
import jamule.ec.packet.Packet.Companion.hash16
import jamule.ec.packet.Packet.Companion.numeric
import jamule.ec.packet.Packet.Companion.string
import jamule.ec.tag.Tag
import jamule.model.AmuleFile

internal data class SharedFileTag(
    override val fileHashHexString: String?, // EC_TAG_PARTFILE_HASH (Not sent on updates)

    override val fileName: String?, // EC_TAG_PARTFILE_NAME (Not sent on updates)
    override val filePath: String?, // EC_TAG_KNOWNFILE_FILENAME (Not sent on updates)
    override val sizeFull: Long?, // EC_TAG_PARTFILE_SIZE_FULL (Not sent on updates)
    override val fileEd2kLink: String?, // EC_TAG_PARTFILE_ED2K_LINK (Not sent on updates)

    override val upPrio: Byte, // EC_TAG_KNOWNFILE_PRIO
    override val getRequests: Short, // EC_TAG_KNOWNFILE_REQ_COUNT
    override val getAllRequests: Int, // EC_TAG_KNOWNFILE_REQ_COUNT_ALL

    override val getAccepts: Short, // EC_TAG_KNOWNFILE_ACCEPT_COUNT
    override val getAllAccepts: Int, // EC_TAG_KNOWNFILE_ACCEPT_COUNT_ALL

    override val getXferred: Long, // EC_TAG_KNOWNFILE_XFERRED
    override val getAllXferred: Long, // EC_TAG_KNOWNFILE_XFERRED_ALL

    override val getCompleteSourcesLow: Short, // EC_TAG_KNOWNFILE_COMPLETE_SOURCES_LOW
    override val getCompleteSourcesHigh: Short, // EC_TAG_KNOWNFILE_COMPLETE_SOURCES_HIGH
    override val getCompleteSources: Short, // EC_TAG_KNOWNFILE_COMPLETE_SOURCES

    override val getOnQueue: Short, // EC_TAG_KNOWNFILE_ON_QUEUE

    override val getComment: String?, // EC_TAG_KNOWNFILE_COMMENT (Not sent on updates)
    override val getRating: Byte?, // EC_TAG_KNOWNFILE_RATING (Not sent on updates)
) : AmuleFile {
    companion object {
        @OptIn(ExperimentalUnsignedTypes::class, ExperimentalStdlibApi::class)
        fun fromSubtags(subtags: List<Tag<*>>) = SharedFileTag(
            fileHashHexString = subtags.hash16(EC_TAG_PARTFILE_HASH)?.getValue()?.toByteArray()?.toHexString(),
            fileName = subtags.string(EC_TAG_PARTFILE_NAME)?.getValue(),

            filePath = subtags.string(EC_TAG_KNOWNFILE_FILENAME)?.getValue(),
            sizeFull = subtags.numeric(EC_TAG_PARTFILE_SIZE_FULL)?.getLong(),
            fileEd2kLink = subtags.string(EC_TAG_PARTFILE_ED2K_LINK)?.getValue(),

            upPrio = subtags.byte(EC_TAG_KNOWNFILE_PRIO)!!.getValue().toByte(),
            getRequests = subtags.numeric(EC_TAG_KNOWNFILE_REQ_COUNT)!!.getShort(),
            getAllRequests = subtags.numeric(EC_TAG_KNOWNFILE_REQ_COUNT_ALL)!!.getInt(),

            getAccepts = subtags.numeric(EC_TAG_KNOWNFILE_ACCEPT_COUNT)!!.getShort(),
            getAllAccepts = subtags.numeric(EC_TAG_KNOWNFILE_ACCEPT_COUNT_ALL)!!.getInt(),

            getXferred = subtags.numeric(EC_TAG_KNOWNFILE_XFERRED)!!.getLong(),
            getAllXferred = subtags.numeric(EC_TAG_KNOWNFILE_XFERRED_ALL)!!.getLong(),

            getCompleteSourcesLow = subtags.numeric(EC_TAG_KNOWNFILE_COMPLETE_SOURCES_LOW)!!.getShort(),
            getCompleteSourcesHigh = subtags.numeric(EC_TAG_KNOWNFILE_COMPLETE_SOURCES_HIGH)!!.getShort(),
            getCompleteSources = subtags.numeric(EC_TAG_KNOWNFILE_COMPLETE_SOURCES)!!.getShort(),

            getOnQueue = subtags.numeric(EC_TAG_KNOWNFILE_ON_QUEUE)!!.getShort(),

            getComment = subtags.string(EC_TAG_KNOWNFILE_COMMENT)?.getValue(),
            getRating = subtags.byte(EC_TAG_KNOWNFILE_RATING)?.getValue()?.toByte(),
        )
    }
}