package jamule.exception

/**
 * Thrown when the authentication with the server fails.
 */
class AuthFailedException(message: String, exception: Exception? = null) : AmuleException(message, exception)