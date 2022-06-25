package me.szymanski.arch.domain.data

import me.szymanski.arch.rest.models.ApiLicense

data class License(
    val name: String
) {
    constructor(api: ApiLicense) : this(name = api.name)
}
