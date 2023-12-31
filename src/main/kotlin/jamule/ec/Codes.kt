package jamule.ec

enum class ProtocolVersion(val value: UShort) {
    EC_CURRENT_PROTOCOL_VERSION(0x0204u)
}

enum class ECFlag(val value: UInt) {
    EC_FLAG_ZLIB(0x00000001u),
    EC_FLAG_UTF8_NUMBERS(0x00000002u),
    EC_FLAG_HAS_ID(0x00000004u),
    EC_FLAG_ACCEPTS(0x00000010u),
    EC_FLAG_UNKNOWN_MASK(0xff7f7f08u)
}

enum class ECOpCode(val value: UByte) {
    EC_OP_NOOP(0x01u),
    EC_OP_AUTH_REQ(0x02u),
    EC_OP_AUTH_FAIL(0x03u),
    EC_OP_AUTH_OK(0x04u),
    EC_OP_FAILED(0x05u),
    EC_OP_STRINGS(0x06u),
    EC_OP_MISC_DATA(0x07u),
    EC_OP_SHUTDOWN(0x08u),
    EC_OP_ADD_LINK(0x09u),
    EC_OP_STAT_REQ(0x0Au),
    EC_OP_GET_CONNSTATE(0x0Bu),
    EC_OP_STATS(0x0Cu),
    EC_OP_GET_DLOAD_QUEUE(0x0Du),
    EC_OP_GET_ULOAD_QUEUE(0x0Eu),
    EC_OP_GET_SHARED_FILES(0x10u),
    EC_OP_SHARED_SET_PRIO(0x11u),
    EC_OP_PARTFILE_SWAP_A4AF_THIS(0x16u),
    EC_OP_PARTFILE_SWAP_A4AF_THIS_AUTO(0x17u),
    EC_OP_PARTFILE_SWAP_A4AF_OTHERS(0x18u),
    EC_OP_PARTFILE_PAUSE(0x19u),
    EC_OP_PARTFILE_RESUME(0x1Au),
    EC_OP_PARTFILE_STOP(0x1Bu),
    EC_OP_PARTFILE_PRIO_SET(0x1Cu),
    EC_OP_PARTFILE_DELETE(0x1Du),
    EC_OP_PARTFILE_SET_CAT(0x1Eu),
    EC_OP_DLOAD_QUEUE(0x1Fu),
    EC_OP_ULOAD_QUEUE(0x20u),
    EC_OP_SHARED_FILES(0x22u),
    EC_OP_SHAREDFILES_RELOAD(0x23u),
    EC_OP_RENAME_FILE(0x25u),
    EC_OP_SEARCH_START(0x26u),
    EC_OP_SEARCH_STOP(0x27u),
    EC_OP_SEARCH_RESULTS(0x28u),
    EC_OP_SEARCH_PROGRESS(0x29u),
    EC_OP_DOWNLOAD_SEARCH_RESULT(0x2Au),
    EC_OP_IPFILTER_RELOAD(0x2Bu),
    EC_OP_GET_SERVER_LIST(0x2Cu),
    EC_OP_SERVER_LIST(0x2Du),
    EC_OP_SERVER_DISCONNECT(0x2Eu),
    EC_OP_SERVER_CONNECT(0x2Fu),
    EC_OP_SERVER_REMOVE(0x30u),
    EC_OP_SERVER_ADD(0x31u),
    EC_OP_SERVER_UPDATE_FROM_URL(0x32u),
    EC_OP_ADDLOGLINE(0x33u),
    EC_OP_ADDDEBUGLOGLINE(0x34u),
    EC_OP_GET_LOG(0x35u),
    EC_OP_GET_DEBUGLOG(0x36u),
    EC_OP_GET_SERVERINFO(0x37u),
    EC_OP_LOG(0x38u),
    EC_OP_DEBUGLOG(0x39u),
    EC_OP_SERVERINFO(0x3Au),
    EC_OP_RESET_LOG(0x3Bu),
    EC_OP_RESET_DEBUGLOG(0x3Cu),
    EC_OP_CLEAR_SERVERINFO(0x3Du),
    EC_OP_GET_LAST_LOG_ENTRY(0x3Eu),
    EC_OP_GET_PREFERENCES(0x3Fu),
    EC_OP_SET_PREFERENCES(0x40u),
    EC_OP_CREATE_CATEGORY(0x41u),
    EC_OP_UPDATE_CATEGORY(0x42u),
    EC_OP_DELETE_CATEGORY(0x43u),
    EC_OP_GET_STATSGRAPHS(0x44u),
    EC_OP_STATSGRAPHS(0x45u),
    EC_OP_GET_STATSTREE(0x46u),
    EC_OP_STATSTREE(0x47u),
    EC_OP_KAD_START(0x48u),
    EC_OP_KAD_STOP(0x49u),
    EC_OP_CONNECT(0x4Au),
    EC_OP_DISCONNECT(0x4Bu),
    EC_OP_KAD_UPDATE_FROM_URL(0x4Du),
    EC_OP_KAD_BOOTSTRAP_FROM_IP(0x4Eu),
    EC_OP_AUTH_SALT(0x4Fu),
    EC_OP_AUTH_PASSWD(0x50u),
    EC_OP_IPFILTER_UPDATE(0x51u),
    EC_OP_GET_UPDATE(0x52u),
    EC_OP_CLEAR_COMPLETED(0x53u),
    EC_OP_CLIENT_SWAP_TO_ANOTHER_FILE(0x54u),
    EC_OP_SHARED_FILE_SET_COMMENT(0x55u),
    EC_OP_SERVER_SET_STATIC_PRIO(0x56u),
    EC_OP_FRIEND(0x57u);

