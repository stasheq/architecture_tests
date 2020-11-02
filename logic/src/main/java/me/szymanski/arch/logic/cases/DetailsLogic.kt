package me.szymanski.arch.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.szymanski.arch.logic.rest.ApiError
import me.szymanski.arch.logic.rest.RepositoryDetails
import me.szymanski.arch.logic.rest.RestApi
import me.szymanski.glue.Logic
import me.szymanski.glue.LogicTemplate
import javax.inject.Inject

interface DetailsLogic : Logic {
    fun reload(forceReload: Boolean = false)
    val result: Observable<RepositoryDetails>
    val loading: Observable<LoadingState>

    enum class LoadingState { LOADING, ERROR, SUCCESS }
}

class DetailsLogicImpl @Inject constructor(private val restApi: RestApi) : LogicTemplate(), DetailsLogic {
    override val loading: BehaviorRelay<DetailsLogic.LoadingState> = BehaviorRelay.create<DetailsLogic.LoadingState>()
    override val result: BehaviorRelay<RepositoryDetails> = BehaviorRelay.create<RepositoryDetails>()
    private var lastJob: Job? = null

    override fun reload(forceReload: Boolean) {
        lastJob?.cancel()
        val lastValue = result.value
        val parentMainCase = (parent as? MainLogicImpl)
        val repoName = parentMainCase?.selectedRepoName?.value?.get()
        val userName = parentMainCase?.userName?.value
        if (lastValue != null && !forceReload && lastValue.name == repoName) return
        if (repoName == null || userName == null) {
            loading.accept(DetailsLogic.LoadingState.ERROR)
            return
        }

        loading.accept(DetailsLogic.LoadingState.LOADING)
        lastJob = ioScope.launch {
            try {
                val item = restApi.getRepository(userName, repoName)
                loading.accept(DetailsLogic.LoadingState.SUCCESS)
                result.accept(item)
            } catch (e: ApiError) {
                loading.accept(DetailsLogic.LoadingState.ERROR)
            }
        }
    }

    override fun destroy() {
        lastJob?.cancel()
    }

    override fun onBackPressed(): Boolean {
        if (super.onBackPressed()) return true
        parent.let {
            if (it is MainLogic) {
                it.detailsBackPressed()
                return true
            }
            return false
        }
    }
}
