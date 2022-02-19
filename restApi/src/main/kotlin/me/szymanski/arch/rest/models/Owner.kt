package me.szymanski.arch.rest.models

internal data class ApiOwner(
    val login: String
) {
    fun toDomain() = Owner(
        login = this.login
    )
}

data class Owner(
    val login: String
)