    companion object {
        fun fromValue(value: UByte): ECOpCode {
            return entries.first { it.value == value }
        }
    }
}

enum class ECTagName(val value: UShort) {
    EC_TAG_STRING(0x0000u),
    EC_TAG_PASSWD_HASH(0x0001u),
    EC_TAG_PROTOCOL_VERSION(0x0002u),
    EC_TAG_VERSION_ID(0x0003u),
    EC_TAG_DETAIL_LEVEL(0x0004u),
    EC_TAG_CONNSTATE(0x0005u),
    EC_TAG_ED2K_ID(0x0006u),
    EC_TAG_LOG_TO_STATUS(0x0007u),
    EC_TAG_BOOTSTRAP_IP(0x0008u),
    EC_TAG_BOOTSTRAP_PORT(0x0009u),
    EC_TAG_CLIENT_ID(0x000Au),
    EC_TAG_PASSWD_SALT(0x000Bu),
    EC_TAG_CAN_ZLIB(0x000Cu),
    EC_TAG_CAN_UTF8_NUMBERS(0x000Du),
    EC_TAG_CAN_NOTIFY(0x000Eu),
    EC_TAG_ECID(0x000Fu),
    EC_TAG_KAD_ID(0x0010u),
    EC_TAG_CLIENT_NAME(0x0100u),
    EC_TAG_CLIENT_VERSION(0x0101u),
    EC_TAG_CLIENT_MOD(0x0102u),
    EC_TAG_STATS_UL_SPEED(0x0200u),
    EC_TAG_STATS_DL_SPEED(0x0201u),
    EC_TAG_STATS_UL_SPEED_LIMIT(0x0202u),
    EC_TAG_STATS_DL_SPEED_LIMIT(0x0203u),
    EC_TAG_STATS_UP_OVERHEAD(0x0204u),
    EC_TAG_STATS_DOWN_OVERHEAD(0x0205u),
    EC_TAG_STATS_TOTAL_SRC_COUNT(0x0206u),
    EC_TAG_STATS_BANNED_COUNT(0x0207u),
    EC_TAG_STATS_UL_QUEUE_LEN(0x0208u),
    EC_TAG_STATS_ED2K_USERS(0x0209u),
    EC_TAG_STATS_KAD_USERS(0x020Au),
    EC_TAG_STATS_ED2K_FILES(0x020Bu),
    EC_TAG_STATS_KAD_FILES(0x020Cu),
    EC_TAG_STATS_LOGGER_MESSAGE(0x020Du),
    EC_TAG_STATS_KAD_FIREWALLED_UDP(0x020Eu),
    EC_TAG_STATS_KAD_INDEXED_SOURCES(0x020Fu),
    EC_TAG_STATS_KAD_INDEXED_KEYWORDS(0x0210u),
    EC_TAG_STATS_KAD_INDEXED_NOTES(0x0211u),
    EC_TAG_STATS_KAD_INDEXED_LOAD(0x0212u),
    EC_TAG_STATS_KAD_IP_ADRESS(0x0213u),
    EC_TAG_STATS_BUDDY_STATUS(0x0214u),
    EC_TAG_STATS_BUDDY_IP(0x0215u),
    EC_TAG_STATS_BUDDY_PORT(0x0216u),
    EC_TAG_STATS_KAD_IN_LAN_MODE(0x0217u),
    EC_TAG_STATS_TOTAL_SENT_BYTES(0x0218u),
    EC_TAG_STATS_TOTAL_RECEIVED_BYTES(0x0219u),
    EC_TAG_STATS_SHARED_FILE_COUNT(0x021Au),
    EC_TAG_STATS_KAD_NODES(0x021Bu),
    EC_TAG_PARTFILE(0x0300u),
    EC_TAG_PARTFILE_NAME(0x0301u),
    EC_TAG_PARTFILE_PARTMETID(0x0302u),
    EC_TAG_PARTFILE_SIZE_FULL(0x0303u),
    EC_TAG_PARTFILE_SIZE_XFER(0x0304u),
    EC_TAG_PARTFILE_SIZE_XFER_UP(0x0305u),
    EC_TAG_PARTFILE_SIZE_DONE(0x0306u),
    EC_TAG_PARTFILE_SPEED(0x0307u),
    EC_TAG_PARTFILE_STATUS(0x0308u),
    EC_TAG_PARTFILE_PRIO(0x0309u),
    EC_TAG_PARTFILE_SOURCE_COUNT(0x030Au),
    EC_TAG_PARTFILE_SOURCE_COUNT_A4AF(0x030Bu),
    EC_TAG_PARTFILE_SOURCE_COUNT_NOT_CURRENT(0x030Cu),
    EC_TAG_PARTFILE_SOURCE_COUNT_XFER(0x030Du),
    EC_TAG_PARTFILE_ED2K_LINK(0x030Eu),
    EC_TAG_PARTFILE_CAT(0x030Fu),
    EC_TAG_PARTFILE_LAST_RECV(0x0310u),
    EC_TAG_PARTFILE_LAST_SEEN_COMP(0x0311u),
    EC_TAG_PARTFILE_PART_STATUS(0x0312u),
    EC_TAG_PARTFILE_GAP_STATUS(0x0313u),
    EC_TAG_PARTFILE_REQ_STATUS(0x0314u),
    EC_TAG_PARTFILE_SOURCE_NAMES(0x0315u),
    EC_TAG_PARTFILE_COMMENTS(0x0316u),
    EC_TAG_PARTFILE_STOPPED(0x0317u),
    EC_TAG_PARTFILE_DOWNLOAD_ACTIVE(0x0318u),
    EC_TAG_PARTFILE_LOST_CORRUPTION(0x0319u),
    EC_TAG_PARTFILE_GAINED_COMPRESSION(0x031Au),
    EC_TAG_PARTFILE_SAVED_ICH(0x031Bu),
    EC_TAG_PARTFILE_SOURCE_NAMES_COUNTS(0x031Cu),
    EC_TAG_PARTFILE_AVAILABLE_PARTS(0x031Du),
    EC_TAG_PARTFILE_HASH(0x031Eu),
    EC_TAG_PARTFILE_SHARED(0x031Fu),
    EC_TAG_PARTFILE_HASHED_PART_COUNT(0x0320u),
    EC_TAG_PARTFILE_A4AFAUTO(0x0321u),
    EC_TAG_PARTFILE_A4AF_SOURCES(0x0322u),
    EC_TAG_KNOWNFILE(0x0400u),
    EC_TAG_KNOWNFILE_XFERRED(0x0401u),
    EC_TAG_KNOWNFILE_XFERRED_ALL(0x0402u),
    EC_TAG_KNOWNFILE_REQ_COUNT(0x0403u),
    EC_TAG_KNOWNFILE_REQ_COUNT_ALL(0x0404u),
    EC_TAG_KNOWNFILE_ACCEPT_COUNT(0x0405u),
    EC_TAG_KNOWNFILE_ACCEPT_COUNT_ALL(0x0406u),
    EC_TAG_KNOWNFILE_AICH_MASTERHASH(0x0407u),
    EC_TAG_KNOWNFILE_FILENAME(0x0408u),
    EC_TAG_KNOWNFILE_COMPLETE_SOURCES_LOW(0x0409u),
    EC_TAG_KNOWNFILE_COMPLETE_SOURCES_HIGH(0x040Au),
    EC_TAG_KNOWNFILE_PRIO(0x040Bu),
    EC_TAG_KNOWNFILE_ON_QUEUE(0x040Cu),
    EC_TAG_KNOWNFILE_COMPLETE_SOURCES(0x040Du),
    EC_TAG_KNOWNFILE_COMMENT(0x040Eu),
    EC_TAG_KNOWNFILE_RATING(0x040Fu),
    EC_TAG_SERVER(0x0500u),
    EC_TAG_SERVER_NAME(0x0501u),
    EC_TAG_SERVER_DESC(0x0502u),
    EC_TAG_SERVER_ADDRESS(0x0503u),
    EC_TAG_SERVER_PING(0x0504u),
    EC_TAG_SERVER_USERS(0x0505u),
    EC_TAG_SERVER_USERS_MAX(0x0506u),
    EC_TAG_SERVER_FILES(0x0507u),
    EC_TAG_SERVER_PRIO(0x0508u),
    EC_TAG_SERVER_FAILED(0x0509u),
    EC_TAG_SERVER_STATIC(0x050Au),
    EC_TAG_SERVER_VERSION(0x050Bu),
    EC_TAG_SERVER_IP(0x050Cu),
    EC_TAG_SERVER_PORT(0x050Du),
    EC_TAG_CLIENT(0x0600u),
    EC_TAG_CLIENT_SOFTWARE(0x0601u),
    EC_TAG_CLIENT_SCORE(0x0602u),
    EC_TAG_CLIENT_HASH(0x0603u),
    EC_TAG_CLIENT_FRIEND_SLOT(0x0604u),
    EC_TAG_CLIENT_WAIT_TIME(0x0605u),
    EC_TAG_CLIENT_XFER_TIME(0x0606u),
    EC_TAG_CLIENT_QUEUE_TIME(0x0607u),
    EC_TAG_CLIENT_LAST_TIME(0x0608u),
    EC_TAG_CLIENT_UPLOAD_SESSION(0x0609u),
    EC_TAG_CLIENT_UPLOAD_TOTAL(0x060Au),
    EC_TAG_CLIENT_DOWNLOAD_TOTAL(0x060Bu),
    EC_TAG_CLIENT_DOWNLOAD_STATE(0x060Cu),
    EC_TAG_CLIENT_UP_SPEED(0x060Du),
    EC_TAG_CLIENT_DOWN_SPEED(0x060Eu),
    EC_TAG_CLIENT_FROM(0x060Fu),
    EC_TAG_CLIENT_USER_IP(0x0610u),
    EC_TAG_CLIENT_USER_PORT(0x0611u),
    EC_TAG_CLIENT_SERVER_IP(0x0612u),
    EC_TAG_CLIENT_SERVER_PORT(0x0613u),
    EC_TAG_CLIENT_SERVER_NAME(0x0614u),
    EC_TAG_CLIENT_SOFT_VER_STR(0x0615u),
    EC_TAG_CLIENT_WAITING_POSITION(0x0616u),
    EC_TAG_CLIENT_IDENT_STATE(0x0617u),
    EC_TAG_CLIENT_OBFUSCATION_STATUS(0x0618u),
    EC_TAG_CLIENT_CURRENTLYUNUSED1(0x0619u),
    EC_TAG_CLIENT_REMOTE_QUEUE_RANK(0x061Au),
    EC_TAG_CLIENT_DISABLE_VIEW_SHARED(0x061Bu),
    EC_TAG_CLIENT_UPLOAD_STATE(0x061Cu),
    EC_TAG_CLIENT_EXT_PROTOCOL(0x061Du),
    EC_TAG_CLIENT_USER_ID(0x061Eu),
    EC_TAG_CLIENT_UPLOAD_FILE(0x061Fu),
    EC_TAG_CLIENT_REQUEST_FILE(0x0620u),
    EC_TAG_CLIENT_A4AF_FILES(0x0621u),
    EC_TAG_CLIENT_OLD_REMOTE_QUEUE_RANK(0x0622u),
    EC_TAG_CLIENT_KAD_PORT(0x0623u),
    EC_TAG_CLIENT_PART_STATUS(0x0624u),
    EC_TAG_CLIENT_NEXT_REQUESTED_PART(0x0625u),
    EC_TAG_CLIENT_LAST_DOWNLOADING_PART(0x0626u),
    EC_TAG_CLIENT_REMOTE_FILENAME(0x0627u),
    EC_TAG_CLIENT_MOD_VERSION(0x0628u),
    EC_TAG_CLIENT_OS_INFO(0x0629u),
    EC_TAG_CLIENT_AVAILABLE_PARTS(0x062Au),
    EC_TAG_CLIENT_UPLOAD_PART_STATUS(0x062Bu),
    EC_TAG_SEARCHFILE(0x0700u),
    EC_TAG_SEARCH_TYPE(0x0701u),
    EC_TAG_SEARCH_NAME(0x0702u),
    EC_TAG_SEARCH_MIN_SIZE(0x0703u),
    EC_TAG_SEARCH_MAX_SIZE(0x0704u),
    EC_TAG_SEARCH_FILE_TYPE(0x0705u),
    EC_TAG_SEARCH_EXTENSION(0x0706u),
    EC_TAG_SEARCH_AVAILABILITY(0x0707u),
    EC_TAG_SEARCH_STATUS(0x0708u),
    EC_TAG_SEARCH_PARENT(0x0709u),
    EC_TAG_FRIEND(0x0800u),
    EC_TAG_FRIEND_NAME(0x0801u),
    EC_TAG_FRIEND_HASH(0x0802u),
    EC_TAG_FRIEND_IP(0x0803u),
    EC_TAG_FRIEND_PORT(0x0804u),
    EC_TAG_FRIEND_CLIENT(0x0805u),
    EC_TAG_FRIEND_ADD(0x0806u),
    EC_TAG_FRIEND_REMOVE(0x0807u),
    EC_TAG_FRIEND_FRIENDSLOT(0x0808u),
    EC_TAG_FRIEND_SHARED(0x0809u),
    EC_TAG_SELECT_PREFS(0x1000u),
    EC_TAG_PREFS_CATEGORIES(0x1100u),
    EC_TAG_CATEGORY(0x1101u),
    EC_TAG_CATEGORY_TITLE(0x1102u),
    EC_TAG_CATEGORY_PATH(0x1103u),
    EC_TAG_CATEGORY_COMMENT(0x1104u),
    EC_TAG_CATEGORY_COLOR(0x1105u),
    EC_TAG_CATEGORY_PRIO(0x1106u),
    EC_TAG_PREFS_GENERAL(0x1200u),
    EC_TAG_USER_NICK(0x1201u),
    EC_TAG_USER_HASH(0x1202u),
    EC_TAG_USER_HOST(0x1203u),
    EC_TAG_GENERAL_CHECK_NEW_VERSION(0x1204u),
    EC_TAG_PREFS_CONNECTIONS(0x1300u),
    EC_TAG_CONN_DL_CAP(0x1301u),
    EC_TAG_CONN_UL_CAP(0x1302u),
    EC_TAG_CONN_MAX_DL(0x1303u),
    EC_TAG_CONN_MAX_UL(0x1304u),
    EC_TAG_CONN_SLOT_ALLOCATION(0x1305u),
    EC_TAG_CONN_TCP_PORT(0x1306u),
    EC_TAG_CONN_UDP_PORT(0x1307u),
    EC_TAG_CONN_UDP_DISABLE(0x1308u),
    EC_TAG_CONN_MAX_FILE_SOURCES(0x1309u),
    EC_TAG_CONN_MAX_CONN(0x130Au),
    EC_TAG_CONN_AUTOCONNECT(0x130Bu),
    EC_TAG_CONN_RECONNECT(0x130Cu),
    EC_TAG_NETWORK_ED2K(0x130Du),
    EC_TAG_NETWORK_KADEMLIA(0x130Eu),
    EC_TAG_PREFS_MESSAGEFILTER(0x1400u),
    EC_TAG_MSGFILTER_ENABLED(0x1401u),
    EC_TAG_MSGFILTER_ALL(0x1402u),
    EC_TAG_MSGFILTER_FRIENDS(0x1403u),
    EC_TAG_MSGFILTER_SECURE(0x1404u),
    EC_TAG_MSGFILTER_BY_KEYWORD(0x1405u),
    EC_TAG_MSGFILTER_KEYWORDS(0x1406u),
    EC_TAG_PREFS_REMOTECTRL(0x1500u),
    EC_TAG_WEBSERVER_AUTORUN(0x1501u),
    EC_TAG_WEBSERVER_PORT(0x1502u),
    EC_TAG_WEBSERVER_GUEST(0x1503u),
    EC_TAG_WEBSERVER_USEGZIP(0x1504u),
    EC_TAG_WEBSERVER_REFRESH(0x1505u),
    EC_TAG_WEBSERVER_TEMPLATE(0x1506u),
    EC_TAG_PREFS_ONLINESIG(0x1600u),
    EC_TAG_ONLINESIG_ENABLED(0x1601u),
    EC_TAG_PREFS_SERVERS(0x1700u),
    EC_TAG_SERVERS_REMOVE_DEAD(0x1701u),
    EC_TAG_SERVERS_DEAD_SERVER_RETRIES(0x1702u),
    EC_TAG_SERVERS_AUTO_UPDATE(0x1703u),
    EC_TAG_SERVERS_URL_LIST(0x1704u),
    EC_TAG_SERVERS_ADD_FROM_SERVER(0x1705u),
    EC_TAG_SERVERS_ADD_FROM_CLIENT(0x1706u),
    EC_TAG_SERVERS_USE_SCORE_SYSTEM(0x1707u),
    EC_TAG_SERVERS_SMART_ID_CHECK(0x1708u),
    EC_TAG_SERVERS_SAFE_SERVER_CONNECT(0x1709u),
    EC_TAG_SERVERS_AUTOCONN_STATIC_ONLY(0x170Au),
    EC_TAG_SERVERS_MANUAL_HIGH_PRIO(0x170Bu),
    EC_TAG_SERVERS_UPDATE_URL(0x170Cu),
    EC_TAG_PREFS_FILES(0x1800u),
    EC_TAG_FILES_ICH_ENABLED(0x1801u),
    EC_TAG_FILES_AICH_TRUST(0x1802u),
    EC_TAG_FILES_NEW_PAUSED(0x1803u),
    EC_TAG_FILES_NEW_AUTO_DL_PRIO(0x1804u),
    EC_TAG_FILES_PREVIEW_PRIO(0x1805u),
    EC_TAG_FILES_NEW_AUTO_UL_PRIO(0x1806u),
    EC_TAG_FILES_UL_FULL_CHUNKS(0x1807u),
    EC_TAG_FILES_START_NEXT_PAUSED(0x1808u),
    EC_TAG_FILES_RESUME_SAME_CAT(0x1809u),
    EC_TAG_FILES_SAVE_SOURCES(0x180Au),
    EC_TAG_FILES_EXTRACT_METADATA(0x180Bu),
    EC_TAG_FILES_ALLOC_FULL_SIZE(0x180Cu),
    EC_TAG_FILES_CHECK_FREE_SPACE(0x180Du),
    EC_TAG_FILES_MIN_FREE_SPACE(0x180Eu),
    EC_TAG_FILES_CREATE_NORMAL(0x180Fu),
    EC_TAG_PREFS_DIRECTORIES(0x1A00u),
    EC_TAG_DIRECTORIES_INCOMING(0x1A01u),
    EC_TAG_DIRECTORIES_TEMP(0x1A02u),
    EC_TAG_DIRECTORIES_SHARED(0x1A03u),
    EC_TAG_DIRECTORIES_SHARE_HIDDEN(0x1A04u),
    EC_TAG_PREFS_STATISTICS(0x1B00u),
    EC_TAG_STATSGRAPH_WIDTH(0x1B01u),
    EC_TAG_STATSGRAPH_SCALE(0x1B02u),
    EC_TAG_STATSGRAPH_LAST(0x1B03u),
    EC_TAG_STATSGRAPH_DATA(0x1B04u),
    EC_TAG_STATTREE_CAPPING(0x1B05u),
    EC_TAG_STATTREE_NODE(0x1B06u),
    EC_TAG_STAT_NODE_VALUE(0x1B07u),
    EC_TAG_STAT_VALUE_TYPE(0x1B08u),
    EC_TAG_STATTREE_NODEID(0x1B09u),
    EC_TAG_PREFS_SECURITY(0x1C00u),
    EC_TAG_SECURITY_CAN_SEE_SHARES(0x1C01u),
    EC_TAG_IPFILTER_CLIENTS(0x1C02u),
    EC_TAG_IPFILTER_SERVERS(0x1C03u),
    EC_TAG_IPFILTER_AUTO_UPDATE(0x1C04u),
    EC_TAG_IPFILTER_UPDATE_URL(0x1C05u),
    EC_TAG_IPFILTER_LEVEL(0x1C06u),
    EC_TAG_IPFILTER_FILTER_LAN(0x1C07u),
    EC_TAG_SECURITY_USE_SECIDENT(0x1C08u),
    EC_TAG_SECURITY_OBFUSCATION_SUPPORTED(0x1C09u),
    EC_TAG_SECURITY_OBFUSCATION_REQUESTED(0x1C0Au),
    EC_TAG_SECURITY_OBFUSCATION_REQUIRED(0x1C0Bu),
    EC_TAG_PREFS_CORETWEAKS(0x1D00u),
    EC_TAG_CORETW_MAX_CONN_PER_FIVE(0x1D01u),
    EC_TAG_CORETW_VERBOSE(0x1D02u),
    EC_TAG_CORETW_FILEBUFFER(0x1D03u),
    EC_TAG_CORETW_UL_QUEUE(0x1D04u),
    EC_TAG_CORETW_SRV_KEEPALIVE_TIMEOUT(0x1D05u),
    EC_TAG_PREFS_KADEMLIA(0x1E00u),
    EC_TAG_KADEMLIA_UPDATE_URL(0x1E01u),
    EC_TAG_UNKNOWN(0xFFFFu);

