package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class StringsResponse(val string: String) : Response {
    companion object {
        @ResponseDeserializer(ECOpCode.EC_OP_STRINGS)
        @JvmStatic
        internal fun fromPacket(packet: Packet) = StringsResponse(packet.string(ECTagName.EC_TAG_STRING)!!.getValue())
    }
}