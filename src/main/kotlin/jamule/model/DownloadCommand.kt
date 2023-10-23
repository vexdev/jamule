package jamule.model

import jamule.ec.ECOpCode

/**
 * Represents the commands that can be sent to a download.
 */
enum class DownloadCommand(internal val value: ECOpCode) {
    SWAP_A4AF_THIS(ECOpCode.EC_OP_PARTFILE_SWAP_A4AF_THIS),
    SWAP_A4AF_THIS_AUTO(ECOpCode.EC_OP_PARTFILE_SWAP_A4AF_THIS_AUTO),
    SWAP_A4AF_OTHERS(ECOpCode.EC_OP_PARTFILE_SWAP_A4AF_OTHERS),
    PAUSE(ECOpCode.EC_OP_PARTFILE_PAUSE),
    RESUME(ECOpCode.EC_OP_PARTFILE_RESUME),
    STOP(ECOpCode.EC_OP_PARTFILE_STOP),
    DELETE(ECOpCode.EC_OP_PARTFILE_DELETE),
}