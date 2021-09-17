package me.szymanski.arch.logic.screenslogic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import me.szymanski.arch.logic.rest.ApiError
import me.szymanski.arch.logic.rest.RepositoryDetails
import me.szymanski.arch.logic.rest.RestApi
import javax.inject.Inject

interface DetailsLogic {
    var repositoryId: RepositoryId?
    fun reload()
    fun onBackPressed()

    val result: SharedFlow<List<RepositoryDetail>>
    val title: SharedFlow<String>
    val state: SharedFlow<LoadingState>

    enum class LoadingState { LOADING, ERROR, SUCCESS }
}

enum class DetailId { NAME, DESCRIPTION, PRIVATE, OWNER, FORKS, LANGUAGE, ISSUES, LICENSE, WATCHERS, BRANCH }

data class RepositoryId(val userName: String, val repositoryName: String)

data class RepositoryDetail(val type: DetailId, val value: String)

class DetailsLogicImpl @Inject constructor(
    private val restApi: RestApi,
    private val scope: CoroutineScope,
    private val navigationLogic: NavigationLogic
) : DetailsLogic {
    override val state = MutableStateFlow(DetailsLogic.LoadingState.LOADING)
    override val result = MutableStateFlow(emptyList<RepositoryDetail>())
    override val title = MutableStateFlow("")
    private var lastJob: Job? = null
    override var repositoryId: RepositoryId? = null
        set(value) {
            field = value
            reload()
        }

    override fun reload() {
        lastJob?.cancel()
        val repository = repositoryId
        if (repository == null) {
            state.value = DetailsLogic.LoadingState.ERROR
            return
        }

        state.value = DetailsLogic.LoadingState.LOADING
        title.value = ""
        lastJob = scope.launch {
            try {
                val details = restApi.getRepository(repository.userName, repository.repositoryName)
                state.value = DetailsLogic.LoadingState.SUCCESS
                result.value = toDetailsItems(details)
                title.value = "${details.owner.login} / ${details.name}"
            } catch (e: ApiError) {
                state.value = DetailsLogic.LoadingState.ERROR
            }
        }
    }

    override fun onBackPressed() = navigationLogic.onBackPressed()

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
}
