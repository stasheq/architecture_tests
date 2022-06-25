package me.szymanski.arch.domain.data

import me.szymanski.arch.rest.models.ApiRepository

data class Repository(
    val id: String,
    val name: String,
    val description: String?
) {
    constructor(api: ApiRepository) : this(
        id = api.id,
        name = api.name,
        description = api.description
    )
}
