package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet

data class AuthFailedResponse(val reason: String) : Exception(reason), Response {
    companion object {
        @ResponseDeserializer(ECOpCode.EC_OP_AUTH_FAIL)
        @JvmStatic
        internal fun fromPacket(packet: Packet) =
            AuthFailedResponse(packet.string(ECTagName.EC_TAG_STRING)!!.getValue())
    }
}