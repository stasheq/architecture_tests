package me.szymanski.arch.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
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
    fun itemClick(detailsLogic: DetailsLogic, repositoryName: String?)
    var userName: String
    var wideScreen: Boolean
    val list: Observable<List<Repository>>
    val state: Observable<LoadingState>
    val closeApp: Observable<Unit>
    val showList: Observable<Boolean>
    val showDetails: Observable<Boolean>

    enum class LoadingState { LOADING, EMPTY, ERROR, SUCCESS }
}

class ListLogicImpl @Inject constructor(private val restApi: RestApi, restConfig: RestConfig) : ListLogic {
    private val scope = instantiateCoroutineScope()
    private var lastJob: Job? = null
    override val state: BehaviorRelay<ListLogic.LoadingState> = BehaviorRelay.create<ListLogic.LoadingState>()
    override val list: BehaviorRelay<List<Repository>> = BehaviorRelay.create<List<Repository>>()
    override val closeApp: PublishRelay<Unit> = PublishRelay.create<Unit>()
    override val showList: BehaviorRelay<Boolean> = BehaviorRelay.createDefault<Boolean>(true)
    override val showDetails: BehaviorRelay<Boolean> = BehaviorRelay.createDefault<Boolean>(false)
    override var userName = restConfig.defaultUser
        set(value) {
            if (field == value) return
            field = value
            if (value.isNotBlank()) reload()
        }
    override var wideScreen: Boolean = false
        set(value) {
            if (field != value) {
                showList.accept(true)
                showDetails.accept(value)
            }
            field = value
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

    override fun itemClick(detailsLogic: DetailsLogic, repositoryName: String?) {
        detailsLogic.repositoryName = repositoryName
        detailsLogic.userName = userName
        detailsLogic.reload()

        showList.accept(wideScreen)
        showDetails.accept(true)
    }

    override fun onBackPressed(): Boolean {
        if (wideScreen || showList.value == true) {
            closeApp.accept(Unit)
        } else {
            showList.accept(true)
            showDetails.accept(false)
        }
        return true
    }

    override fun create() {
        reload()
    }

    override fun destroy() {
        lastJob?.cancel()
    }
}
