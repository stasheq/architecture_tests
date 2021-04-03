package me.szymanski.arch.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.Relay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.szymanski.arch.logic.Logger
import me.szymanski.arch.logic.Optional
import me.szymanski.arch.logic.rest.ApiError
import javax.inject.Inject

abstract class LoadPagedListCase<T, E> {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var scope: CoroutineScope

    val error: Relay<Optional<E>> = BehaviorRelay.create()
    val list: Relay<List<T>> = BehaviorRelay.create()
    val loading: Relay<Boolean> = BehaviorRelay.create()
    val hasNextPage: Relay<Boolean> = BehaviorRelay.create()
    private val loadedItems = mutableListOf<T>()
    private val firstPage = 1
    private var currentPage = firstPage
    private var lastJob: Job? = null

    abstract suspend fun getPage(page: Int): LoadingResult<T>

    abstract fun mapError(e: ApiError): E

    fun loadNextPage(fromFirstPage: Boolean = false) {
        if (fromFirstPage) {
            lastJob?.cancel()
            loading.accept(true)
            currentPage = firstPage
        }
        if (lastJob?.isActive == true) {
            logger.log("Not loading next page because previous loading is not finished")
            return
        }
        lastJob = scope.launch {
            fun loadingFinished(results: List<T>, errorType: E?, hasNext: Boolean) {
                if (!isActive) return
                if (fromFirstPage && errorType == null) loadedItems.clear()
                loadedItems.addAll(results)
                list.accept(loadedItems)
                error.accept(Optional(errorType))
                loading.accept(false)
                hasNextPage.accept(hasNext)
            }

            try {
                val result = getPage(currentPage)
                currentPage++
                loadingFinished(result.results, null, result.hasNext)
            } catch (e: ApiError) {
                logger.log("Loading page failed", e)
                loadingFinished(emptyList(), mapError(e), false)
            }
        }
    }

    data class LoadingResult<T>(val results: List<T>, val hasNext: Boolean)
}
