package me.szymanski.arch.logic.rest

sealed class ApiError(override val cause: Throwable? = null) : Throwable() {
    data class HttpErrorResponse(override val cause: Throwable, val code: Int) : ApiError(cause)
    data class NoConnection(override val cause: Throwable) : ApiError(cause)
    data class UnknownError(override val cause: Throwable) : ApiError(cause)
}
