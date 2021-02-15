package me.szymanski.arch.logic.rest

data class RestConfig(val baseUrl: String, val defaultUser: String, val pageLimit: Int, val callTimeout: Int)

data class Repository(val id: String, val name: String, val description: String)

data class RepositoryDetails(
    val id: String,
    val name: String,
    val description: String?,
    val private: Boolean,
    val owner: Owner,
    val forks: Int,
    val language: String?,
    val openIssues: Int, //open_issues_count
    val license: License?,
    val watchers: Int,
    val defaultBranch: String //default_branch
)

data class Owner(val login: String)

data class License(val name: String)
