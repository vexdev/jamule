package jamule.response

import jamule.ec.ECTagName
import jamule.ec.packet.Packet

sealed interface Response

data class AuthSaltResponse(val salt: ULong) : Response {
    companion object {
        fun fromPacket(packet: Packet) = AuthSaltResponse(packet.long(ECTagName.EC_TAG_PASSWD_SALT)!!.getValue())
    }
}

data class AuthOkResponse(val version: String) : Response {
    companion object {
        fun fromPacket(packet: Packet) = AuthOkResponse(packet.string(ECTagName.EC_TAG_SERVER_VERSION)!!.getValue())
    }
}

data class AuthFailedResponse(val reason: String) : Response {
    companion object {
        fun fromPacket(packet: Packet) = AuthFailedResponse(packet.string(ECTagName.EC_TAG_STRING)!!.getValue())
    }
}

data class StatsResponse(
    val uploadSpeed: Long,
    val downloadSpeed: Long,
    val uploadSpeedLimit: Long,
    val downloadSpeedLimit: Long,
    val uploadOverhead: Long,
    val downloadOverhead: Long,
    val totalSourceCount: Long,
    val bannedCount: Long,
    val uploadQueueLength: Long,
    val ed2kUsers: Long,
    val kadUsers: Long,
    val ed2kFiles: Long,
    val kadFiles: Long,
    val loggerMessage: String,
    val kadFirewalledUdp: Long,
    val kadIndexedSources: Long,
    val kadIndexedKeywords: Long,
    val kadIndexedNotes: Long,
    val kadIndexedLoad: Long,
    val totalSentBytes: Long,
    val totalReceivedBytes: Long,
    val sharedFileCount: Long,
    val kadNodes: Long,
    val connState: UByte?,

    ) : Response {
    companion object {
        fun fromPacket(packet: Packet) = StatsResponse(
            packet.asLong(ECTagName.EC_TAG_STATS_UL_SPEED) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_DL_SPEED) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_UL_SPEED_LIMIT) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_DL_SPEED_LIMIT) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_UP_OVERHEAD) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_DOWN_OVERHEAD) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_TOTAL_SRC_COUNT) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_BANNED_COUNT) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_UL_QUEUE_LEN) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_ED2K_USERS) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_KAD_USERS) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_ED2K_FILES) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_KAD_FILES) ?: 0,
            packet.string(ECTagName.EC_TAG_STATS_LOGGER_MESSAGE)?.getValue() ?: "",
            packet.asLong(ECTagName.EC_TAG_STATS_KAD_FIREWALLED_UDP) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_KAD_INDEXED_SOURCES) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_KAD_INDEXED_KEYWORDS) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_KAD_INDEXED_NOTES) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_KAD_INDEXED_LOAD) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_TOTAL_SENT_BYTES) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_TOTAL_RECEIVED_BYTES) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_SHARED_FILE_COUNT) ?: 0,
            packet.asLong(ECTagName.EC_TAG_STATS_KAD_NODES) ?: 0,
            packet.byte(ECTagName.EC_TAG_CONNSTATE)?.getValue(),
        )
    }
}