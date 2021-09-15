package me.szymanski.arch.logic.screenslogic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import me.szymanski.arch.logic.Logic
import me.szymanski.arch.logic.rest.ApiError
import me.szymanski.arch.logic.rest.RepositoryDetails
import me.szymanski.arch.logic.rest.RestApi
import javax.inject.Inject

interface DetailsLogic : Logic {
    var repositoryName: String?
    var userName: String?
    fun reload()
    val result: SharedFlow<List<RepositoryDetail>>
    val title: SharedFlow<String>
    val state: SharedFlow<LoadingState>

    enum class LoadingState { LOADING, ERROR, SUCCESS }
}

enum class DetailId { NAME, DESCRIPTION, PRIVATE, OWNER, FORKS, LANGUAGE, ISSUES, LICENSE, WATCHERS, BRANCH }

data class RepositoryDetail(val type: DetailId, val value: String)

class DetailsLogicImpl @Inject constructor(private val restApi: RestApi, private val scope: CoroutineScope) : DetailsLogic {
    override val state = MutableStateFlow(DetailsLogic.LoadingState.LOADING)
    override val result = MutableStateFlow(emptyList<RepositoryDetail>())
    override val title = MutableStateFlow("")
    private var lastJob: Job? = null
    override var repositoryName: String? = null
    override var userName: String? = null

    override fun reload() {
        lastJob?.cancel()
        val repositoryName = this.repositoryName
        val userName = this.userName
        if (repositoryName == null || userName == null) {
            state.value = DetailsLogic.LoadingState.SUCCESS
            return
        }

        state.value = DetailsLogic.LoadingState.LOADING
        title.value = ""
        lastJob = scope.launch {
            try {
                val repository = restApi.getRepository(userName, repositoryName)
                state.value = DetailsLogic.LoadingState.SUCCESS
                result.value = toDetailsItems(repository)
                title.value = "${repository.owner.login} / ${repository.name}"
            } catch (e: ApiError) {
                state.value = DetailsLogic.LoadingState.ERROR
            }
        }
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
}
