package jamule.ec.packet

import jamule.ec.InvalidECException
import jamule.ec.tag.TagEncoder
import jamule.ec.toUByteArray
import org.slf4j.Logger
import java.io.OutputStream
import java.util.zip.Deflater

@OptIn(ExperimentalUnsignedTypes::class)
class PacketWriter(
    private val logger: Logger,
    private val tagEncoder: TagEncoder,
) {

    fun write(
        packet: Packet,
        outputStream: OutputStream
    ) {
        if (packet.flags.hasId && packet.id == null)
            throw InvalidECException("ID must be provided when packet has ID flag")
        logger.debug("Writing packet...")

        writeTransport(packet, outputStream)
    }

    private fun writeTransport(
        packet: Packet,
        outputStream: OutputStream
    ) {
        // Print flags first
        outputStream.write(packet.flags.toUInt().toUByteArray().toByteArray())

        // Print accept flags if present
        if (packet.flags.accepts) {
            if (packet.accepts == null)
                throw InvalidECException("Accepts flags must be provided when packet has accepts flag")
            outputStream.write(packet.accepts.toUInt().toUByteArray().toByteArray())
        }

        // Encode whole payload
        val rawPayload = encodePayload(packet)
        val payload = if (packet.flags.zlibCompressed) {
            compressPayload(rawPayload)
        } else {
            rawPayload
        }

        // Print length
        outputStream.write(payload.size.toUInt().toUByteArray().toByteArray())

        // Print payload
        outputStream.write(payload.toByteArray())
    }

    private fun encodePayload(packet: Packet): UByteArray {
        val opCode = packet.opCode.value
        val tagCount = packet.tags.size.toUShort().toUByteArray(packet.flags.utf8)
        val tags = List(packet.tags.size) { i ->
            tagEncoder.encode(packet.tags[i], packet.flags.utf8)
        }.reduce { acc, bytes -> acc + bytes }
        return UByteArray(0) + opCode + tagCount + tags
    }

    private fun compressPayload(input: UByteArray): UByteArray {
        val deflater = Deflater()
        deflater.setInput(input.toByteArray())
        deflater.finish()

        val compressedData = ByteArray(input.size)
        val compressedDataLength = deflater.deflate(compressedData)

        return compressedData.copyOfRange(0, compressedDataLength).toUByteArray()
    }

}