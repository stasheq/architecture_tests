package me.szymanski.logic.rest

interface RestApi {
    suspend fun getRepositories(): List<Repository>
}

class RestApiImpl(
    private val api: GitHubService,
    private val restConfig: RestConfig
) : RestApi {

    override suspend fun getRepositories(): List<Repository> {
        return api.getRepositoriesList(restConfig.user, restConfig.limit)
    }
}
