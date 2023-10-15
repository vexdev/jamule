package jamule.ec.tag

import jamule.ec.*
import java.math.BigInteger

@ExperimentalUnsignedTypes
sealed class Tag<T : Any>(
    open val name: ECTagName,
    val type: ECTagType,
    open val subtags: List<Tag<*>>,
) {
    private val nestingLevel: Int = 0
    private val subTags: List<Tag<*>> = emptyList()
    private lateinit var value: T

    abstract fun parseValue(value: UByteArray)

    fun getValue() = value

    fun setValue(value: T) {
        if (this::value.isInitialized) throw IllegalStateException("Tag value already set")
        this.value = value
    }

    abstract fun encodeValue(): UByteArray
}

@ExperimentalUnsignedTypes
data class CustomTag(override val name: ECTagName, override val subtags: List<Tag<out Any>> = listOf()) :
    Tag<UByteArray>(name, ECTagType.EC_TAGTYPE_CUSTOM, subtags) {

    constructor(name: ECTagName, value: UByteArray, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    override fun encodeValue(): UByteArray = getValue()

    override fun parseValue(value: UByteArray) {
        setValue(value)
    }
}

@ExperimentalUnsignedTypes
data class UByteTag(override val name: ECTagName, override val subtags: List<Tag<out Any>> = listOf()) :
    Tag<UByte>(name, ECTagType.EC_TAGTYPE_UINT8, subtags) {

    constructor(name: ECTagName, value: UByte, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    override fun encodeValue(): UByteArray = ubyteArrayOf(getValue())

    override fun parseValue(value: UByteArray) {
        if (value.isEmpty()) setValue(0u)
        else if (value.size == 1) setValue(value[0])
        else throw IllegalArgumentException("UInt8Tag value must be 1 byte long")
    }
}

@ExperimentalUnsignedTypes
data class UShortTag(override val name: ECTagName, override val subtags: List<Tag<out Any>> = listOf()) :
    Tag<UShort>(name, ECTagType.EC_TAGTYPE_UINT16, subtags) {

    constructor(name: ECTagName, value: UShort, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    override fun encodeValue(): UByteArray = getValue().toUByteArray()

    override fun parseValue(value: UByteArray) {
        if (value.isEmpty()) setValue(0u)
        else if (value.size == 2) setValue(value.readUint16(false, 0).toUShort())
        else throw IllegalArgumentException("UInt16Tag value must be 2 bytes long")
    }
}

@ExperimentalUnsignedTypes
data class UIntTag(override val name: ECTagName, override val subtags: List<Tag<out Any>> = listOf()) :
    Tag<UInt>(name, ECTagType.EC_TAGTYPE_UINT32, subtags) {

    constructor(name: ECTagName, value: UInt, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    override fun encodeValue(): UByteArray = getValue().toUByteArray()

    override fun parseValue(value: UByteArray) {
        if (value.isEmpty()) setValue(0u)
        else if (value.size == 4) setValue(value.readUInt32(false, 0))
        else throw IllegalArgumentException("UInt32Tag value must be 4 bytes long")
    }
}

@ExperimentalUnsignedTypes
data class ULongTag(override val name: ECTagName, override val subtags: List<Tag<out Any>> = listOf()) :
    Tag<ULong>(name, ECTagType.EC_TAGTYPE_UINT64, subtags) {

    constructor(name: ECTagName, value: ULong, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    override fun encodeValue(): UByteArray = getValue().toUByteArray()

    override fun parseValue(value: UByteArray) {
        if (value.isEmpty()) setValue(0u)
        else if (value.size == 8) setValue(value.toUInt64())
        else throw IllegalArgumentException("UInt64Tag value must be 8 bytes long")
    }
}

@ExperimentalUnsignedTypes
data class UInt128Tag(override val name: ECTagName, override val subtags: List<Tag<out Any>> = listOf()) :
    Tag<BigInteger>(name, ECTagType.EC_TAGTYPE_UINT128, subtags) {

    constructor(name: ECTagName, value: BigInteger, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    override fun encodeValue(): UByteArray = getValue().toByteArray().toUByteArray()

    override fun parseValue(value: UByteArray) {
        if (value.isEmpty()) setValue(BigInteger.ZERO)
        else setValue(BigInteger(value.toByteArray()))
    }
}

@ExperimentalUnsignedTypes
data class StringTag(override val name: ECTagName, override val subtags: List<Tag<out Any>> = listOf()) :
    Tag<String>(name, ECTagType.EC_TAGTYPE_STRING, subtags) {

    constructor(name: ECTagName, value: String, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    override fun encodeValue(): UByteArray = getValue().toByteArray().toUByteArray() + 0x00.toUByte()

    override fun parseValue(value: UByteArray) {
        if (value[value.size - 1] != 0x00.toUByte()) throw IllegalArgumentException("StringTag value must be null terminated")
        else setValue(value.toByteArray().decodeToString().trimEnd(0.toChar()))
    }
}

@ExperimentalUnsignedTypes
data class DoubleTag(override val name: ECTagName, override val subtags: List<Tag<out Any>> = listOf()) :
    Tag<Double>(name, ECTagType.EC_TAGTYPE_DOUBLE, subtags) {

    constructor(name: ECTagName, value: Double, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    override fun encodeValue(): UByteArray = getValue().toString().toByteArray().toUByteArray() + 0x00.toUByte()

    override fun parseValue(value: UByteArray) {
        if (value[value.size - 1] != 0x00.toUByte()) throw IllegalArgumentException("DoubleTag value must be null terminated")
        else setValue(value.toByteArray().decodeToString().trimEnd(0.toChar()).toDouble())
    }
}

@ExperimentalUnsignedTypes
data class Ipv4Tag(override val name: ECTagName, override val subtags: List<Tag<out Any>> = listOf()) :
    Tag<Ipv4Tag.Ipv4>(name, ECTagType.EC_TAGTYPE_IPV4, subtags) {

    constructor(name: ECTagName, value: Ipv4, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    override fun encodeValue(): UByteArray =
        getValue().address.split(".").map { it.toUByte() }.toUByteArray() + getValue().port.toUByteArray()

    override fun parseValue(value: UByteArray) {
        // IPV4 is 4 bytes long, the last 2 bytes are used for the port
        if (value.size != 6) throw IllegalArgumentException("Ipv4Tag value must be 6 bytes long")
        else setValue(
            Ipv4(
                "${value[0]}.${value[1]}.${value[2]}.${value[3]}",
                value.readUint16(false, 4)
            )
        )
    }

    data class Ipv4(val address: String, val port: UInt)
}

@ExperimentalUnsignedTypes
data class Hash16Tag(override val name: ECTagName, override val subtags: List<Tag<out Any>> = listOf()) :
    Tag<UByteArray>(name, ECTagType.EC_TAGTYPE_HASH16, subtags) {

    constructor(name: ECTagName, value: UByteArray, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    override fun encodeValue(): UByteArray = getValue()

    override fun parseValue(value: UByteArray) {
        if (value.size == 16) setValue(value)
        else throw IllegalArgumentException("Hash16Tag value must be 16 bytes long")
    }
}