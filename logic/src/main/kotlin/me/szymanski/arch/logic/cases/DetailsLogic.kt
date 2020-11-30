package me.szymanski.arch.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Job
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
    val result: Observable<List<RepositoryDetail>>
    val title: Observable<String>
    val state: Observable<LoadingState>

    enum class LoadingState { LOADING, ERROR, SUCCESS }
}

enum class DetailId { NAME, DESCRIPTION, PRIVATE, OWNER, FORKS, LANGUAGE, ISSUES, LICENSE, WATCHERS, BRANCH }

data class RepositoryDetail(val type: DetailId, val value: String)

class DetailsLogicImpl @Inject constructor(private val restApi: RestApi) : DetailsLogic {
    private val scope = instantiateCoroutineScope()
    override val state: BehaviorRelay<DetailsLogic.LoadingState> = BehaviorRelay.create()
    override val result: BehaviorRelay<List<RepositoryDetail>> = BehaviorRelay.create()
    override val title: BehaviorRelay<String> = BehaviorRelay.create()
    private var lastJob: Job? = null
    override var repositoryName: String? = null
    override var userName: String? = null

    override fun reload() {
        lastJob?.cancel()
        val repositoryName = this.repositoryName
        val userName = this.userName
        if (repositoryName == null || userName == null) {
            state.accept(DetailsLogic.LoadingState.ERROR)
            return
        }

        state.accept(DetailsLogic.LoadingState.LOADING)
        title.accept("")
        lastJob = scope.launch {
            try {
                val repository = restApi.getRepository(userName, repositoryName)
                state.accept(DetailsLogic.LoadingState.SUCCESS)
                result.accept(toDetailsItems(repository))
                title.accept("${repository.owner.login} / ${repository.name}")
            } catch (e: ApiError) {
                state.accept(DetailsLogic.LoadingState.ERROR)
            }
        }
    }

    private fun toDetailsItems(repo: RepositoryDetails): List<RepositoryDetail> = listOf(
        RepositoryDetail(DetailId.NAME, repo.name),
        RepositoryDetail(DetailId.DESCRIPTION, repo.description ?: ""),
        RepositoryDetail(DetailId.LANGUAGE, repo.language),
        RepositoryDetail(DetailId.PRIVATE, "${repo.private}"),
        RepositoryDetail(DetailId.OWNER, repo.owner.login),
        RepositoryDetail(DetailId.FORKS, "${repo.forks}"),
        RepositoryDetail(DetailId.ISSUES, "${repo.openIssues}"),
        RepositoryDetail(DetailId.LICENSE, repo.license.name),
        RepositoryDetail(DetailId.BRANCH, repo.defaultBranch),
        RepositoryDetail(DetailId.WATCHERS, "${repo.watchers}")
    )

    override fun destroy() {
        lastJob?.cancel()
    }
}
