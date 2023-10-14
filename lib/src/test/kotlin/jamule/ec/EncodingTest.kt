package jamule.ec

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EncodingTest {

    @Test
    fun `decodes utf8 value`() {
        val encoded = byteArrayOf(0x01)

        val result = encoded.readUtf8Number(0)

        assertEquals(1L, result)
    }

    @Test
    fun `decodes and re-encodes same value`() {
        val encoded = 1u.toUShort().toByteArray(true)

        val result = encoded.readUtf8Number(0)

        assertEquals(1L, result)
    }

}