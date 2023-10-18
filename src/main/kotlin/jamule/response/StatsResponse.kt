package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECTagName.*
import jamule.ec.packet.Packet
import jamule.ec.packet.Packet.Companion.byte
import jamule.ec.packet.Packet.Companion.ipv4
import jamule.ec.packet.Packet.Companion.numeric
import jamule.ec.packet.Packet.Companion.string
import jamule.ec.tag.Ipv4Tag
import jamule.ec.tag.StringTag
import jamule.ec.tag.Tag
import java.net.InetAddress
import java.nio.ByteBuffer

data class StatsResponse(

    /**
     * The connection state of both ED2k and Kad's networks, with details.
     */
    val connectionState: ConnectionState?,

    /**
     * The overhead is the amount of data that is sent in addition to the actual data.
     * This metric returns it as bytes per second.
     */
    val uploadOverhead: Int,

    /**
     * The overhead is the amount of data that is sent in addition to the actual data.
     * This metric returns it as bytes per second.
     */
    val downloadOverhead: Int,

    /**
     * The number of banned clients.
     */
    val bannedCount: Int,

    /**
     * Up to 200 lines of log messages.
     */
    val loggerMessage: List<String>,

    /**
     * The total number of bytes sent.
     */
    val totalSentBytes: Int,

    /**
     * The total number of bytes received.
     */
    val totalReceivedBytes: Int,

    /**
     * The total number of shared files.
     */
    val sharedFileCount: Int,

    /**
     * The upload speed in bytes per second.
     */
    val uploadSpeed: Int,

    /**
     * The download speed in bytes per second.
     */
    val downloadSpeed: Int,

    /**
     * The upload speed limit in bytes per second.
     */
    val uploadSpeedLimit: Int,

    /**
     * The download speed limit in bytes per second.
     */
    val downloadSpeedLimit: Int,

    /**
     * The number of users in the upload queue.
     */
    val uploadQueueLength: Int,

    /**
     * The total number of sources found.
     */
    val totalSourceCount: Int,

    /**
     * The number of users connected to the eD2K network.
     */
    val ed2kUsers: Int,

    /**
     * The number of users connected to the Kad network.
     */
    val kadUsers: Int,

    /**
     * The number of files shared on the eD2K network.
     */
    val ed2kFiles: Int,

    /**
     * The number of files shared on the Kad network.
     */
    val kadFiles: Int,

    /**
     * The number of nodes in the Kad network.
     */
    val kadNodes: Int,

    //// Following metrics are only available if the client is connected to the Kad network.

    /**
     * True if the client's UDP port is firewalled in the Kad network.
     * Only available if the client is connected to the Kad network.
     */
    val kadFirewalledUdp: Boolean?,

    /**
     * The number of sources indexed on the Kad network.
     * Only available if the client is connected to the Kad network.
     */
    val kadIndexedSources: Int?,

    /**
     * The number of keywords indexed on the Kad network.
     * Only available if the client is connected to the Kad network.
     */
    val kadIndexedKeywords: Int?,

    /**
     * The number of notes indexed on the Kad network.
     * Only available if the client is connected to the Kad network.
     */
    val kadIndexedNotes: Int?,

    /**
     * The total indexed load of the Kad network.
     * Only available if the client is connected to the Kad network.
     */
    val kadIndexedLoad: Int?,

    /**
     * The client's IP address in the Kad network.
     * Only available if the client is connected to the Kad network.
     */
    val kadIpAddress: String?,

    /**
     * True if the client is running in LAN mode.
     * Only available if the client is connected to the Kad network.
     */
    val kadIsRunningInLanMode: Boolean?,

    /**
     * In the context of the Kad network, a "buddy" is a special kind of peer
     * that acts as a relay for your client when you're behind a firewall or
     * NAT (Network Address Translation) and cannot accept incoming connections
     * directly.
     * Only available if the client is connected to the Kad network.
     */
    val buddyStatus: BuddyState?,

    /**
     * The buddy's IP address.
     * Only available if the client is connected to the Kad network.
     */
    val buddyIp: String?,

    /**
     * The buddy's UDP port.
     * Only available if the client is connected to the Kad network.
     */
    val buddyPort: UShort?,

    ) : Response {
    companion object {
        @ResponseDeserializer(ECOpCode.EC_OP_STATS)
        @JvmStatic
        internal fun fromPacket(packet: Packet) = StatsResponse(
            connectionState = packet.tags.firstOrNull { it.name == EC_TAG_CONNSTATE }
                ?.let { ConnectionState.fromConStateTag(it) },
            uploadOverhead = packet.numeric(EC_TAG_STATS_UP_OVERHEAD)?.getInt() ?: 0,
            downloadOverhead = packet.numeric(EC_TAG_STATS_DOWN_OVERHEAD)?.getInt() ?: 0,
            bannedCount = packet.numeric(EC_TAG_STATS_BANNED_COUNT)?.getInt() ?: 0,
            loggerMessage = packet.tags.firstOrNull { it.name == EC_TAG_STATS_LOGGER_MESSAGE }
                ?.subtags
                ?.filter { it.name == EC_TAG_STRING }
                ?.map { (it as StringTag).getValue() } ?: listOf(),
            totalSentBytes = packet.numeric(EC_TAG_STATS_TOTAL_SENT_BYTES)?.getInt() ?: 0,
            totalReceivedBytes = packet.numeric(EC_TAG_STATS_TOTAL_RECEIVED_BYTES)?.getInt() ?: 0,
            sharedFileCount = packet.numeric(EC_TAG_STATS_SHARED_FILE_COUNT)?.getInt() ?: 0,
            uploadSpeed = packet.numeric(EC_TAG_STATS_UL_SPEED)?.getInt() ?: 0,
            downloadSpeed = packet.numeric(EC_TAG_STATS_DL_SPEED)?.getInt() ?: 0,
            uploadSpeedLimit = packet.numeric(EC_TAG_STATS_UL_SPEED_LIMIT)?.getInt() ?: 0,
            downloadSpeedLimit = packet.numeric(EC_TAG_STATS_DL_SPEED_LIMIT)?.getInt() ?: 0,
            uploadQueueLength = packet.numeric(EC_TAG_STATS_UL_QUEUE_LEN)?.getInt() ?: 0,
            totalSourceCount = packet.numeric(EC_TAG_STATS_TOTAL_SRC_COUNT)?.getInt() ?: 0,
            ed2kUsers = packet.numeric(EC_TAG_STATS_ED2K_USERS)?.getInt() ?: 0,
            kadUsers = packet.numeric(EC_TAG_STATS_KAD_USERS)?.getInt() ?: 0,
            ed2kFiles = packet.numeric(EC_TAG_STATS_ED2K_FILES)?.getInt() ?: 0,
            kadFiles = packet.numeric(EC_TAG_STATS_KAD_FILES)?.getInt() ?: 0,
            kadNodes = packet.numeric(EC_TAG_STATS_KAD_NODES)?.getInt() ?: 0,
            // Following metrics are only available if the client is connected to the Kad network.
            kadFirewalledUdp = (packet.byte(EC_TAG_STATS_KAD_FIREWALLED_UDP)?.getValue()?.let { !it.equals(0u) }),
            kadIndexedSources = packet.numeric(EC_TAG_STATS_KAD_INDEXED_SOURCES)?.getInt(),
            kadIndexedKeywords = packet.numeric(EC_TAG_STATS_KAD_INDEXED_KEYWORDS)?.getInt(),
            kadIndexedNotes = packet.numeric(EC_TAG_STATS_KAD_INDEXED_NOTES)?.getInt(),
            kadIndexedLoad = packet.numeric(EC_TAG_STATS_KAD_INDEXED_LOAD)?.getInt(),
            kadIpAddress = packet.int(EC_TAG_STATS_KAD_IP_ADRESS)?.getValue()
                ?.let { ByteBuffer.allocate(4).putInt(it.toInt()).array() }
                ?.let { InetAddress.getByAddress(it).hostAddress },
            kadIsRunningInLanMode = packet.byte(EC_TAG_STATS_KAD_IN_LAN_MODE)?.getValue()?.let { !it.equals(0u) },
            buddyStatus = packet.byte(EC_TAG_STATS_BUDDY_STATUS)?.getValue()
                ?.let { BuddyState.fromByte(it) },
            buddyIp = packet.int(EC_TAG_STATS_BUDDY_IP)?.getValue()
                ?.let { ByteBuffer.allocate(4).putInt(it.toInt()).array() }
                ?.let { InetAddress.getByAddress(it).hostAddress },
            buddyPort = packet.short(EC_TAG_STATS_BUDDY_PORT)?.getValue()
        )
    }

    enum class BuddyState(val value: UByte) {
        Disconnected(0u),
        Connecting(1u),
        Connected(2u);

        companion object {
            fun fromByte(byte: UByte) = BuddyState.entries.firstOrNull { it.value == byte }
        }
    }

    data class ConnectionState(
        val ed2kConnected: Boolean,
        val ed2kConnecting: Boolean,
        val kadConnected: Boolean,
        val kadFirewalled: Boolean,
        val kadRunning: Boolean,
        val serverIpv4: Ipv4Tag.Ipv4?,
        val serverPing: Int?,
        val serverPrio: Int?,
        val serverFailed: Int?,
        val serverStatic: Boolean?,
        val serverVersion: String?,
        val serverDescription: String?,
        val serverUsers: Int?,
        val serverUsersMax: Int?,
        val serverFiles: Int?,
        val ed2kId: Int?,
        val kadId: Int?,
        val clientId: Int?
    ) {
        companion object {
            fun fromConStateTag(tag: Tag<*>): ConnectionState {
                val byte = tag.byte()!!.getValue()
                val serverTag = tag.subtags.firstOrNull { it.name == EC_TAG_SERVER }

                return ConnectionState(
                    ed2kConnected = !(byte and 0x01u).equals(0x00u),
                    ed2kConnecting = !(byte and 0x02u).equals(0x00u),
                    kadConnected = !(byte and 0x04u).equals(0x00u),
                    kadFirewalled = !(byte and 0x08u).equals(0x00u),
                    kadRunning = !(byte and 0x10u).equals(0x00u),
                    ed2kId = tag.subtags.numeric(EC_TAG_ED2K_ID)?.getInt(),
                    kadId = tag.subtags.numeric(EC_TAG_KAD_ID)?.getInt(),
                    clientId = tag.subtags.numeric(EC_TAG_CLIENT_ID)?.getInt(),
                    serverIpv4 = serverTag?.ipv4()?.getValue(),
                    serverPing = serverTag?.subtags?.numeric(EC_TAG_SERVER_PING)?.getInt(),
                    serverPrio = serverTag?.subtags?.numeric(EC_TAG_SERVER_PRIO)?.getInt(),
                    serverFailed = serverTag?.subtags?.numeric(EC_TAG_SERVER_FAILED)?.getInt(),
                    serverStatic = serverTag?.subtags?.numeric(EC_TAG_SERVER_STATIC)?.getInt()?.let { it != 0 },
                    serverVersion = serverTag?.subtags?.string(EC_TAG_SERVER_VERSION)?.getValue(),
                    serverDescription = serverTag?.subtags?.string(EC_TAG_SERVER_DESC)?.getValue(),
                    serverUsers = serverTag?.subtags?.numeric(EC_TAG_SERVER_USERS)?.getInt(),
                    serverUsersMax = serverTag?.subtags?.numeric(EC_TAG_SERVER_USERS_MAX)?.getInt(),
                    serverFiles = serverTag?.subtags?.numeric(EC_TAG_SERVER_FILES)?.getInt(),
                )
            }
        }
    }


}