package jamule.auth

import java.security.MessageDigest

object PasswordHasher {

    @ExperimentalUnsignedTypes
    @OptIn(ExperimentalStdlibApi::class)
    fun hash(password: String, salt: ULong): UByteArray {
        val md = MessageDigest.getInstance("MD5")
        val saltHash = md.digest(salt.toHexString().uppercase().toByteArray())
        md.reset()
        val passwordHash = md.digest(password.toByteArray())
        md.reset()
        md.update(passwordHash.toHexString().lowercase().toByteArray())
        md.update(saltHash.toHexString().lowercase().toByteArray())
        return md.digest().toUByteArray()
    }

}