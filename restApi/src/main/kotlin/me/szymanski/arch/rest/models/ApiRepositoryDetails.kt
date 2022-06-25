package me.szymanski.arch.rest.models

import com.google.gson.annotations.SerializedName

data class ApiRepositoryDetails(
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
)
