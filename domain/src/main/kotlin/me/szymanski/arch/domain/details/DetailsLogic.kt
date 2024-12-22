package me.szymanski.arch.domain.details

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.szymanski.arch.Logger
import me.szymanski.arch.domain.data.DetailId
import me.szymanski.arch.domain.data.LoadingState
import me.szymanski.arch.domain.data.RepositoryDetail
import me.szymanski.arch.domain.data.RepositoryDetails
import me.szymanski.arch.domain.data.RepositoryId
import me.szymanski.arch.domain.navigation.NavigationCoordinator
import me.szymanski.arch.rest.RestApi
import javax.inject.Inject

interface DetailsLogic {
    fun loadDetails(scope: CoroutineScope, repository: RepositoryId, force: Boolean = false)
    fun onBackClick()

    val items: StateFlow<List<RepositoryDetail>>
    val title: StateFlow<String>
    val loadingState: StateFlow<LoadingState>
}

class DetailsLogicImpl @Inject constructor(
    private val restApi: RestApi,
    private val navigationCoordinator: NavigationCoordinator,
    private val logger: Logger
) : DetailsLogic {
    override val loadingState = MutableStateFlow(LoadingState.LOADING)
    override val items = MutableStateFlow(emptyList<RepositoryDetail>())
    override val title = MutableStateFlow("")

    private var lastJob: Job? = null
    private var lastRepositoryId: RepositoryId? = null

    override fun loadDetails(scope: CoroutineScope, repositoryId: RepositoryId, force: Boolean) {
        if (lastRepositoryId == repositoryId && !force) {
            return
        }
        title.value = repositoryId.repositoryName
        lastJob?.cancel()

        loadingState.value = LoadingState.LOADING
        lastRepositoryId = repositoryId
        lastJob = scope.launch {
            runCatching {
                val details = RepositoryDetails(restApi.getRepository(repositoryId.userName, repositoryId.repositoryName))
                loadingState.value = LoadingState.SUCCESS
                items.value = toDetailsItems(details)
                title.value = "${details.owner.login} / ${details.name}"
            }.onFailure {
                logger.log(it)
                loadingState.value = LoadingState.ERROR
            }
        }
    }

    override fun onBackClick() = navigationCoordinator.onBackPressed()

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
