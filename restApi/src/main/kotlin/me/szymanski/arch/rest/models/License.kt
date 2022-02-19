package me.szymanski.arch.rest.models

internal data class ApiLicense(
    val name: String
) {
    fun toDomain() = License(
        name = this.name
    )
}

data class License(
    val name: String
)