package jamule.response

import jamule.ec.packet.Packet

object ResponseParser {
    private val deserializers: List<ResponseDeserializer> = listOf(
        AuthFailedResponse.AuthFailedResponseDeserializer,
        AuthOkResponse.AuthOkResponseDeserializer,
        AuthSaltResponse.AuthSaltResponseDeserializer,
        DownloadQueueResponse.DownloadQueueResponseDeserializer,
        EmptyPreferencesResponse.EmptyPreferencesResponseDeserializer,
        ErrorResponse.ErrorResponseDeserializer,
        MiscDataResponse.MiscDataResponseDeserializer,
        NoopResponse.NoopResponseDeserializer,
        PrefsCategoriesResponse.PrefsCategoriesResponseDeserializer,
        SearchResultsResponse.SearchResultsResponseDeserializer,
        SearchStatusResponse.SearchStatusResponseDeserializer,
        SharedFilesResponse.SharedFilesResponseDeserializer,
        StatsResponse.StatsResponseDeserializer,
        StringsResponse.StringsResponseDeserializer,
    )

    internal fun parse(packet: Packet): Response {
        return deserializers.firstOrNull { it.canDeserialize(packet) }?.deserialize(packet)
            ?: throw IllegalStateException("No deserializer found for opCode ${packet.opCode}")
    }
}