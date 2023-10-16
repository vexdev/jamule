package jamule.ec.packet

import jamule.ec.ECOpCode
import jamule.ec.ECTagName
import jamule.ec.tag.*

@OptIn(ExperimentalUnsignedTypes::class)
internal data class Packet(
    val opCode: ECOpCode = ECOpCode.EC_OP_NOOP,
    val tags: List<Tag<*>> = mutableListOf(),
    val flags: Flags = Flags(),
    val accepts: Flags? = null,
    val id: UInt? = null,
) {

    fun byte(ecTagName: ECTagName): UByteTag? = tags.byte(ecTagName)
    fun short(ecTagName: ECTagName): UShortTag? = tags.short(ecTagName)
    fun int(ecTagName: ECTagName): UIntTag? = tags.int(ecTagName)
    fun long(ecTagName: ECTagName): ULongTag? = tags.long(ecTagName)
    fun bigint(ecTagName: ECTagName): UInt128Tag? = tags.bigint(ecTagName)
    fun string(ecTagName: ECTagName): StringTag? = tags.string(ecTagName)
    fun double(ecTagName: ECTagName): DoubleTag? = tags.double(ecTagName)
    fun ipv4(ecTagName: ECTagName): Ipv4Tag? = tags.ipv4(ecTagName)
    fun hash16(ecTagName: ECTagName): Hash16Tag? = tags.hash16(ecTagName)
    fun custom(ecTagName: ECTagName): CustomTag? = tags.custom(ecTagName)
    fun numeric(ecTagName: ECTagName): NumericTag? = tags.numeric(ecTagName)

    fun byte(tagName: UShort): UByteTag? = tags.byte(tagName)
    fun short(tagName: UShort): UShortTag? = tags.short(tagName)
    fun int(tagName: UShort): UIntTag? = tags.int(tagName)
    fun long(tagName: UShort): ULongTag? = tags.long(tagName)
    fun bigint(tagName: UShort): UInt128Tag? = tags.bigint(tagName)
    fun string(tagName: UShort): StringTag? = tags.string(tagName)
    fun double(tagName: UShort): DoubleTag? = tags.double(tagName)
    fun ipv4(tagName: UShort): Ipv4Tag? = tags.ipv4(tagName)
    fun hash16(tagName: UShort): Hash16Tag? = tags.hash16(tagName)
    fun custom(tagName: UShort): CustomTag? = tags.custom(tagName)
    fun numeric(tagName: UShort): NumericTag? = tags.numeric(tagName)

    companion object {
        fun Tag<*>.byte() = this as? UByteTag
        fun Tag<*>.short() = this as? UShortTag
        fun Tag<*>.int() = this as? UIntTag
        fun Tag<*>.long() = this as? ULongTag
        fun Tag<*>.bigint() = this as? UInt128Tag
        fun Tag<*>.string() = this as? StringTag
        fun Tag<*>.double() = this as? DoubleTag
        fun Tag<*>.ipv4() = this as? Ipv4Tag
        fun Tag<*>.hash16() = this as? Hash16Tag
        fun Tag<*>.custom() = this as? CustomTag
        fun Tag<*>.numeric() = this as? NumericTag

        fun Iterable<Tag<*>>.byte(ecTagName: ECTagName): UByteTag? = firstOrNull { it.name == ecTagName }?.byte()
        fun Iterable<Tag<*>>.short(ecTagName: ECTagName): UShortTag? = firstOrNull { it.name == ecTagName }?.short()
        fun Iterable<Tag<*>>.int(ecTagName: ECTagName): UIntTag? = firstOrNull { it.name == ecTagName }?.int()
        fun Iterable<Tag<*>>.long(ecTagName: ECTagName): ULongTag? = firstOrNull { it.name == ecTagName }?.long()
        fun Iterable<Tag<*>>.bigint(ecTagName: ECTagName): UInt128Tag? = firstOrNull { it.name == ecTagName }?.bigint()
        fun Iterable<Tag<*>>.string(ecTagName: ECTagName): StringTag? = firstOrNull { it.name == ecTagName }?.string()
        fun Iterable<Tag<*>>.double(ecTagName: ECTagName): DoubleTag? = firstOrNull { it.name == ecTagName }?.double()
        fun Iterable<Tag<*>>.ipv4(ecTagName: ECTagName): Ipv4Tag? = firstOrNull { it.name == ecTagName }?.ipv4()
        fun Iterable<Tag<*>>.hash16(ecTagName: ECTagName): Hash16Tag? = firstOrNull { it.name == ecTagName }?.hash16()
        fun Iterable<Tag<*>>.custom(ecTagName: ECTagName): CustomTag? = firstOrNull { it.name == ecTagName }?.custom()
        fun Iterable<Tag<*>>.numeric(ecTagName: ECTagName): NumericTag? =
            firstOrNull { it.name == ecTagName }?.numeric()

        fun Iterable<Tag<*>>.byte(tagName: UShort): UByteTag? = firstOrNull { it.nameValue == tagName }?.byte()
        fun Iterable<Tag<*>>.short(tagName: UShort): UShortTag? = firstOrNull { it.nameValue == tagName }?.short()
        fun Iterable<Tag<*>>.int(tagName: UShort): UIntTag? = firstOrNull { it.nameValue == tagName }?.int()
        fun Iterable<Tag<*>>.long(tagName: UShort): ULongTag? = firstOrNull { it.nameValue == tagName }?.long()
        fun Iterable<Tag<*>>.bigint(tagName: UShort): UInt128Tag? = firstOrNull { it.nameValue == tagName }?.bigint()
        fun Iterable<Tag<*>>.string(tagName: UShort): StringTag? = firstOrNull { it.nameValue == tagName }?.string()
        fun Iterable<Tag<*>>.double(tagName: UShort): DoubleTag? = firstOrNull { it.nameValue == tagName }?.double()
        fun Iterable<Tag<*>>.ipv4(tagName: UShort): Ipv4Tag? = firstOrNull { it.nameValue == tagName }?.ipv4()
        fun Iterable<Tag<*>>.hash16(tagName: UShort): Hash16Tag? = firstOrNull { it.nameValue == tagName }?.hash16()
        fun Iterable<Tag<*>>.custom(tagName: UShort): CustomTag? = firstOrNull { it.nameValue == tagName }?.custom()
        fun Iterable<Tag<*>>.numeric(tagName: UShort): NumericTag? = firstOrNull { it.nameValue == tagName }?.numeric()
    }
}
