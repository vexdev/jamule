package jamule.response

sealed interface Response

data class AuthResponse(val salt: ULong) : Response