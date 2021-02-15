package me.szymanski.arch.logic.rest

import me.szymanski.arch.logic.Logger

class RestApi(
    private val restConfig: RestConfig,
    private val logger: Logger
) {

    suspend fun getRepositories(user: String, page: Int) = call<List<Repository>> {
        throw RuntimeException("Not implemented")
    }

    suspend fun getRepository(user: String, repoName: String) = call<RepositoryDetails> {
        throw RuntimeException("Not implemented")
    }

    private suspend fun <T> call(request: suspend () -> T): T {
        try {
            return request()
        } catch (e: Throwable) {
            logger.log("API call error", e, level = Logger.Level.DEBUG)
            throw ApiError.UnknownError(e)
        }
    }
}
