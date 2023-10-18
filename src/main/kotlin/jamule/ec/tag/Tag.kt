package jamule.ec.tag

import jamule.ec.*
import java.math.BigInteger

interface NumericTag {
    /**
     * Gets a tag that can be any size up to a Short
     */
    fun getShort(): Short

    /**
     * Gets a tag that can be any size up to an Int
     */
    fun getInt(): Int

    /**
     * Gets a tag that can be any size up to a Long
     */
    fun getLong(): Long
}

sealed class Tag<T : Any>(
    open val name: ECTagName,
    val type: ECTagType,
    open val subtags: List<Tag<*>>,
    open val nameValue: UShort,
) {
    private val nestingLevel: Int = 0
    private val subTags: List<Tag<*>> = emptyList()
    private lateinit var value: T

    @OptIn(ExperimentalUnsignedTypes::class)
    abstract fun parseValue(value: UByteArray)

    fun getValue() = value

    fun setValue(value: T) {
        if (this::value.isInitialized) throw IllegalStateException("Tag value already set")
        this.value = value
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    abstract fun encodeValue(): UByteArray
}

@ExperimentalUnsignedTypes
data class CustomTag(
    override val name: ECTagName,
    override val subtags: List<Tag<out Any>> = listOf(),
    override val nameValue: UShort = name.value
) :
    Tag<UByteArray>(name, ECTagType.EC_TAGTYPE_CUSTOM, subtags, nameValue) {

    constructor(name: ECTagName, value: UByteArray, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    @ExperimentalUnsignedTypes
    override fun encodeValue(): UByteArray = getValue()

    @ExperimentalUnsignedTypes
    override fun parseValue(value: UByteArray) {
        setValue(value)
    }
}

data class UByteTag(
    override val name: ECTagName,
    override val subtags: List<Tag<out Any>> = listOf(),
    override val nameValue: UShort = name.value
) : Tag<UByte>(name, ECTagType.EC_TAGTYPE_UINT8, subtags, nameValue),
    NumericTag {

    constructor(name: ECTagName, value: UByte, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }


    @ExperimentalUnsignedTypes
    override fun encodeValue(): UByteArray = ubyteArrayOf(getValue())


    @ExperimentalUnsignedTypes
    override fun parseValue(value: UByteArray) {
        if (value.isEmpty()) setValue(0u)
        else if (value.size == 1) setValue(value[0])
        else throw IllegalArgumentException("UInt8Tag value must be 1 byte long")
    }

    override fun getShort(): Short = getValue().toShort()
    override fun getInt(): Int = getValue().toInt()
    override fun getLong(): Long = getValue().toLong()
}

data class UShortTag(
    override val name: ECTagName,
    override val subtags: List<Tag<out Any>> = listOf(),
    override val nameValue: UShort = name.value
) : Tag<UShort>(name, ECTagType.EC_TAGTYPE_UINT16, subtags, nameValue),
    NumericTag {

    constructor(name: ECTagName, value: UShort, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }


    @ExperimentalUnsignedTypes
    override fun encodeValue(): UByteArray = getValue().toUByteArray()


    @ExperimentalUnsignedTypes
    override fun parseValue(value: UByteArray) {
        if (value.isEmpty()) setValue(0u)
        else if (value.size == 2) setValue(value.readUint16(false, 0).toUShort())
        else throw IllegalArgumentException("UInt16Tag value must be 2 bytes long")
    }

    override fun getShort(): Short = getValue().toShort()
    override fun getInt(): Int = getValue().toInt()
    override fun getLong(): Long = getValue().toLong()
}

data class UIntTag(
    override val name: ECTagName,
    override val subtags: List<Tag<out Any>> = listOf(),
    override val nameValue: UShort = name.value
) : Tag<UInt>(name, ECTagType.EC_TAGTYPE_UINT32, subtags, nameValue),
    NumericTag {

    constructor(name: ECTagName, value: UInt, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }


    @ExperimentalUnsignedTypes
    override fun encodeValue(): UByteArray = getValue().toUByteArray()


    @ExperimentalUnsignedTypes
    override fun parseValue(value: UByteArray) {
        if (value.isEmpty()) setValue(0u)
        else if (value.size == 4) setValue(value.readUInt32(false, 0))
        else throw IllegalArgumentException("UInt32Tag value must be 4 bytes long")
    }

    override fun getShort(): Short = throw IllegalStateException("Unsigned Integer cannot be cast to short")
    override fun getInt(): Int = getValue().toInt()
    override fun getLong(): Long = getValue().toLong()
}

data class ULongTag(
    override val name: ECTagName,
    override val subtags: List<Tag<out Any>> = listOf(),
    override val nameValue: UShort = name.value
) :
    Tag<ULong>(name, ECTagType.EC_TAGTYPE_UINT64, subtags, nameValue),
    NumericTag {

    constructor(name: ECTagName, value: ULong, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    @ExperimentalUnsignedTypes
    override fun encodeValue(): UByteArray = getValue().toUByteArray()

    @ExperimentalUnsignedTypes
    override fun parseValue(value: UByteArray) {
        if (value.isEmpty()) setValue(0u)
        else if (value.size == 8) setValue(value.toUInt64())
        else throw IllegalArgumentException("UInt64Tag value must be 8 bytes long")
    }

    override fun getShort(): Short = throw IllegalStateException("Unsigned Long cannot be cast to short")
    override fun getInt(): Int = throw IllegalStateException("Unsigned Long cannot be cast to int")
    override fun getLong(): Long = getValue().toLong()
}

data class UInt128Tag(
    override val name: ECTagName,
    override val subtags: List<Tag<out Any>> = listOf(),
    override val nameValue: UShort = name.value
) :
    Tag<BigInteger>(name, ECTagType.EC_TAGTYPE_UINT128, subtags, nameValue) {

    constructor(name: ECTagName, value: BigInteger, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    @ExperimentalUnsignedTypes
    override fun encodeValue(): UByteArray = getValue().toByteArray().toUByteArray()

    @ExperimentalUnsignedTypes
    override fun parseValue(value: UByteArray) {
        if (value.isEmpty()) setValue(BigInteger.ZERO)
        else setValue(BigInteger(value.toByteArray()))
    }
}

data class StringTag(
    override val name: ECTagName,
    override val subtags: List<Tag<out Any>> = listOf(),
    override val nameValue: UShort = name.value
) :
    Tag<String>(name, ECTagType.EC_TAGTYPE_STRING, subtags, nameValue) {

    constructor(name: ECTagName, value: String, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    @ExperimentalUnsignedTypes
    override fun encodeValue(): UByteArray = getValue().toByteArray().toUByteArray() + 0x00.toUByte()

    @ExperimentalUnsignedTypes
    override fun parseValue(value: UByteArray) {
        if (value[value.size - 1] != 0x00.toUByte()) throw IllegalArgumentException("StringTag value must be null terminated")
        else setValue(value.toByteArray().decodeToString().trimEnd(0.toChar()))
    }
}

data class DoubleTag(
    override val name: ECTagName,
    override val subtags: List<Tag<out Any>> = listOf(),
    override val nameValue: UShort = name.value
) :
    Tag<Double>(name, ECTagType.EC_TAGTYPE_DOUBLE, subtags, nameValue) {

    constructor(name: ECTagName, value: Double, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    @ExperimentalUnsignedTypes
    override fun encodeValue(): UByteArray = getValue().toString().toByteArray().toUByteArray() + 0x00.toUByte()

    @ExperimentalUnsignedTypes
    override fun parseValue(value: UByteArray) {
        if (value[value.size - 1] != 0x00.toUByte()) throw IllegalArgumentException("DoubleTag value must be null terminated")
        else setValue(value.toByteArray().decodeToString().trimEnd(0.toChar()).toDouble())
    }
}

data class Ipv4Tag(
    override val name: ECTagName,
    override val subtags: List<Tag<out Any>> = listOf(),
    override val nameValue: UShort = name.value
) :
    Tag<Ipv4Tag.Ipv4>(name, ECTagType.EC_TAGTYPE_IPV4, subtags, nameValue) {

    constructor(name: ECTagName, value: Ipv4, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    @ExperimentalUnsignedTypes
    override fun encodeValue(): UByteArray =
        getValue().address.split(".").map { it.toUByte() }.toUByteArray() + getValue().port.toUByteArray()

    @ExperimentalUnsignedTypes
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
data class Hash16Tag(
    override val name: ECTagName,
    override val subtags: List<Tag<out Any>> = listOf(),
    override val nameValue: UShort = name.value
) :
    Tag<UByteArray>(name, ECTagType.EC_TAGTYPE_HASH16, subtags, nameValue) {

    constructor(name: ECTagName, value: UByteArray, subtags: List<Tag<out Any>> = listOf()) : this(name, subtags) {
        setValue(value)
    }

    @ExperimentalUnsignedTypes
    override fun encodeValue(): UByteArray = getValue()

    @ExperimentalUnsignedTypes
    override fun parseValue(value: UByteArray) {
        if (value.size == 16) setValue(value)
        else throw IllegalArgumentException("Hash16Tag value must be 16 bytes long")
    }
}