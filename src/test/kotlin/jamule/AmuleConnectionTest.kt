package jamule

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jamule.request.StatsRequest
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.OutputStream
import java.net.Socket
import java.util.concurrent.CountDownLatch

@OptIn(ExperimentalStdlibApi::class)
class AmuleConnectionTest : FunSpec({

    val socket = mockk<Socket>()
    val logger = LoggerFactory.getLogger(this::class.java)
    val outputStream = OutputStream.nullOutputStream()
    every { socket.getOutputStream() } returns outputStream
    every { socket.close() } returns Unit
    val authSaltResponse = ByteArrayInputStream("000000220000000d4f0116050855099a4aea510c43".hexToByteArray())
    val authOkResponse =
        ByteArrayInputStream("000000220000001d0401e0a8960616322e332e31204164756e616e7a4120323031322e3100".hexToByteArray())
    val statusResponse = ByteArrayInputStream(
        ("000000220000008c0c10d08003021664d082020100d484020100d4860302" +
                "1664d488020100d48a020100d084020100d086020100d09002010" +
                "0d08c020100d092040400017cbbd09402010ad096040402e2740f" +
                "d09803020438d0b60201000b023f03e0a881081f01e0a88206124" +
                "16b74656f6e20536572766572204e6f3200b07de76247b50c0404" +
                "1d4e48541404041d4e485419")
            .hexToByteArray()
    )

    test("single request works ok") {
        val amule = AmuleConnection({ socket }, "password", logger)
        every { socket.getInputStream() } returnsMany listOf(
            authSaltResponse,
            authOkResponse,
            statusResponse
        )
        amule.sendRequest(StatsRequest())
        // Called 3 times: 1 for salt, 1 for auth, 1 for stats
        verify(exactly = 3) { socket.getOutputStream() }
    }

    test("multiple parallel requests are synchronised") {
        val amule = AmuleConnection({ socket }, "password", logger)
        val firstRequestArrivedLatch = CountDownLatch(1)
        val firstRequestLatch = CountDownLatch(1)
        val secondRequestLatch = CountDownLatch(1)
        var requestCount = 0
        every { socket.getInputStream() } answers {
            when (requestCount++) {
                0 -> authSaltResponse
                1 -> authOkResponse
                2 -> {
                    firstRequestArrivedLatch.countDown()
                    firstRequestLatch.await()
                    statusResponse
                }

                3 -> {
                    secondRequestLatch.await()
                    statusResponse
                }

                else -> throw IllegalStateException("Unexpected request count: $requestCount")
            }.also { requestCount++ }
        }
        // Send two requests from two separate threads
        Thread { amule.sendRequest(StatsRequest()) }.start()
        Thread { amule.sendRequest(StatsRequest()) }.start()
        // Wait for the first request to arrive
        firstRequestArrivedLatch.await()
        Thread.sleep(50) // Allow for the second request to arrive if it's not synchronised
        requestCount shouldBe 3
        firstRequestLatch.countDown()
        secondRequestLatch.countDown()
    }

})
