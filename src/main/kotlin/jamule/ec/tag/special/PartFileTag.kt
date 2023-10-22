package jamule.ec.tag.special

import jamule.ec.ECTagName.*
import jamule.ec.packet.Packet.Companion.byte
import jamule.ec.packet.Packet.Companion.numeric
import jamule.ec.tag.Tag
import jamule.model.AmuleFile
import jamule.model.AmuleTransferringFile
import jamule.model.FileStatus

internal data class PartFileTag(
    override val partMetID: Short?, // EC_TAG_PARTFILE_PARTMETID

    override val sizeXfer: Long?, // EC_TAG_PARTFILE_SIZE_XFER
    override val sizeDone: Long?, // EC_TAG_PARTFILE_SIZE_DONE
    override val fileStatus: FileStatus, // EC_TAG_PARTFILE_STATUS
    override val stopped: Boolean, // EC_TAG_PARTFILE_STOPPED
    override val sourceCount: Short, // EC_TAG_PARTFILE_SOURCE_COUNT
    override val sourceNotCurrCount: Short, // EC_TAG_PARTFILE_SOURCE_COUNT_NOT_CURRENT
    override val sourceXferCount: Short, // EC_TAG_PARTFILE_SOURCE_COUNT_XFER
    override val sourceCountA4AF: Short, // EC_TAG_PARTFILE_SOURCE_COUNT_A4AF
    override val speed: Long?, //? EC_TAG_PARTFILE_SPEED.getInt(),
    override val downPrio: Byte, // EC_TAG_PARTFILE_PRIO
    override val fileCat: Long, // EC_TAG_PARTFILE_CAT
    override val lastSeenComplete: Long, // EC_TAG_PARTFILE_LAST_SEEN_COMP
    override val lastDateChanged: Long, // EC_TAG_PARTFILE_LAST_RECV
    override val downloadActiveTime: Int, // EC_TAG_PARTFILE_DOWNLOAD_ACTIVE
    override val availablePartCount: Short, // EC_TAG_PARTFILE_AVAILABLE_PARTS
    override val a4AFAuto: Boolean, // EC_TAG_PARTFILE_A4AFAUTO
    override val hashingProgress: Boolean, // EC_TAG_PARTFILE_HASHED_PART_COUNT

    override val getLostDueToCorruption: Long, // EC_TAG_PARTFILE_LOST_CORRUPTION
    override val getGainDueToCompression: Long, // EC_TAG_PARTFILE_GAINED_COMPRESSION
    override val totalPacketsSavedDueToICH: Int, // EC_TAG_PARTFILE_SAVED_ICH

    // What follows is a beautiful example of composition with Kotlin *_*
    val sharedFileTag: SharedFileTag
) : AmuleFile by sharedFileTag, AmuleTransferringFile {
    companion object {
        fun fromSubtags(subtags: List<Tag<*>>) = PartFileTag(
            sharedFileTag = SharedFileTag.fromSubtags(subtags),

            partMetID = subtags.numeric(EC_TAG_PARTFILE_PARTMETID)?.getShort(),

            sizeXfer = subtags.numeric(EC_TAG_PARTFILE_SIZE_XFER)?.getLong(),
            sizeDone = subtags.numeric(EC_TAG_PARTFILE_SIZE_DONE)?.getLong(),
            fileStatus = subtags.byte(EC_TAG_PARTFILE_STATUS)!!.getValue().let { FileStatus.fromValue(it) },
            stopped = subtags.byte(EC_TAG_PARTFILE_STOPPED)!!.getValue() != UByte.MIN_VALUE,
            sourceCount = subtags.numeric(EC_TAG_PARTFILE_SOURCE_COUNT)!!.getShort(),
            sourceNotCurrCount = subtags.numeric(EC_TAG_PARTFILE_SOURCE_COUNT_NOT_CURRENT)!!.getShort(),
            sourceXferCount = subtags.numeric(EC_TAG_PARTFILE_SOURCE_COUNT_XFER)!!.getShort(),
            sourceCountA4AF = subtags.numeric(EC_TAG_PARTFILE_SOURCE_COUNT_A4AF)!!.getShort(),
            speed = subtags.numeric(EC_TAG_PARTFILE_SPEED)?.getLong(),
            downPrio = subtags.byte(EC_TAG_PARTFILE_PRIO)!!.getValue().toByte(),
            fileCat = subtags.numeric(EC_TAG_PARTFILE_CAT)!!.getLong(),
            lastSeenComplete = subtags.numeric(EC_TAG_PARTFILE_LAST_SEEN_COMP)!!.getLong(),
            lastDateChanged = subtags.numeric(EC_TAG_PARTFILE_LAST_RECV)!!.getLong(),
            downloadActiveTime = subtags.numeric(EC_TAG_PARTFILE_DOWNLOAD_ACTIVE)!!.getInt(),
            availablePartCount = subtags.numeric(EC_TAG_PARTFILE_AVAILABLE_PARTS)!!.getShort(),
            a4AFAuto = subtags.byte(EC_TAG_PARTFILE_A4AFAUTO)!!.getValue() != UByte.MIN_VALUE,
            hashingProgress = subtags.byte(EC_TAG_PARTFILE_HASHED_PART_COUNT)!!.getValue() != UByte.MIN_VALUE,

            getLostDueToCorruption = subtags.numeric(EC_TAG_PARTFILE_LOST_CORRUPTION)!!.getLong(),
            getGainDueToCompression = subtags.numeric(EC_TAG_PARTFILE_GAINED_COMPRESSION)!!.getLong(),
            totalPacketsSavedDueToICH = subtags.numeric(EC_TAG_PARTFILE_SAVED_ICH)!!.getInt(),
        )
    }
}