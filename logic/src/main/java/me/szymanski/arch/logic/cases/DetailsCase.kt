package me.szymanski.arch.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.szymanski.arch.logic.rest.ApiError
import me.szymanski.arch.logic.rest.RepositoryDetails
import me.szymanski.arch.logic.rest.RestApi
import me.szymanski.glue.BaseCase
import javax.inject.Inject

class DetailsCase @Inject constructor(private val restApi: RestApi) : BaseCase() {
    val loading: BehaviorRelay<LoadingState> = BehaviorRelay.create<LoadingState>()
    val result: BehaviorRelay<RepositoryDetails> = BehaviorRelay.create<RepositoryDetails>()
    private var lastJob: Job? = null

    fun reload(forceReload: Boolean = false) {
        lastJob?.cancel()
        val lastValue = result.value
        val repoName = (parent as? MainCase)?.selectedRepoName?.value?.get()
        if (lastValue != null && !forceReload && lastValue.name == repoName) return
        if (repoName == null) {
            loading.accept(LoadingState.ERROR)
            return
        }

        loading.accept(LoadingState.LOADING)
        lastJob = ioScope.launch {
            try {
                val item = restApi.getRepository(repoName)
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
