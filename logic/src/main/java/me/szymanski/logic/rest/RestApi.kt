package me.szymanski.logic.rest

import javax.inject.Inject

class RestApi @Inject constructor(
    private val service: GitHubService,
    private val restConfig: RestConfig
) {

    suspend fun getRepositories() = call { service.getRepositoriesList(restConfig.user, restConfig.limit) }

    private suspend fun <T> call(request: suspend () -> T): T {
        try {
            return request()
        } catch (e: Throwable) {
            throw ApiError.UnknownError(e)
        }
    }
}
