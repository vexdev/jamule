package jamule.request

import jamule.ec.packet.Packet

interface Request {
    @OptIn(ExperimentalUnsignedTypes::class)
    fun packet(): Packet
}