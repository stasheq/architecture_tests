package me.szymanski.arch.domain.data

import me.szymanski.arch.rest.models.ApiRepositoryDetails

data class RepositoryDetails(
    val id: String,
    val name: String,
    val description: String?,
    val private: Boolean,
    val owner: Owner,
    val forks: Int,
    val language: String?,
    val openIssues: Int,
    val license: License?,
    val watchers: Int,
    val defaultBranch: String
) {
    constructor(api: ApiRepositoryDetails) : this(
        id = api.id,
        name = api.name,
        description = api.description,
        private = api.private,
        owner = Owner(api.owner),
        forks = api.forks,
        language = api.language,
        openIssues = api.openIssues,
        license = api.license?.let { License(it) },
        watchers = api.watchers,
        defaultBranch = api.defaultBranch
    )
}
