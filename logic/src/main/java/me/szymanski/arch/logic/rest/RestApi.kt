package me.szymanski.arch.logic.rest

import javax.inject.Inject

class RestApi @Inject constructor(
    private val service: GitHubService,
    private val restConfig: RestConfig
) {

    suspend fun getRepositories(user: String) = call { service.getRepositoriesList(user, restConfig.limit) }

    suspend fun getRepository(user: String, repoName: String) = call { service.getRepositoryDetails(user, repoName) }

    private suspend fun <T> call(request: suspend () -> T): T {
        try {
            return request()
        } catch (e: Throwable) {
            throw ApiError.UnknownError(e)
        }
    }
}
