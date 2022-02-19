package me.szymanski.arch.rest

import me.szymanski.arch.rest.models.ApiRepository
import me.szymanski.arch.rest.models.ApiRepositoryDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface GitHubRestApiService {

    @GET("users/{user}/repos")
    suspend fun getRepositoriesList(
        @Path("user") user: String,
        @Query("per_page") limit: Int,
        @Query("page") page: Int
    ): List<ApiRepository>

    @GET("repos/{user}/{repo}")
    suspend fun getRepositoryDetails(
        @Path("user") user: String,
        @Path("repo") repo: String
    ): ApiRepositoryDetails
}
