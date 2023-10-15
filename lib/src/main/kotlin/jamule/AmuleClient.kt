import jamule.AmuleConnection
import jamule.request.AuthRequest
import org.slf4j.Logger
import org.slf4j.helpers.NOPLogger
import java.net.Socket

class AmuleClient(
    ipv4: String,
    port: Int,
    private val timeout: Int = 0,
    private val logger: Logger = NOPLogger.NOP_LOGGER
) : AutoCloseable {
    private val socket: Socket = Socket(ipv4, port).apply { soTimeout = timeout }
    private val amuleConnection = AmuleConnection(socket, logger)

    override fun close() = amuleConnection.close()

    fun authenticate(password: String) {
        logger.info("Authenticating...")
        val response = amuleConnection.sendRequest(AuthRequest())
        logger.info("Got response: $response")
    }

    companion object {
        const val CLIENT_NAME = "jAmule"
    }

}