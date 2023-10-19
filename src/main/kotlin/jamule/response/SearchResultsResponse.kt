package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECSearchFileDownloadStatus
import jamule.ec.ECTagName
import jamule.ec.packet.Packet
import jamule.ec.packet.Packet.Companion.byte
import jamule.ec.packet.Packet.Companion.hash16
import jamule.ec.packet.Packet.Companion.numeric
import jamule.ec.packet.Packet.Companion.string
import jamule.exception.CommunicationException

data class SearchResultsResponse(val files: List<SearchFile>) : Response {
    internal object SearchResultsResponseDeserializer : ResponseDeserializer() {
        override fun canDeserialize(packet: Packet) =
            packet.opCode == ECOpCode.EC_OP_SEARCH_RESULTS

        override fun deserialize(packet: Packet) =
            SearchResultsResponse(
                packet.tags.map { tag ->
                    if (tag.name == ECTagName.EC_TAG_SEARCHFILE) {
                        SearchFile(
                            fileName = tag.subtags.string(ECTagName.EC_TAG_PARTFILE_NAME)!!.getValue(),
                            hash = tag.subtags.hash16(ECTagName.EC_TAG_PARTFILE_HASH)!!.getValue().toByteArray(),
                            sizeFull = tag.subtags.numeric(ECTagName.EC_TAG_PARTFILE_SIZE_FULL)?.getLong() ?: 0,
                            downloadStatus = SearchFileDownloadStatus.fromECStatus(
                                tag.subtags.byte(ECTagName.EC_TAG_PARTFILE_STATUS)!!.getValue()
                                    .let { ECSearchFileDownloadStatus.fromValue(it) }
                            ),
                            completeSourceCount = tag.subtags.numeric(ECTagName.EC_TAG_PARTFILE_SOURCE_COUNT_XFER)
                                ?.getInt() ?: 0,
                            sourceCount = tag.subtags.numeric(ECTagName.EC_TAG_PARTFILE_SOURCE_COUNT)?.getInt() ?: 0,
                        )
                    } else {
                        throw CommunicationException("Unexpected tag ${tag.name} in SearchResultsResponse")
                    }
                }
            )

    }

    /**
     * Represents a search-result returned from a server or client.
     *
     * A file may have either a parent or any number of children.
     * When a child is added to a result, the parent becomes a generic
     * representation of all its children, which will include a copy
     * of the original result. The parent object will contain the sum
     * of sources (total/complete) and will have the most common
     * filename.
     */
    data class SearchFile(
        val fileName: String,
        val hash: ByteArray,
        val sizeFull: Long,
        // Possible download status of a file
        val downloadStatus: SearchFileDownloadStatus,
        // Returns the number of sources that have the entire file.
        val completeSourceCount: Int,
        // Returns the total number of sources.
        val sourceCount: Int,
    )

    enum class SearchFileDownloadStatus(internal val ecStatus: ECSearchFileDownloadStatus) {
        NEW(ECSearchFileDownloadStatus.NEW), // not known
        DOWNLOADED(ECSearchFileDownloadStatus.DOWNLOADED), // successfully downloaded or shared
        QUEUED(ECSearchFileDownloadStatus.QUEUED), // downloading (Partfile)
        CANCELED(ECSearchFileDownloadStatus.CANCELED), // canceled
        QUEUEDCANCELED(ECSearchFileDownloadStatus.QUEUEDCANCELED); // canceled once, but now downloading again

        companion object {
            internal fun fromECStatus(ecStatus: ECSearchFileDownloadStatus) = entries.first { it.ecStatus == ecStatus }
        }
    }
}