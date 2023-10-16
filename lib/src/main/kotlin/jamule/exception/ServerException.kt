package jamule.exception

/**
 * Thrown when the server returns an error response.
 */
class ServerException(message: String, exception: Exception? = null) : AmuleException(message, exception)