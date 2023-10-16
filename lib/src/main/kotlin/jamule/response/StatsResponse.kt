package jamule.response

import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class StatsResponse(
    val uploadSpeed: Int,
    val downloadSpeed: Int,
    val uploadSpeedLimit: Int,
    val downloadSpeedLimit: Int,
    val uploadOverhead: Int,
    val downloadOverhead: Int,
    val totalSourceCount: Int,
    val bannedCount: Int,
    val uploadQueueLength: Int,
    val ed2kUsers: Int,
    val kadUsers: Int,
    val ed2kFiles: Int,
    val kadFiles: Int,
    val loggerMessage: String,
    val kadFirewalledUdp: Int,
    val kadIndexedSources: Int,
    val kadIndexedKeywords: Int,
    val kadIndexedNotes: Int,
    val kadIndexedLoad: Int,
    val totalSentBytes: Int,
    val totalReceivedBytes: Int,
    val sharedFileCount: Int,
    val kadNodes: Int,
    val connState: UByte?,

    ) : Response {
    companion object {
        internal fun fromPacket(packet: Packet) = StatsResponse(
            packet.numeric(ECTagName.EC_TAG_STATS_UL_SPEED)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_DL_SPEED)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_UL_SPEED_LIMIT)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_DL_SPEED_LIMIT)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_UP_OVERHEAD)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_DOWN_OVERHEAD)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_TOTAL_SRC_COUNT)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_BANNED_COUNT)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_UL_QUEUE_LEN)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_ED2K_USERS)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_KAD_USERS)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_ED2K_FILES)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_KAD_FILES)?.getNumber() ?: 0,
            packet.string(ECTagName.EC_TAG_STATS_LOGGER_MESSAGE)?.getValue() ?: "",
            packet.numeric(ECTagName.EC_TAG_STATS_KAD_FIREWALLED_UDP)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_KAD_INDEXED_SOURCES)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_KAD_INDEXED_KEYWORDS)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_KAD_INDEXED_NOTES)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_KAD_INDEXED_LOAD)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_TOTAL_SENT_BYTES)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_TOTAL_RECEIVED_BYTES)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_SHARED_FILE_COUNT)?.getNumber() ?: 0,
            packet.numeric(ECTagName.EC_TAG_STATS_KAD_NODES)?.getNumber() ?: 0,
            packet.byte(ECTagName.EC_TAG_CONNSTATE)?.getValue(),
        )
    }
}