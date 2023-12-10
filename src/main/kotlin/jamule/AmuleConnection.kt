package jamule

import jamule.auth.PasswordHasher
import jamule.ec.packet.PacketParser
import jamule.ec.packet.PacketWriter
import jamule.ec.tag.TagEncoder
import jamule.ec.tag.TagParser
import jamule.exception.CommunicationException
import jamule.exception.ServerException
import jamule.request.AuthRequest
import jamule.request.Request
import jamule.request.SaltRequest
import jamule.response.*
import org.slf4j.Logger
import java.io.IOException
import java.net.Socket

internal class AmuleConnection(
    private var socketBuilder: () -> Socket,
    private val password: String,
    private val logger: Logger
) {
    private var connected = false
    private var socket = socketBuilder()

    internal constructor(
        host: String,
        port: Int,
        timeout: Int,
        password: String,
        logger: Logger
    ) : this({ Socket(host, port).apply { soTimeout = timeout } }, password, logger)

    @OptIn(ExperimentalUnsignedTypes::class)
    private val tagParser = TagParser(logger)

    @OptIn(ExperimentalUnsignedTypes::class)
    private val packetParser = PacketParser(tagParser, logger)

    @OptIn(ExperimentalUnsignedTypes::class)
    private val tagEncoder = TagEncoder(logger)

    @OptIn(ExperimentalUnsignedTypes::class)
    private val packetWriter = PacketWriter(tagEncoder, logger)

    fun reconnect() {
        synchronized(socket) {
            logger.info("Reconnecting...")
            connected = false
            runCatching { socket.close() }
            socket = socketBuilder()
            authenticate()
        }
    }

    fun sendRequest(request: Request): Response {
        if (!connected) reconnect()
        try {
            return sendRequestNoAuth(request)
        } catch (e: IOException) {
            connected = false
            throw e
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun sendRequestNoAuth(request: Request): Response {
        synchronized(socket) {
            val outputStream = socket.getOutputStream()
            val inputStream = socket.getInputStream().buffered()
            val packet = request.packet()
            packetWriter.write(packet, outputStream)
            val responsePacket = packetParser.parse(inputStream)
            return ResponseParser.parse(responsePacket).also {
                if (it is ErrorResponse) {
                    throw ServerException(it.serverMessage)
                }
            }
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun authenticate() {
        logger.info("Authenticating...")
        val saltResponse = sendRequestNoAuth(SaltRequest())
        if (saltResponse is AuthFailedResponse)
            throw ServerException("Authentication failed", saltResponse)
        else if (saltResponse !is AuthSaltResponse)
            throw CommunicationException("Unable to get auth salt")
        val saltedPassword = PasswordHasher.hash(password, saltResponse.salt)
        val response = sendRequestNoAuth(AuthRequest(saltedPassword))
        if (response is AuthFailedResponse)
            throw ServerException("Authentication failed", response)
        else if (response !is AuthOkResponse)
            throw CommunicationException("Unable to authenticate")
        logger.info("Authenticated with server version ${response.version}")
        connected = true
    }
}