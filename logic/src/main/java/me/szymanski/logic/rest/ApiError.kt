package me.szymanski.logic.rest

sealed class ApiError(override val message: String? = null, override val cause: Throwable? = null) : Throwable() {
    data class UnknownError(override val cause: Throwable) : ApiError()
}
