package me.szymanski.arch.domain.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.szymanski.arch.Logger
import me.szymanski.arch.rest.ApiError
import javax.inject.Inject

abstract class LoadPagedListCase<Item, Page, Error>(private val firstPageInfo: Page) {
    @Inject
    lateinit var logger: Logger

    val error = MutableStateFlow<Error?>(null)
    val list = MutableStateFlow<List<Item>?>(null)
    val loading = MutableStateFlow(true)
    val hasNextPage = MutableStateFlow(false)
    private val loadedItems = mutableListOf<Item>()
    private var currentPage = firstPageInfo
    private var lastJob: Job? = null

    abstract fun nextPageInfo(page: Page): Page

    abstract suspend fun getPage(page: Page): LoadingResult<Item>

    abstract fun mapError(e: ApiError): Error

    fun loadNextPage(scope: CoroutineScope, fromFirstPage: Boolean = false) {
        if (fromFirstPage) {
            lastJob?.cancel()
            loading.value = true
            currentPage = firstPageInfo
        }
        if (lastJob?.isActive == true) {
            logger.log("Not loading next page because previous loading is not finished")
            return
        }
        lastJob = scope.launch {
            fun loadingFinished(results: List<Item>, errorType: Error?, hasNext: Boolean) {
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
                currentPage = nextPageInfo(currentPage)
                loadingFinished(result.results, null, result.hasNext)
            } catch (e: ApiError) {
                logger.log("Loading page failed", e)
                loadingFinished(emptyList(), mapError(e), false)
            }
        }
    }

    data class LoadingResult<T>(val results: List<T>, val hasNext: Boolean)
}
