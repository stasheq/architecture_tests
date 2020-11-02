package me.szymanski.arch.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.szymanski.arch.logic.rest.ApiError
import me.szymanski.arch.logic.rest.Repository
import me.szymanski.arch.logic.rest.RestApi
import me.szymanski.arch.logic.rest.RestConfig
import me.szymanski.glue.Logic
import me.szymanski.glue.LogicTemplate
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

interface ListLogic : Logic {
    fun reload()
    fun selectItem(repoName: String)
    var userName: String
    val list: Observable<List<Repository>>
    val loading: Observable<LoadingState>

    enum class LoadingState { LOADING, EMPTY, ERROR, SUCCESS }
}

class ListLogicImpl @Inject constructor(private val restApi: RestApi, restConfig: RestConfig) :
    LogicTemplate(), ListLogic {
    override val loading: BehaviorRelay<ListLogic.LoadingState> = BehaviorRelay.create<ListLogic.LoadingState>()
    override val list: BehaviorRelay<List<Repository>> = BehaviorRelay.create<List<Repository>>()
    private var lastJob: Job? = null
    override var userName = restConfig.defaultUser
        set(value) {
            if (field == value) return
            field = value
            if (value.isNotBlank()) {
                (parent as? MainLogicImpl)?.userName?.accept(value)
                reload()
            }
        }

    override fun reload() {
        lastJob?.cancel()
        loading.accept(ListLogic.LoadingState.LOADING)
        lastJob = ioScope.launch {
            try {
                val items = restApi.getRepositories(userName)
                loading.accept(if (items.isEmpty()) ListLogic.LoadingState.EMPTY else ListLogic.LoadingState.SUCCESS)
                list.accept(items)
            } catch (e: ApiError) {
                list.accept(emptyList())
                loading.accept(ListLogic.LoadingState.ERROR)
            }
        }
    }

    override fun selectItem(repoName: String) {
        (parent as? MainLogicImpl)?.selectedRepoName?.accept(AtomicReference(repoName))
    }

    override fun create() {
        reload()
    }

    override fun destroy() {
        lastJob?.cancel()
    }
}
