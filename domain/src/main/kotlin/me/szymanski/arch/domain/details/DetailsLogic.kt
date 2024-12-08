package me.szymanski.arch.domain.details

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.szymanski.arch.domain.data.DetailId
import me.szymanski.arch.domain.data.LoadingState
import me.szymanski.arch.domain.data.RepositoryDetail
import me.szymanski.arch.domain.data.RepositoryDetails
import me.szymanski.arch.domain.data.RepositoryId
import me.szymanski.arch.domain.navigation.NavigationCoordinator
import me.szymanski.arch.rest.ApiError
import me.szymanski.arch.rest.RestApi
import javax.inject.Inject

interface DetailsLogic {
    var repositoryId: RepositoryId?
    fun reload()
    fun onBackClick()

    val items: StateFlow<List<RepositoryDetail>>
    val title: StateFlow<String>
    val loadingState: StateFlow<LoadingState>
}

class DetailsLogicImpl @Inject constructor(
    private val restApi: RestApi,
    private val scope: CoroutineScope,
    private val navigationCoordinator: NavigationCoordinator
) : DetailsLogic {
    override val loadingState = MutableStateFlow(LoadingState.LOADING)
    override val items = MutableStateFlow(emptyList<RepositoryDetail>())
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
            loadingState.value = LoadingState.ERROR
            return
        }

        loadingState.value = LoadingState.LOADING
        title.value = ""
        lastJob = scope.launch {
            try {
                val details = RepositoryDetails(restApi.getRepository(repository.userName, repository.repositoryName))
                loadingState.value = LoadingState.SUCCESS
                items.value = toDetailsItems(details)
                title.value = "${details.owner.login} / ${details.name}"
            } catch (e: ApiError) {
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
