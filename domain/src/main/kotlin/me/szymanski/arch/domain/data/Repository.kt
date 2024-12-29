package me.szymanski.arch.domain.data

import me.szymanski.arch.rest.models.ApiRepository
import java.io.Serializable

data class Repository(
    val id: String,
    val name: String,
    val description: String?,
    val owner: String,
) : Serializable {
    constructor(api: ApiRepository) : this(
        id = api.id,
        name = api.name,
        description = api.description,
        owner = api.owner.login
    )
}
