package jamule.response

sealed interface Response

data class AuthSaltResponse(val salt: ULong) : Response

data class AuthOkResponse(val version: String) : Response

data class AuthFailedResponse(val reason: String) : Response