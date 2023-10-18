package jamule.ec.packet

import jamule.ec.ECFlag

data class Flags(
    val zlibCompressed: Boolean = false,
    val utf8: Boolean = true,
    val hasId: Boolean = false,
    val accepts: Boolean = false,
) {

    fun toUInt(): UInt {
        // Bit 5 is Always set to 1, to distinguish from older (pre-rc8) clients.
        var flags = 0x20u
        if (zlibCompressed) flags = flags or ECFlag.EC_FLAG_ZLIB.value
        if (utf8) flags = flags or ECFlag.EC_FLAG_UTF8_NUMBERS.value
        if (hasId) flags = flags or ECFlag.EC_FLAG_HAS_ID.value
        if (accepts) flags = flags or ECFlag.EC_FLAG_ACCEPTS.value
        return flags
    }

    companion object {
        fun fromUInt(flags: UInt): Flags {
            return Flags(
                zlibCompressed = flags and ECFlag.EC_FLAG_ZLIB.value != 0u,
                utf8 = flags and ECFlag.EC_FLAG_UTF8_NUMBERS.value != 0u,
                hasId = flags and ECFlag.EC_FLAG_HAS_ID.value != 0u,
                accepts = flags and ECFlag.EC_FLAG_ACCEPTS.value != 0u,
            )
        }
    }
}