package me.szymanski.arch.logic.cases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.szymanski.arch.Logger
import me.szymanski.arch.rest.ApiError
import javax.inject.Inject

abstract class LoadPagedListCase<T, E> {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var scope: CoroutineScope

    val error = MutableStateFlow<E?>(null)
    val list = MutableStateFlow(emptyList<T>())
    val loading = MutableStateFlow(true)
    val hasNextPage = MutableStateFlow(false)
    private val loadedItems = mutableListOf<T>()
    private val firstPage = 1
    private var currentPage = firstPage
    private var lastJob: Job? = null

    abstract suspend fun getPage(page: Int): LoadingResult<T>

    abstract fun mapError(e: ApiError): E

    fun loadNextPage(fromFirstPage: Boolean = false) {
        if (fromFirstPage) {
            lastJob?.cancel()
            loading.value = true
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
                list.value = ArrayList(loadedItems)
                error.value = errorType
                loading.value = false
                hasNextPage.value = hasNext
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
