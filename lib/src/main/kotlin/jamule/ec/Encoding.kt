package jamule.ec

fun UShort.toByteArray(): ByteArray =
    byteArrayOf(
        (this.toUInt() shr 8).toByte(),
        this.toByte()
    )

fun UInt.toByteArray(): ByteArray =
    byteArrayOf(
        (this shr 24).toByte(),
        (this shr 16).toByte(),
        (this shr 8).toByte(),
        this.toByte()
    )

fun ULong.toByteArray(): ByteArray =
    byteArrayOf(
        (this shr 56).toByte(),
        (this shr 48).toByte(),
        (this shr 40).toByte(),
        (this shr 32).toByte(),
        (this shr 24).toByte(),
        (this shr 16).toByte(),
        (this shr 8).toByte(),
        this.toByte()
    )

fun ByteArray.toUInt64(): ULong =
    (((this[0].toLong() and 0xFF) shl 56) or
            ((this[1].toLong() and 0xFF) shl 48) or
            ((this[2].toLong() and 0xFF) shl 40) or
            ((this[3].toLong() and 0xFF) shl 32) or
            ((this[4].toLong() and 0xFF) shl 24) or
            ((this[5].toLong() and 0xFF) shl 16) or
            ((this[6].toLong() and 0xFF) shl 8) or
            (this[7].toLong() and 0xFF)).toULong()

fun ByteArray.readUInt32(utf: Boolean, index: Int): UInt = if (!utf) {
    this.sliceArray(index..<index + LEN_UINT).toUInt32()
} else {
    this.readUtf8Number(index).toUInt()
}

private fun ByteArray.toUInt32(): UInt =
    (((this[0].toInt() and 0xFF) shl 24) or
            ((this[1].toInt() and 0xFF) shl 16) or
            ((this[2].toInt() and 0xFF) shl 8) or
            (this[3].toInt() and 0xFF)).toUInt()

fun ByteArray.readUint16(utf: Boolean, index: Int): UInt = if (!utf) {
    this.sliceArray(index..<index + LEN_USHORT).toUInt16()
} else {
    this.readUtf8Number(index).toUInt()
}

private fun ByteArray.toUInt16(): UInt =
    (((this[0].toInt() and 0xFF) shl 8) or
            (this[1].toInt() and 0xFF)).toUInt()

fun Byte.numberLength(utf: Boolean, size: Int): Int =
    if (utf) this.utf8SequenceLength() else size

fun Byte.utf8SequenceLength(): Int {
    var length = 1 // at least 1 byte
    if (this.toInt() and 0x80 == 0) {
        // ASCII character, 1 byte
        return length
    }

    // Check the number of leading 1 bits to determine sequence length
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

fun ByteArray.readUtf8Number(offset: Int): Long {
    val len = this[offset].utf8SequenceLength()
    val bitShift = if (len == 1) 7 else 7 - len
    val decodedBitLen = (len - 1) * 6 + bitShift
    var result = 0L
    for (i in 0..<decodedBitLen) {
        val encodedByte = if (i < bitShift) 0 else (i - bitShift) / 6 + 1
        val encodedBit = if (encodedByte == 0) 8 - bitShift + i else (i - bitShift) % 6 + 2
        if (encodedByte > 0 && this[offset + encodedByte].toInt() shr 6 and 0x3 != 0x2)
            throw InvalidECException("Invalid UTF-8 sequence")
        val bitValue = this[offset + encodedByte].toInt() shr 7 - encodedBit and 0x01
        result = result or (bitValue shl decodedBitLen - i - 1).toLong()
    }
    return result
}

fun UShort.toByteArray(utf: Boolean): ByteArray =
    if (!utf) {
        this.toByteArray()
    } else {
        this.toLong().toUtf8ByteArray()
    }

fun UInt.toByteArray(utf: Boolean): ByteArray =
    if (!utf) {
        this.toByteArray()
    } else {
        this.toLong().toUtf8ByteArray()
    }

fun Long.toUtf8ByteArray(): ByteArray {
    // Determine the number of bits needed to represent the Long
    val numBits = 64 - this.countLeadingZeroBits()

    // Calculate the number of bytes required
    val numBytes = when {
        numBits <= 7 -> 1
        numBits <= 11 -> 2
        numBits <= 16 -> 3
        else -> 4
    }

    // Create a ByteArray to store the result
    val result = ByteArray(numBytes)

    // Initialize mask and shift variables
    var mask: Int
    var shift: Int

    // Calculate the first byte
    if (numBytes == 1) {
        result[0] = this.toByte()
        return result
    } else {
        mask = 0xFF shl (8 - numBytes - 1)
        shift = (numBytes - 1) * 6 + (8 - numBytes - 1)
        result[0] = ((this shr shift and mask.toLong()) or (0xFFL shl (8 - numBytes))).toByte()
    }

    // Fill in the remaining bytes
    for (i in 1..<numBytes) {
        shift -= 6
        mask = 0x3F
        result[i] = ((this shr shift and mask.toLong()) or 0x80).toByte()
    }

    return result
}