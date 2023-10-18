package jamule.model

interface AmuleFile {
    val fileHashHexString: String?
    val fileName: String?
    val filePath: String?
    val sizeFull: Long?
    val fileEd2kLink: String?
    val upPrio: Byte
    val getRequests: Short
    val getAllRequests: Int
    val getAccepts: Short
    val getAllAccepts: Int
    val getXferred: Long
    val getAllXferred: Long
    val getCompleteSourcesLow: Short
    val getCompleteSourcesHigh: Short
    val getCompleteSources: Short
    val getOnQueue: Short
    val getComment: String?
    val getRating: Byte?
}