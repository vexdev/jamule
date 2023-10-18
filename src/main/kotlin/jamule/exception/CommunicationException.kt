package jamule.exception

/**
 * Thrown when the communication with the server fails for unknown reasons.
 */
class CommunicationException(message: String, exception: Exception? = null) : AmuleException(message, exception)