package me.szymanski.arch.logic.rest

import com.google.gson.annotations.SerializedName

data class RestConfig(val baseUrl: String, val defaultUser: String, val pageLimit: Int, val callTimeout: Int)

data class Repository(val id: String, val name: String, val description: String)

data class RepositoryDetails(
    val id: String,
    val name: String,
    val description: String?,
    val private: Boolean,
    val owner: Owner,
    val forks: Int,
    val language: String,
    @SerializedName("open_issues_count")
    val openIssues: Int,
    val license: License,
    val watchers: Int,
    @SerializedName("default_branch")
    val defaultBranch: String
)

data class Owner(val login: String)

data class License(val name: String)
