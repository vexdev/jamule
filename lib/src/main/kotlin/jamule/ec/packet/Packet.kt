package jamule.ec.packet

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.tag.*

@OptIn(ExperimentalUnsignedTypes::class)
data class Packet(
    val opCode: ECOpCode = ECOpCode.EC_OP_NOOP,
    val tags: List<Tag<*>> = mutableListOf(),
    val flags: Flags,
    val accepts: Flags? = null,
    val id: UInt? = null,
) {
    fun byte(ecTagName: ECTagName): UByteTag = tags.first { it.name == ecTagName } as UByteTag
    fun short(ecTagName: ECTagName): UShortTag = tags.first { it.name == ecTagName } as UShortTag
    fun int(ecTagName: ECTagName): UIntTag = tags.first { it.name == ecTagName } as UIntTag
    fun long(ecTagName: ECTagName): ULongTag = tags.first { it.name == ecTagName } as ULongTag
    fun bigint(ecTagName: ECTagName): UInt128Tag = tags.first { it.name == ecTagName } as UInt128Tag
    fun string(ecTagName: ECTagName): StringTag = tags.first { it.name == ecTagName } as StringTag
    fun double(ecTagName: ECTagName): DoubleTag = tags.first { it.name == ecTagName } as DoubleTag
    fun ipv4(ecTagName: ECTagName): Ipv4Tag = tags.first { it.name == ecTagName } as Ipv4Tag
    fun hash16(ecTagName: ECTagName): Hash16Tag = tags.first { it.name == ecTagName } as Hash16Tag
    fun custom(ecTagName: ECTagName): CustomTag = tags.first { it.name == ecTagName } as CustomTag
}
