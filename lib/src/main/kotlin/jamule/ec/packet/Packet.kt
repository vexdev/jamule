package jamule.ec.packet

import jamule.ec.ECOpCode
import jamule.ec.tag.Tag

data class Packet(
    val opCode: ECOpCode = ECOpCode.EC_OP_NOOP,
    val tags: List<Tag<*>> = mutableListOf(),
    val flags: Flags,
    val accepts: Flags? = null,
    val id: UInt? = null,
)