    companion object {
        fun fromValue(value: UShort): ECTagName {
            return entries.firstOrNull { it.value == value } ?: EC_TAG_UNKNOWN
        }
    }
}

enum class ECDetailLevel(val value: UByte) {
    EC_DETAIL_CMD(0x00u),
    EC_DETAIL_WEB(0x01u),
    EC_DETAIL_FULL(0x02u),
    EC_DETAIL_UPDATE(0x03u),
    EC_DETAIL_INC_UPDATE(0x04u);

    companion object {
        fun fromValue(value: UByte): ECDetailLevel {
            return entries.first { it.value == value }
        }
    }
}

enum class ECSearchType(val value: UByte) {
    EC_SEARCH_LOCAL(0x00u),
    EC_SEARCH_GLOBAL(0x01u),
    EC_SEARCH_KAD(0x02u),
    EC_SEARCH_WEB(0x03u);

    companion object {
        fun fromValue(value: UByte): ECSearchType {
            return entries.first { it.value == value }
        }
    }
}

enum class ECStatTreeNodeValueType(val value: UByte) {
    EC_VALUE_INTEGER(0x00u),
    EC_VALUE_ISTRING(0x01u),
    EC_VALUE_BYTES(0x02u),
    EC_VALUE_ISHORT(0x03u),
    EC_VALUE_TIME(0x04u),
    EC_VALUE_SPEED(0x05u),
    EC_VALUE_STRING(0x06u),
    EC_VALUE_DOUBLE(0x07u);

