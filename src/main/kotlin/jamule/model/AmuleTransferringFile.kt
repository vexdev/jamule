package jamule.model

interface AmuleTransferringFile : AmuleFile {
    val partMetID: Short?
    val sizeXfer: Long?
    val sizeDone: Long?
    val fileStatus: FileStatus
    val stopped: Boolean
    val sourceCount: Short
    val sourceNotCurrCount: Short
    val sourceXferCount: Short
    val sourceCountA4AF: Short
    val speed: Long?
    val downPrio: Byte
    val fileCat: Long
    val lastSeenComplete: Long
    val lastDateChanged: Long
    val downloadActiveTime: Int
    val availablePartCount: Short
    val a4AFAuto: Boolean
    val hashingProgress: Boolean
    val getLostDueToCorruption: Long
    val getGainDueToCompression: Long
    val totalPacketsSavedDueToICH: Int
}