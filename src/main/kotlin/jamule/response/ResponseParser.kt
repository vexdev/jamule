package jamule.response

import jamule.ec.ECOpCode
import jamule.ec.packet.Packet
import org.reflections.Reflections
import kotlin.reflect.KFunction
import kotlin.reflect.full.*

object ResponseParser {
    private val deserializersMap: Map<ECOpCode, Pair<Any, KFunction<Response>>> = Reflections("jamule.response")
        .getSubTypesOf(Response::class.java)
        .mapNotNull {
            it.kotlin.companionObject
                ?.declaredMemberFunctions
                ?.firstOrNull { func -> func.findAnnotations(ResponseDeserializer::class).isNotEmpty() }
                ?.let { func -> it.kotlin.companionObjectInstance!! to func as KFunction<Response> }
        }
        .map { (companion, func) -> func.findAnnotation<ResponseDeserializer>()!!.opCode to Pair(companion, func) }
        .associate { it }

    internal fun parse(packet: Packet): Response {
        return deserializersMap[packet.opCode]?.let { (obj, fnc) -> fnc.call(obj, packet) }
            ?: throw IllegalStateException("No deserializer found for opCode ${packet.opCode}")
    }
}

@Target(AnnotationTarget.FUNCTION)
annotation class ResponseDeserializer(val opCode: ECOpCode)
