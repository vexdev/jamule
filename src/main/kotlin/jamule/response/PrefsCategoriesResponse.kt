package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.packet.Packet
import jamule.ec.packet.Packet.Companion.byte
import jamule.ec.packet.Packet.Companion.numeric
import jamule.ec.packet.Packet.Companion.string
import jamule.model.AmuleCategory

data class PrefsCategoriesResponse(val categories: List<AmuleCategory>) : Response {
    internal object PrefsCategoriesResponseDeserializer : ResponseDeserializer() {
        @OptIn(ExperimentalUnsignedTypes::class)
        override fun canDeserialize(packet: Packet) =
            packet.opCode == ECOpCode.EC_OP_SET_PREFERENCES
                    && packet.custom(ECTagName.EC_TAG_PREFS_CATEGORIES) != null

        @OptIn(ExperimentalUnsignedTypes::class)
        override fun deserialize(packet: Packet) = packet
            .custom(ECTagName.EC_TAG_PREFS_CATEGORIES)!!
            .subtags
            .filter { it.name == ECTagName.EC_TAG_CATEGORY }
            .map {
                AmuleCategory(
                    name = it.subtags.string(ECTagName.EC_TAG_CATEGORY_TITLE)!!.getValue(),
                    path = it.subtags.string(ECTagName.EC_TAG_CATEGORY_PATH)!!.getValue(),
                    comment = it.subtags.string(ECTagName.EC_TAG_CATEGORY_COMMENT)!!.getValue(),
                    priority = it.subtags.byte(ECTagName.EC_TAG_CATEGORY_PRIO)!!.getValue().toByte(),
                    color = it.subtags.numeric(ECTagName.EC_TAG_CATEGORY_COLOR)!!.getInt(),
                )
            }
            .let { PrefsCategoriesResponse(it) }
    }
}