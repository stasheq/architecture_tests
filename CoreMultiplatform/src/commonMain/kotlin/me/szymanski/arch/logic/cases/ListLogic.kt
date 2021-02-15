package me.szymanski.arch.logic.cases

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.szymanski.arch.logic.*
import me.szymanski.arch.logic.rest.ApiError
import me.szymanski.arch.logic.rest.Repository
import me.szymanski.arch.logic.rest.RestApi
import me.szymanski.arch.logic.rest.RestConfig
import me.szymanski.arch.logic.cases.ListLogic.ShowViews.*

interface ListLogic : Logic {
    fun reload()
    fun loadNextPage()
    fun itemClick(detailsLogic: DetailsLogic, repositoryName: String?)
    var userName: String
    var wideScreen: Boolean
    val list: Flow<List<Repository>>
    val loading: Flow<Boolean>
    val error: Flow<ErrorType?>
    val closeApp: Flow<Unit>
    val showViews: Flow<ShowViews>
    val hasNextPage: Flow<Boolean>

    enum class ErrorType { USER_DOESNT_EXIST, NO_CONNECTION, OTHER }
    enum class ShowViews { LIST, DETAILS, BOTH }
}

class ListLogicImpl(
    private val restApi: RestApi,
    private val restConfig: RestConfig,
    private val logger: Logger
) : ListLogic {
    private val scope = instantiateCoroutineScope()
    private val loadedItems = ArrayList<Repository>()
    private val firstPage = 1
    private var currentPage = firstPage
    private var lastJob: Job? = null
    private var itemClicked = false
    override val list = replayFlow<List<Repository>>()
    override val loading = replayFlow<Boolean>()
    override val error = replayFlow<ListLogic.ErrorType?>()
    override val closeApp = publishFlow<Unit>()
    override val showViews = replayFlow<ListLogic.ShowViews>()
    override val hasNextPage = replayFlow<Boolean>()
    override var userName = restConfig.defaultUser
        set(value) {
            if (field == value) return
            field = value
            if (value.isNotBlank()) reload()
        }
    override var wideScreen: Boolean = false
        set(value) {
            if (value) {
                showViews.publish(BOTH)
            } else {
                val lastValue: ListLogic.ShowViews? = showViews.lastValue
                showViews.publish(if (lastValue == null || lastValue == LIST || !itemClicked) LIST else DETAILS)
            }
            field = value
        }

    override fun reload() = loadNextPage(true)

    override fun loadNextPage() = loadNextPage(false)

    private fun loadNextPage(fromFirstPage: Boolean = false) {
        if (fromFirstPage) {
            lastJob?.cancel()
            loading.publish(true)
            currentPage = firstPage
        }
        if (lastJob?.isActive == true) {
            logger.log("Not loading next page because previous loading is not finished")
            return
        }
        lastJob = scope.launch {
            fun loadingFinished(items: List<Repository>, errorType: ListLogic.ErrorType?) {
                if (!isActive) return
                if (fromFirstPage && errorType == null) loadedItems.clear()
                loadedItems.addAll(items)
                list.publish(loadedItems)
                error.publish(errorType)
                loading.publish(false)
                hasNextPage.publish(items.size == restConfig.pageLimit)
            }

            try {
                val items = restApi.getRepositories(userName, currentPage)
                currentPage++
                loadingFinished(items, null)
            } catch (e: ApiError) {
                val errorType = when {
                    e is ApiError.HttpErrorResponse && e.code == 404 -> ListLogic.ErrorType.USER_DOESNT_EXIST
                    e is ApiError.NoConnection -> ListLogic.ErrorType.NO_CONNECTION
                    else -> ListLogic.ErrorType.OTHER
                }
                loadingFinished(emptyList(), errorType)
            }
        }
    }

    override fun itemClick(detailsLogic: DetailsLogic, repositoryName: String?) {
        detailsLogic.repositoryName = repositoryName
        detailsLogic.userName = userName
        detailsLogic.reload()
        itemClicked = true
        showViews.publish(if (wideScreen) BOTH else DETAILS)
    }

    override fun onBackPressed(): Boolean {
        itemClicked = false
        if (showViews.lastValue == BOTH || showViews.lastValue == LIST) {
            closeApp.publish(Unit)
        } else {
            showViews.publish(LIST)
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
