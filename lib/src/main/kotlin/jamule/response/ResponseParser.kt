package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.ECTagName.*
import jamule.ec.packet.Packet
import org.slf4j.Logger

class ResponseParser(
    private val logger: Logger
) {

    @ExperimentalUnsignedTypes
    fun parse(packet: Packet): Response {
        logger.info("Parsing packet with opCode ${packet.opCode}")
        return when (packet.opCode) {
            ECOpCode.EC_OP_NOOP -> TODO()
            ECOpCode.EC_OP_AUTH_REQ -> TODO()
            ECOpCode.EC_OP_AUTH_FAIL -> TODO()
            ECOpCode.EC_OP_AUTH_OK -> AuthOkResponse(packet.string(EC_TAG_SERVER_VERSION).getValue())
            ECOpCode.EC_OP_FAILED -> AuthFailedResponse("aaa")
            ECOpCode.EC_OP_STRINGS -> TODO()
            ECOpCode.EC_OP_MISC_DATA -> TODO()
            ECOpCode.EC_OP_SHUTDOWN -> TODO()
            ECOpCode.EC_OP_ADD_LINK -> TODO()
            ECOpCode.EC_OP_STAT_REQ -> TODO()
            ECOpCode.EC_OP_GET_CONNSTATE -> TODO()
            ECOpCode.EC_OP_STATS -> TODO()
            ECOpCode.EC_OP_GET_DLOAD_QUEUE -> TODO()
            ECOpCode.EC_OP_GET_ULOAD_QUEUE -> TODO()
            ECOpCode.EC_OP_GET_SHARED_FILES -> TODO()
            ECOpCode.EC_OP_SHARED_SET_PRIO -> TODO()
            ECOpCode.EC_OP_PARTFILE_SWAP_A4AF_THIS -> TODO()
            ECOpCode.EC_OP_PARTFILE_SWAP_A4AF_THIS_AUTO -> TODO()
            ECOpCode.EC_OP_PARTFILE_SWAP_A4AF_OTHERS -> TODO()
            ECOpCode.EC_OP_PARTFILE_PAUSE -> TODO()
            ECOpCode.EC_OP_PARTFILE_RESUME -> TODO()
            ECOpCode.EC_OP_PARTFILE_STOP -> TODO()
            ECOpCode.EC_OP_PARTFILE_PRIO_SET -> TODO()
            ECOpCode.EC_OP_PARTFILE_DELETE -> TODO()
            ECOpCode.EC_OP_PARTFILE_SET_CAT -> TODO()
            ECOpCode.EC_OP_DLOAD_QUEUE -> TODO()
            ECOpCode.EC_OP_ULOAD_QUEUE -> TODO()
            ECOpCode.EC_OP_SHARED_FILES -> TODO()
            ECOpCode.EC_OP_SHAREDFILES_RELOAD -> TODO()
            ECOpCode.EC_OP_RENAME_FILE -> TODO()
            ECOpCode.EC_OP_SEARCH_START -> TODO()
            ECOpCode.EC_OP_SEARCH_STOP -> TODO()
            ECOpCode.EC_OP_SEARCH_RESULTS -> TODO()
            ECOpCode.EC_OP_SEARCH_PROGRESS -> TODO()
            ECOpCode.EC_OP_DOWNLOAD_SEARCH_RESULT -> TODO()
            ECOpCode.EC_OP_IPFILTER_RELOAD -> TODO()
            ECOpCode.EC_OP_GET_SERVER_LIST -> TODO()
            ECOpCode.EC_OP_SERVER_LIST -> TODO()
            ECOpCode.EC_OP_SERVER_DISCONNECT -> TODO()
            ECOpCode.EC_OP_SERVER_CONNECT -> TODO()
            ECOpCode.EC_OP_SERVER_REMOVE -> TODO()
            ECOpCode.EC_OP_SERVER_ADD -> TODO()
            ECOpCode.EC_OP_SERVER_UPDATE_FROM_URL -> TODO()
            ECOpCode.EC_OP_ADDLOGLINE -> TODO()
            ECOpCode.EC_OP_ADDDEBUGLOGLINE -> TODO()
            ECOpCode.EC_OP_GET_LOG -> TODO()
            ECOpCode.EC_OP_GET_DEBUGLOG -> TODO()
            ECOpCode.EC_OP_GET_SERVERINFO -> TODO()
            ECOpCode.EC_OP_LOG -> TODO()
            ECOpCode.EC_OP_DEBUGLOG -> TODO()
            ECOpCode.EC_OP_SERVERINFO -> TODO()
            ECOpCode.EC_OP_RESET_LOG -> TODO()
            ECOpCode.EC_OP_RESET_DEBUGLOG -> TODO()
            ECOpCode.EC_OP_CLEAR_SERVERINFO -> TODO()
            ECOpCode.EC_OP_GET_LAST_LOG_ENTRY -> TODO()
            ECOpCode.EC_OP_GET_PREFERENCES -> TODO()
            ECOpCode.EC_OP_SET_PREFERENCES -> TODO()
            ECOpCode.EC_OP_CREATE_CATEGORY -> TODO()
            ECOpCode.EC_OP_UPDATE_CATEGORY -> TODO()
            ECOpCode.EC_OP_DELETE_CATEGORY -> TODO()
            ECOpCode.EC_OP_GET_STATSGRAPHS -> TODO()
            ECOpCode.EC_OP_STATSGRAPHS -> TODO()
            ECOpCode.EC_OP_GET_STATSTREE -> TODO()
            ECOpCode.EC_OP_STATSTREE -> TODO()
            ECOpCode.EC_OP_KAD_START -> TODO()
            ECOpCode.EC_OP_KAD_STOP -> TODO()
            ECOpCode.EC_OP_CONNECT -> TODO()
            ECOpCode.EC_OP_DISCONNECT -> TODO()
            ECOpCode.EC_OP_KAD_UPDATE_FROM_URL -> TODO()
            ECOpCode.EC_OP_KAD_BOOTSTRAP_FROM_IP -> TODO()
            ECOpCode.EC_OP_AUTH_SALT -> AuthSaltResponse(packet.long(EC_TAG_PASSWD_SALT).getValue())
            ECOpCode.EC_OP_AUTH_PASSWD -> TODO()
            ECOpCode.EC_OP_IPFILTER_UPDATE -> TODO()
            ECOpCode.EC_OP_GET_UPDATE -> TODO()
            ECOpCode.EC_OP_CLEAR_COMPLETED -> TODO()
            ECOpCode.EC_OP_CLIENT_SWAP_TO_ANOTHER_FILE -> TODO()
            ECOpCode.EC_OP_SHARED_FILE_SET_COMMENT -> TODO()
            ECOpCode.EC_OP_SERVER_SET_STATIC_PRIO -> TODO()
            ECOpCode.EC_OP_FRIEND -> TODO()
        }
    }

}