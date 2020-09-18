package me.szymanski.arch.logic.rest

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    @GET("users/{user}/repos")
    suspend fun getRepositoriesList(
        @Path("user") user: String,
        @Query("per_page") limit: Int
    ): List<Repository>

    @GET("repos/{user}/{repo}")
    suspend fun getRepositoryDetails(
        @Path("user") user: String,
        @Query("repo") repo: String
    ): RepositoryDetails
}
