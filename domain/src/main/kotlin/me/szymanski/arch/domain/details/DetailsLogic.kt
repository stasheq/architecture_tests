package me.szymanski.arch.domain.details

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.szymanski.arch.Logger
import me.szymanski.arch.domain.data.DetailId
import me.szymanski.arch.domain.data.LoadingState
import me.szymanski.arch.domain.data.Repository
import me.szymanski.arch.domain.data.RepositoryDetail
import me.szymanski.arch.domain.data.RepositoryDetails
import me.szymanski.arch.domain.navigation.NavigationCoordinator
import me.szymanski.arch.rest.RestApi
import javax.inject.Inject

interface DetailsLogic {
    fun loadDetails(scope: CoroutineScope, repository: Repository, force: Boolean = false)
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
    private var lastRepository: Repository? = null

    override fun loadDetails(scope: CoroutineScope, repository: Repository, force: Boolean) {
        if (lastRepository == repository && !force) {
            return
        }
        title.value = "${repository.owner}/${repository.name}"
        lastJob?.cancel()

        loadingState.value = LoadingState.LOADING
        lastRepository = repository
        lastJob = scope.launch {
            runCatching {
                val details = RepositoryDetails(restApi.getRepository(repository.owner, repository.name))
                loadingState.value = LoadingState.SUCCESS
                items.value = toDetailsItems(details)
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
