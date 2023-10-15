package jamule.ec.packet

import jamule.ec.*
import jamule.ec.tag.Tag
import jamule.ec.tag.TagParser
import jamule.exception.InvalidECException
import org.slf4j.Logger
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.zip.Inflater

@ExperimentalUnsignedTypes
class PacketParser(
    private val tagParser: TagParser,
    private val logger: Logger
) {

    fun parse(stream: InputStream): Packet {
        logger.debug("Parsing packet...")
        val transport = parseTransport(stream)
        return parseApplication(transport)
    }

    private fun parseTransport(stream: InputStream): Transport {
        logger.debug("Parsing transport layer...")
        val flags = parseFlags(stream)

        val acceptsFlags = if (flags.accepts) {
            logger.debug("Packet has accepts flag")
            parseFlags(stream)
        } else null

        val length = stream.readUInt()
        logger.trace("Packet length: {}", length)
        if (length == 0u)
            throw InvalidECException("Payload cannot be empty")

        val payload = if (flags.zlibCompressed) {
            logger.debug("Packet is compressed")
            decompressPayload(stream, length)
        } else {
            logger.debug("Packet is not compressed")
            stream.readNBytes(length.toInt()).toUByteArray()
        }
        logger.trace("Payload: {}", payload)
        return Transport(flags, acceptsFlags, length, payload)
    }

    private fun parseFlags(inputStream: InputStream): Flags {
        logger.debug("Parsing flags...")
        val flags = inputStream.readUInt()
        logger.trace("Flags: {}", flags)
        if (flags and ECFlag.EC_FLAG_UNKNOWN_MASK.value != 0u)
            throw InvalidECException("Unknown trasmission flags")
        return Flags.fromUInt(flags)
    }

    private fun parseApplication(transport: Transport): Packet {
        logger.debug("Parsing application layer...")
        val payload = transport.payload
        val flags = transport.flags
        val tagsIndex = INDEX_TAG_COUNT + payload[INDEX_TAG_COUNT].numberLength(flags.utf8, TAG_COUNT_SIZE)
        logger.debug("Tags start at index {}", tagsIndex)
        val tagsCount = payload.readUint16(flags.utf8, INDEX_TAG_COUNT).toInt()
        logger.debug("Packet has {} tags", tagsCount)
        val tagList = mutableListOf<Tag<*>>()
        val opCode = ECOpCode.fromValue(payload[0])
        logger.debug("Packet opcode is {}", opCode)
        var index = tagsIndex
        var tagCounter = 0
        while (index < payload.size && tagCounter < tagsCount) {
            logger.trace("Parsing tag {} starting at {}", tagCounter, index)
            val (tag: Tag<out Any>, endIndex) = tagParser.parse(payload, index, flags.utf8)
            tagList.add(tag)
            index = endIndex + 1
            tagCounter++
        }
        if (index != payload.size) {
            throw InvalidECException("Invalid tags size in packet, expected ${payload.size} found $index")
        }
        if (tagCounter != tagsCount) {
            throw InvalidECException("Error parsing tags list - Expected tags $tagsCount found tags $tagCounter")
        }
        return Packet(opCode, tagList, transport.flags, transport.acceptsFlags)
    }

    private fun decompressPayload(stream: InputStream, length: UInt): UByteArray {
        val compressed = stream.readNBytes(length.toInt())
        val inflater = Inflater()
        inflater.setInput(compressed)
        val outputStream = ByteArrayOutputStream(length.toInt())
        val buffer = ByteArray(8192)
        while (!inflater.finished()) {
            val count = inflater.inflate(buffer)
            outputStream.write(buffer, 0, count)
        }
        outputStream.close()
        inflater.end()
        return outputStream.toByteArray().toUByteArray()
    }

    private fun InputStream.readUInt(): UInt =
        this.readNBytes(4).toUByteArray().readUInt32(false, 0)

    private data class Transport(
        val flags: Flags,
        val acceptsFlags: Flags?,
        val length: UInt,
        val payload: UByteArray
    )

    companion object {
        const val INDEX_TAG_COUNT = 1 // Index of the tag count in the payload
        const val TAG_COUNT_SIZE = LEN_USHORT // Size of the tag count in bytes
    }

}