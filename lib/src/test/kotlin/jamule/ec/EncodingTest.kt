@file:OptIn(ExperimentalUnsignedTypes::class)

package jamule.ec

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EncodingTest {

    @Test
    fun `decodes utf8 value`() {
        val encoded = ubyteArrayOf(0x01u)

        val result = encoded.readUtf8Number(0)

        assertEquals(1L, result)
    }

    @Test
    fun `decodes and re-encodes same value`() {
        val encoded = 1u.toUShort().toUByteArray(true)

        val result = encoded.readUtf8Number(0)

        assertEquals(1L, result)
    }

}