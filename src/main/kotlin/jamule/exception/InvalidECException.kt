package jamule.exception

/**
 * Thrown when the EC is invalid.
 */
class InvalidECException(message: String, exception: Exception? = null) : AmuleException(message, exception)