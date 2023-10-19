package jamule.response

import jamule.ec.packet.Packet

internal abstract class ResponseDeserializer {
    abstract fun canDeserialize(packet: Packet): Boolean
    abstract fun deserialize(packet: Packet): Response
}