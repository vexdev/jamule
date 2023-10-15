package jamule.exception

abstract class AmuleException(message: String, exception: Exception?) : Exception(message, exception)