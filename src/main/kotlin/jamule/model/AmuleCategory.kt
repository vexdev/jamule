package jamule.model

data class AmuleCategory(
    val id: Long,
    val name: String,
    val path: String = "",
    val comment: String = "",
    val color: Int = 0,
    val priority: Byte = 0,
)