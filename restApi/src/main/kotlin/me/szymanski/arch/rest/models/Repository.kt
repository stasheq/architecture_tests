package me.szymanski.arch.rest.models

internal data class ApiRepository(
    val id: String,
    val name: String,
    val description: String?
) {
    fun toDomain() = Repository(
        id = this.id,
        name = this.name,
        description = this.description
    )
}

data class Repository(
    val id: String,
    val name: String,
    val description: String?
)
