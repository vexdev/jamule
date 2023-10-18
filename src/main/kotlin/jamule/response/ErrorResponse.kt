package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class ErrorResponse(val serverMessage: String) : Exception(serverMessage), Response {

    companion object {
        @ResponseDeserializer(ECOpCode.EC_OP_FAILED)
        @JvmStatic
        internal fun fromPacket(packet: Packet) = ErrorResponse(packet.string(ECTagName.EC_TAG_STRING)!!.getValue())
    }
}