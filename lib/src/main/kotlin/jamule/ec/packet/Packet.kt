package jamule.ec.packet

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.tag.*

@OptIn(ExperimentalUnsignedTypes::class)
data class Packet(
    val opCode: ECOpCode = ECOpCode.EC_OP_NOOP,
    val tags: List<Tag<*>> = mutableListOf(),
    val flags: Flags = Flags(),
    val accepts: Flags? = null,
    val id: UInt? = null,
) {

    fun asLong(ecTagName: ECTagName): Long? = tags.firstOrNull { it.name == ecTagName }?.let {
        when (it) {
            is UByteTag -> it.getValue().toLong()
            is UShortTag -> it.getValue().toLong()
            is UIntTag -> it.getValue().toLong()
            is ULongTag -> it.getValue().toLong()
            else -> throw IllegalArgumentException("Tag $ecTagName is not a number")
        }
    }

    fun byte(ecTagName: ECTagName): UByteTag? = tags.firstOrNull { it.name == ecTagName } as? UByteTag

    fun short(ecTagName: ECTagName): UShortTag? = tags.firstOrNull { it.name == ecTagName } as? UShortTag

    fun int(ecTagName: ECTagName): UIntTag? = tags.firstOrNull { it.name == ecTagName } as? UIntTag

    fun long(ecTagName: ECTagName): ULongTag? = tags.firstOrNull { it.name == ecTagName } as? ULongTag

    fun bigint(ecTagName: ECTagName): UInt128Tag? = tags.firstOrNull { it.name == ecTagName } as? UInt128Tag

    fun string(ecTagName: ECTagName): StringTag? = tags.firstOrNull { it.name == ecTagName } as? StringTag

    fun double(ecTagName: ECTagName): DoubleTag? = tags.firstOrNull { it.name == ecTagName } as? DoubleTag

    fun ipv4(ecTagName: ECTagName): Ipv4Tag? = tags.firstOrNull { it.name == ecTagName } as? Ipv4Tag

    fun hash16(ecTagName: ECTagName): Hash16Tag? = tags.firstOrNull { it.name == ecTagName } as? Hash16Tag

    fun custom(ecTagName: ECTagName): CustomTag? = tags.firstOrNull { it.name == ecTagName } as? CustomTag

    fun byte(tagName: UShort): UByteTag? = tags.firstOrNull { it.nameValue == tagName } as? UByteTag

    fun short(tagName: UShort): UShortTag? = tags.firstOrNull { it.nameValue == tagName } as? UShortTag

    fun int(tagName: UShort): UIntTag? = tags.firstOrNull { it.nameValue == tagName } as? UIntTag

    fun long(tagName: UShort): ULongTag? = tags.firstOrNull { it.nameValue == tagName } as? ULongTag

    fun bigint(tagName: UShort): UInt128Tag? = tags.firstOrNull { it.nameValue == tagName } as? UInt128Tag

    fun string(tagName: UShort): StringTag? = tags.firstOrNull { it.nameValue == tagName } as? StringTag

    fun double(tagName: UShort): DoubleTag? = tags.firstOrNull { it.nameValue == tagName } as? DoubleTag

    fun ipv4(tagName: UShort): Ipv4Tag? = tags.firstOrNull { it.nameValue == tagName } as? Ipv4Tag

    fun hash16(tagName: UShort): Hash16Tag? = tags.firstOrNull { it.nameValue == tagName } as? Hash16Tag

    fun custom(tagName: UShort): CustomTag? = tags.firstOrNull { it.nameValue == tagName } as? CustomTag
}
