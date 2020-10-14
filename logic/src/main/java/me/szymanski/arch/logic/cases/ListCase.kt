package me.szymanski.arch.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.szymanski.arch.logic.rest.ApiError
import me.szymanski.arch.logic.rest.Repository
import me.szymanski.arch.logic.rest.RestApi
import me.szymanski.arch.logic.rest.RestConfig
import me.szymanski.glue.BaseCase
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class ListCase @Inject constructor(private val restApi: RestApi, restConfig: RestConfig) : BaseCase() {
    val loading: BehaviorRelay<LoadingState> = BehaviorRelay.create<LoadingState>()
    val list: BehaviorRelay<List<Repository>> = BehaviorRelay.create<List<Repository>>()
    private var lastJob: Job? = null
    var userName = restConfig.defaultUser
        set(value) {
            if (field == value) return
            field = value
            if (value.isNotBlank()) {
                (parent as? MainCase)?.userName?.accept(value)
                reload()
            }
        }

    fun reload() {
        lastJob?.cancel()
        loading.accept(LoadingState.LOADING)
        lastJob = ioScope.launch {
            try {
                val items = restApi.getRepositories(userName)
                loading.accept(if (items.isEmpty()) LoadingState.EMPTY else LoadingState.SUCCESS)
                list.accept(items)
            } catch (e: ApiError) {
                list.accept(emptyList())
                loading.accept(LoadingState.ERROR)
            }
        }
    }

    fun selectItem(repoName: String) {
        (parent as? MainCase)?.selectedRepoName?.accept(AtomicReference(repoName))
    }

    override fun create() {
        reload()
    }

    override fun destroy() {
        lastJob?.cancel()
    }

    enum class LoadingState { LOADING, EMPTY, ERROR, SUCCESS }
}
