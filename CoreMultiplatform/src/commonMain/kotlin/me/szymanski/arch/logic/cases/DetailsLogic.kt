package me.szymanski.arch.logic.cases

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import me.szymanski.arch.logic.Logic
import me.szymanski.arch.logic.replayFlow
import me.szymanski.arch.logic.publish
import me.szymanski.arch.logic.rest.ApiError
import me.szymanski.arch.logic.rest.RepositoryDetails
import me.szymanski.arch.logic.rest.RestApi

interface DetailsLogic : Logic {
    var repositoryName: String?
    var userName: String?
    fun reload()
    val result: Flow<List<RepositoryDetail>>
    val title: Flow<String>
    val state: Flow<LoadingState>

    enum class LoadingState { LOADING, ERROR, SUCCESS }
}

enum class DetailId { NAME, DESCRIPTION, PRIVATE, OWNER, FORKS, LANGUAGE, ISSUES, LICENSE, WATCHERS, BRANCH }

data class RepositoryDetail(val type: DetailId, val value: String)

class DetailsLogicImpl(private val restApi: RestApi) : DetailsLogic {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    override val state = replayFlow<DetailsLogic.LoadingState>()
    override val result = replayFlow<List<RepositoryDetail>>()
    override val title = replayFlow<String>()
    private var lastJob: Job? = null
    override var repositoryName: String? = null
    override var userName: String? = null

    override fun reload() {
        lastJob?.cancel()
        val repositoryName = this.repositoryName
        val userName = this.userName
        if (repositoryName == null || userName == null) {
            state.publish(DetailsLogic.LoadingState.SUCCESS)
            return
        }

        state.publish(DetailsLogic.LoadingState.LOADING)
        title.publish("")
        lastJob = scope.launch {
            try {
                val repository = restApi.getRepository(userName, repositoryName)
                state.publish(DetailsLogic.LoadingState.SUCCESS)
                result.publish(toDetailsItems(repository))
                title.publish("${repository.owner.login} / ${repository.name}")
            } catch (e: ApiError) {
                state.publish(DetailsLogic.LoadingState.ERROR)
            }
        }
        state.collect {  }
    }

    private fun toDetailsItems(repo: RepositoryDetails): List<RepositoryDetail> = listOf(
        RepositoryDetail(DetailId.NAME, repo.name),
        RepositoryDetail(DetailId.DESCRIPTION, repo.description ?: ""),
        RepositoryDetail(DetailId.LANGUAGE, repo.language ?: ""),
        RepositoryDetail(DetailId.PRIVATE, "${repo.private}"),
        RepositoryDetail(DetailId.OWNER, repo.owner.login),
        RepositoryDetail(DetailId.FORKS, "${repo.forks}"),
        RepositoryDetail(DetailId.ISSUES, "${repo.openIssues}"),
        RepositoryDetail(DetailId.LICENSE, repo.license?.name ?: ""),
        RepositoryDetail(DetailId.BRANCH, repo.defaultBranch),
        RepositoryDetail(DetailId.WATCHERS, "${repo.watchers}")
    )

    override fun create() {
        reload()
    }

    override fun destroy() {
        lastJob?.cancel()
    }
}
