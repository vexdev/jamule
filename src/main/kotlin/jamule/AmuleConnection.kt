package jamule

import jamule.ec.packet.PacketParser
import jamule.ec.packet.PacketWriter
import jamule.ec.tag.TagEncoder
import jamule.ec.tag.TagParser
import jamule.exception.ServerException
import jamule.request.Request
import jamule.response.ErrorResponse
import jamule.response.Response
import jamule.response.ResponseParser
import org.slf4j.Logger
import java.net.Socket

internal class AmuleConnection(
    private val socket: Socket,
    logger: Logger
) : AutoCloseable by socket {
    @OptIn(ExperimentalUnsignedTypes::class)
    private val tagParser = TagParser(logger)

    @OptIn(ExperimentalUnsignedTypes::class)
    private val packetParser = PacketParser(tagParser, logger)

    @OptIn(ExperimentalUnsignedTypes::class)
    private val tagEncoder = TagEncoder(logger)

    @OptIn(ExperimentalUnsignedTypes::class)
    private val packetWriter = PacketWriter(tagEncoder, logger)

    @OptIn(ExperimentalUnsignedTypes::class)
    fun sendRequest(request: Request): Response {
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