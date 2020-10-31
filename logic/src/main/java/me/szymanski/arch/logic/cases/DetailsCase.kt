package me.szymanski.arch.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.szymanski.arch.logic.rest.ApiError
import me.szymanski.arch.logic.rest.RepositoryDetails
import me.szymanski.arch.logic.rest.RestApi
import me.szymanski.glue.CaseTemplate
import javax.inject.Inject

class DetailsCase @Inject constructor(private val restApi: RestApi) : CaseTemplate() {
    val loading: BehaviorRelay<LoadingState> = BehaviorRelay.create<LoadingState>()
    val result: BehaviorRelay<RepositoryDetails> = BehaviorRelay.create<RepositoryDetails>()
    private var lastJob: Job? = null

    fun reload(forceReload: Boolean = false) {
        lastJob?.cancel()
        val lastValue = result.value
        val parentMainCase = (parent as? MainCase)
        val repoName = parentMainCase?.selectedRepoName?.value?.get()
        val userName = parentMainCase?.userName?.value
        if (lastValue != null && !forceReload && lastValue.name == repoName) return
        if (repoName == null || userName == null) {
            loading.accept(LoadingState.ERROR)
            return
        }

        loading.accept(LoadingState.LOADING)
        lastJob = ioScope.launch {
            try {
                val item = restApi.getRepository(userName, repoName)
                loading.accept(LoadingState.SUCCESS)
                result.accept(item)
            } catch (e: ApiError) {
                loading.accept(LoadingState.ERROR)
            }
        }
    }

    override fun destroy() {
        lastJob?.cancel()
    }

    override fun onBackPressed(): Boolean {
        if (super.onBackPressed()) return true
        parent.let {
            if (it is MainCase) {
                it.detailsBackPressed()
                return true
            }
            return false
        }
    }

    enum class LoadingState { LOADING, ERROR, SUCCESS }
}
