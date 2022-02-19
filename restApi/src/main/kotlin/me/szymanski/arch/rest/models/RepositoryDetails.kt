package me.szymanski.arch.rest.models

import com.google.gson.annotations.SerializedName

internal data class ApiRepositoryDetails(
    val id: String,
    val name: String,
    val description: String?,
    val private: Boolean,
    val owner: ApiOwner,
    val forks: Int,
    val language: String?,
    @SerializedName("open_issues_count")
    val openIssues: Int,
    val license: ApiLicense?,
    val watchers: Int,
    @SerializedName("default_branch")
    val defaultBranch: String
) {
    fun toDomain() = RepositoryDetails(
        id = this.id,
        name = this.name,
        description = this.description,
        private = this.private,
        owner = this.owner.toDomain(),
        forks = this.forks,
        language = this.language,
        openIssues = this.openIssues,
        license = this.license?.toDomain(),
        watchers = this.watchers,
        defaultBranch = this.defaultBranch
    )
}

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
)
