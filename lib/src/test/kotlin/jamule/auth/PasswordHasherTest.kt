package jamule.auth

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

@OptIn(ExperimentalStdlibApi::class, ExperimentalUnsignedTypes::class)
class PasswordHasherTest : FunSpec({

    test("should hash simple password") {
        val salt = "55099a4aea510c43".hexToULong()
        val password = "amule"
        val expectedHash = "ca9026415e1a7df7ec0f7ec69678c150".hexToUByteArray()
        PasswordHasher.hash(password, salt) shouldBe expectedHash
    }

})
