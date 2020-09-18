package me.szymanski.arch.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.szymanski.arch.logic.BaseCase
import me.szymanski.arch.logic.rest.ApiError
import me.szymanski.arch.logic.rest.Repository
import me.szymanski.arch.logic.rest.RestApi
import javax.inject.Inject

class RepositoriesListCase @Inject constructor(private val restApi: RestApi) : BaseCase() {
    val loading: BehaviorRelay<LoadingState> = BehaviorRelay.create<LoadingState>()
    val list: BehaviorRelay<List<Repository>> = BehaviorRelay.create<List<Repository>>()
    private var lastJob: Job? = null

    fun reload() {
        lastJob?.cancel()
        loading.accept(LoadingState.LOADING)
        lastJob = ioScope.launch {
            try {
                val items = restApi.getRepositories()
                loading.accept(if (items.isEmpty()) LoadingState.EMPTY else LoadingState.SUCCESS)
                list.accept(items)
            } catch (e: ApiError) {
                list.accept(emptyList())
                loading.accept(LoadingState.ERROR)
            }
        }
    }

    fun selectItem(repoName: String) {
        (parent as? MainCase)?.selectedRepoName?.accept(repoName)
    }

    override fun create() {
        reload()
    }

    override fun destroy() {
        lastJob?.cancel()
    }

    enum class LoadingState { LOADING, EMPTY, ERROR, SUCCESS }
}
