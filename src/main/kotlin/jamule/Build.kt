package jamule

import java.util.*

object Build {
    private val buildProperties by lazy {
        Properties().also {
            it.load(this.javaClass.getResourceAsStream("/build.properties"))
        }
    }

    val version by lazy {
        buildProperties.getProperty("version") ?: "no version"
    }
}