    companion object {
        fun fromValue(value: UByte): ECStatTreeNodeValueType {
            return entries.first { it.value == value }
        }
    }
}

enum class EcPrefs(val value: UInt) {
    EC_PREFS_CATEGORIES(0x00000001u),
    EC_PREFS_GENERAL(0x00000002u),
    EC_PREFS_CONNECTIONS(0x00000004u),
    EC_PREFS_MESSAGEFILTER(0x00000008u),
    EC_PREFS_REMOTECONTROLS(0x00000010u),
    EC_PREFS_ONLINESIG(0x00000020u),
    EC_PREFS_SERVERS(0x00000040u),
    EC_PREFS_FILES(0x00000080u),
    EC_PREFS_DIRECTORIES(0x00000200u),
    EC_PREFS_STATISTICS(0x00000400u),
    EC_PREFS_SECURITY(0x00000800u),
    EC_PREFS_CORETWEAKS(0x00001000u),
    EC_PREFS_KADEMLIA(0x00002000u);

    companion object {
        fun fromValue(value: UInt): EcPrefs {
            return entries.first { it.value == value }
        }
    }
}

enum class ECTagType(val value: UByte) {
    EC_TAGTYPE_UNKNOWN(0u),
    EC_TAGTYPE_CUSTOM(1u),
    EC_TAGTYPE_UINT8(2u),
    EC_TAGTYPE_UINT16(3u),
    EC_TAGTYPE_UINT32(4u),
    EC_TAGTYPE_UINT64(5u),
    EC_TAGTYPE_STRING(6u),
    EC_TAGTYPE_DOUBLE(7u),
    EC_TAGTYPE_IPV4(8u),
    EC_TAGTYPE_HASH16(9u),
    EC_TAGTYPE_UINT128(10u);

    companion object {
        fun fromValue(value: UByte): ECTagType {
            return entries.firstOrNull { it.value == value } ?: EC_TAGTYPE_UNKNOWN
        }
    }
}

// Possible download status of a file
internal enum class ECSearchFileDownloadStatus(val value: UByte) {
    NEW(0u),                // not known
    DOWNLOADED(1u),        // successfully downloaded or shared
    QUEUED(2u),            // downloading (Partfile)
    CANCELED(3u),            // canceled
    QUEUEDCANCELED(4u);    // canceled once, but now downloading again

    companion object {
        fun fromValue(value: UByte): ECSearchFileDownloadStatus {
            return entries.first { it.value == value }
        }
    }
}