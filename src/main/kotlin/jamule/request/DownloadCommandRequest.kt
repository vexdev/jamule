package jamule.request

import jamule.ec.ECTagName.EC_TAG_PARTFILE
import jamule.ec.packet.Packet
import jamule.ec.tag.Hash16Tag
import jamule.model.DownloadCommand

internal data class DownloadCommandRequest(
    val fileHash: ByteArray,
    val status: DownloadCommand,
) : Request {
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun packet(): Packet =
        Packet(
            status.value,
            listOf(
                Hash16Tag(EC_TAG_PARTFILE, fileHash.toUByteArray()),
            )
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DownloadCommandRequest

        if (!fileHash.contentEquals(other.fileHash)) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fileHash.contentHashCode()
        result = 31 * result + status.hashCode()
        return result
    }
}