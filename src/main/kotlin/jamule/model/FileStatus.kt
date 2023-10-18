package jamule.model

/**
 * The names of the enum correspond to the value passed by amule when lowercased.
 */
enum class FileStatus(val value: UByte) {
    READY(0u),
    EMPTY(1u),
    WAITINGFORHASH(2u),
    HASHING(3u),
    ERROR(4u),
    INSUFFICIENT(5u),
    UNKNOWN(6u),
    PAUSED(7u),
    COMPLETING(8u),
    COMPLETE(9u),
    ALLOCATING(10u);

    companion object {
        internal fun fromValue(value: UByte) = entries.first { it.value == value }
    }
}