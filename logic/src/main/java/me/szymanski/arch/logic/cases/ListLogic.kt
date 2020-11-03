package me.szymanski.arch.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.szymanski.arch.logic.Logic
import me.szymanski.arch.logic.rest.ApiError
import me.szymanski.arch.logic.rest.Repository
import me.szymanski.arch.logic.rest.RestApi
import me.szymanski.arch.logic.rest.RestConfig
import javax.inject.Inject

interface ListLogic : Logic {
    fun reload()
    var userName: String
    val list: Observable<List<Repository>>
    val state: Observable<LoadingState>

    enum class LoadingState { LOADING, EMPTY, ERROR, SUCCESS }
}

class ListLogicImpl @Inject constructor(private val restApi: RestApi, restConfig: RestConfig) : ListLogic {
    private val scope = instantiateCoroutineScope()
    override val state: BehaviorRelay<ListLogic.LoadingState> = BehaviorRelay.create<ListLogic.LoadingState>()
    override val list: BehaviorRelay<List<Repository>> = BehaviorRelay.create<List<Repository>>()
    private var lastJob: Job? = null
    override var userName = restConfig.defaultUser
        set(value) {
            if (field == value) return
            field = value
            if (value.isNotBlank()) reload()
        }

    override fun reload() {
        lastJob?.cancel()
        state.accept(ListLogic.LoadingState.LOADING)
        lastJob = scope.launch {
            try {
                val items = restApi.getRepositories(userName)
                state.accept(if (items.isEmpty()) ListLogic.LoadingState.EMPTY else ListLogic.LoadingState.SUCCESS)
                list.accept(items)
            } catch (e: ApiError) {
                list.accept(emptyList())
                state.accept(ListLogic.LoadingState.ERROR)
            }
        }
    }

    override fun create() {
        reload()
    }

    override fun destroy() {
        lastJob?.cancel()
    }
}
