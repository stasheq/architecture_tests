package me.szymanski.logic.rest

interface RestApi {
    suspend fun getRepositories(): List<Repository>
}

class RestApiImpl(
    private val api: GitHubService
) : RestApi {

    override suspend fun getRepositories(): List<Repository> {
        return api.getRepositoriesList("stasheq", 5)
    }
}
