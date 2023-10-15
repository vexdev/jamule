package jamule

import jamule.auth.PasswordHasher
import jamule.exception.AuthFailedException
import jamule.exception.CommunicationException
import jamule.request.AuthRequest
import jamule.request.SaltRequest
import jamule.request.StatsRequest
import jamule.response.AuthFailedResponse
import jamule.response.AuthOkResponse
import jamule.response.AuthSaltResponse
import jamule.response.StatsResponse
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

    @OptIn(ExperimentalUnsignedTypes::class)
    fun authenticate(password: String) {
        logger.info("Authenticating...")
        val authSalt = amuleConnection.sendRequest(SaltRequest())
        if (authSalt !is AuthSaltResponse) {
            throw CommunicationException("Unable to get auth salt")
        }
        val saltedPassword = PasswordHasher.hash(password, authSalt.salt)
        val authResponse = amuleConnection.sendRequest(AuthRequest(saltedPassword))
        if (authResponse !is AuthOkResponse) {
            if (authResponse is AuthFailedResponse)
                throw AuthFailedException("Auth failed: ${authResponse.reason}")
            else
                throw CommunicationException("Unable to authenticate")
        }
        logger.info("Authenticated with server version ${authResponse.version}")
    }

    fun getStats(): StatsResponse {
        logger.info("Getting stats...")
        val statsResponse = amuleConnection.sendRequest(StatsRequest())
        if (statsResponse !is StatsResponse) {
            throw CommunicationException("Unable to get stats")
        }
        logger.info("Stats: $statsResponse")
        return statsResponse
    }

    companion object {
        const val CLIENT_NAME = "jAmule"
    }

}