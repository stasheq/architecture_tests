package me.szymanski.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.szymanski.logic.Case
import me.szymanski.logic.rest.ApiError
import me.szymanski.logic.rest.Repository
import me.szymanski.logic.rest.RestApi
import javax.inject.Inject

class RepositoriesListCase @Inject constructor(private val restApi: RestApi) : Case() {
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

    override fun create() {
        reload()
    }

    enum class LoadingState { LOADING, EMPTY, ERROR, SUCCESS }
}
