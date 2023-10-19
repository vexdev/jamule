package jamule.request

import jamule.ec.ECDetailLevel
import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.EcPrefs
import jamule.ec.packet.Packet
import jamule.ec.tag.UByteTag
import jamule.ec.tag.UIntTag

internal data class GetPreferencesRequest(val prefs: EcPrefs) : Request {

    override fun packet(): Packet = Packet(
        ECOpCode.EC_OP_GET_PREFERENCES,
        listOf(
            UByteTag(ECTagName.EC_TAG_DETAIL_LEVEL, ECDetailLevel.EC_DETAIL_FULL.value),
            UIntTag(ECTagName.EC_TAG_SELECT_PREFS, prefs.value),
        )
    )

}