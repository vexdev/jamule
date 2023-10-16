package jamule.request

import jamule.ec.packet.Packet

internal interface Request {
    fun packet(): Packet
}