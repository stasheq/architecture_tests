package me.szymanski.arch.domain.data

import me.szymanski.arch.rest.models.ApiOwner

data class Owner(
    val login: String
) {
    constructor(api: ApiOwner) : this(login = api.login)
}
