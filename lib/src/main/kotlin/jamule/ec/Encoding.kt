package jamule.ec

@OptIn(ExperimentalUnsignedTypes::class)
fun UShort.toUByteArray(): UByteArray =
    ubyteArrayOf(
        (this.toUInt() shr 8).toUByte(),
        this.toUByte()
    )

@OptIn(ExperimentalUnsignedTypes::class)
fun UInt.toUByteArray(): UByteArray =
    ubyteArrayOf(
        (this shr 24).toUByte(),
        (this shr 16).toUByte(),
        (this shr 8).toUByte(),
        this.toUByte()
    )

@OptIn(ExperimentalUnsignedTypes::class)
fun ULong.toUByteArray(): UByteArray =
    ubyteArrayOf(
        (this shr 56).toUByte(),
        (this shr 48).toUByte(),
        (this shr 40).toUByte(),
        (this shr 32).toUByte(),
        (this shr 24).toUByte(),
        (this shr 16).toUByte(),
        (this shr 8).toUByte(),
        this.toUByte()
    )

@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.toUInt64(): ULong =
    (((this[0].toLong() and 0xFF) shl 56) or
            ((this[1].toLong() and 0xFF) shl 48) or
            ((this[2].toLong() and 0xFF) shl 40) or
            ((this[3].toLong() and 0xFF) shl 32) or
            ((this[4].toLong() and 0xFF) shl 24) or
            ((this[5].toLong() and 0xFF) shl 16) or
            ((this[6].toLong() and 0xFF) shl 8) or
            (this[7].toLong() and 0xFF)).toULong()

@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.readUInt32(utf: Boolean, index: Int): UInt = if (!utf) {
    this.sliceArray(index..<index + LEN_UINT).toUInt32()
} else {
    this.readUtf8Number(index).toUInt()
}

@OptIn(ExperimentalUnsignedTypes::class)
private fun UByteArray.toUInt32(): UInt =
    (((this[0].toInt() and 0xFF) shl 24) or
            ((this[1].toInt() and 0xFF) shl 16) or
            ((this[2].toInt() and 0xFF) shl 8) or
            (this[3].toInt() and 0xFF)).toUInt()

@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.readUint16(utf: Boolean, index: Int): UInt = if (!utf) {
    this.sliceArray(index..<index + LEN_USHORT).toUInt16()
} else {
    this.readUtf8Number(index).toUInt()
}

@OptIn(ExperimentalUnsignedTypes::class)
private fun UByteArray.toUInt16(): UInt =
    (((this[0].toInt() and 0xFF) shl 8) or
            (this[1].toInt() and 0xFF)).toUInt()

fun UByte.numberLength(utf: Boolean, size: Int): Int =
    if (utf) this.utf8SequenceLength() else size

fun UByte.utf8SequenceLength(): Int {
    var length = 1 // at least 1 byte
    if (this.toInt() and 0x80 == 0) {
        // ASCII character, 1 byte
        return length
    }

    // Check the number of leading 1 bit to determine sequence length
    var mask = 0x40 // start with the second most significant bit
    while (this.toInt() and mask != 0) {
        length++
        mask = mask shr 1
    }

    // Validate length
    if (length < 2 || length > 4) {
        throw IllegalArgumentException("Invalid UTF-8 first byte: $this")
    }

    return length
}

@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.readUtf8Number(offset: Int): Long {
    val str = String(this.toByteArray(), offset, this.toByteArray().size - offset, Charsets.UTF_8)
    return str.codePointAt(0).toLong()
}

@OptIn(ExperimentalUnsignedTypes::class)
fun UShort.toUByteArray(utf: Boolean): UByteArray =
    if (!utf) {
        this.toUByteArray()
    } else {
        this.toULong().toUtf8ByteArray()
    }

@OptIn(ExperimentalUnsignedTypes::class)
fun UInt.toUByteArray(utf: Boolean): UByteArray =
    if (!utf) {
        this.toUByteArray()
    } else {
        this.toULong().toUtf8ByteArray()
    }

@OptIn(ExperimentalUnsignedTypes::class)
fun ULong.toUtf8ByteArray(): UByteArray {
    val str = String(intArrayOf(this.toInt()), 0, 1)
    return str.toByteArray(Charsets.UTF_8).toUByteArray()
}