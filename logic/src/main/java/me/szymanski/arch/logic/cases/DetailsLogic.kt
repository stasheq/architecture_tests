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
    fun reload(forceReload: Boolean = false)
    val result: Observable<RepositoryDetails>
    val state: Observable<LoadingState>

    enum class LoadingState { LOADING, ERROR, SUCCESS }
}

class DetailsLogicImpl @Inject constructor(private val restApi: RestApi) : DetailsLogic {
    private val scope = instantiateCoroutineScope()
    override val state: BehaviorRelay<DetailsLogic.LoadingState> = BehaviorRelay.create<DetailsLogic.LoadingState>()
    override val result: BehaviorRelay<RepositoryDetails> = BehaviorRelay.create<RepositoryDetails>()
    private var lastJob: Job? = null
    override var repositoryName: String? = null
    override var userName: String? = null

    override fun reload(forceReload: Boolean) {
        lastJob?.cancel()
        val lastValue = result.value
        if (lastValue != null && !forceReload && lastValue.name == repositoryName) return
        val repositoryName = this.repositoryName
        val userName = this.userName
        if (repositoryName == null || userName == null) {
            state.accept(DetailsLogic.LoadingState.ERROR)
            return
        }

        state.accept(DetailsLogic.LoadingState.LOADING)
        lastJob = scope.launch {
            try {
                val item = restApi.getRepository(userName, repositoryName)
                state.accept(DetailsLogic.LoadingState.SUCCESS)
                result.accept(item)
            } catch (e: ApiError) {
                state.accept(DetailsLogic.LoadingState.ERROR)
            }
        }
    }

    override fun destroy() {
        lastJob?.cancel()
    }
}
