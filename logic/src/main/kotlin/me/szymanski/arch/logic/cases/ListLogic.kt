package me.szymanski.arch.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.szymanski.arch.logic.Logger
import me.szymanski.arch.logic.Logic
import me.szymanski.arch.logic.Optional
import me.szymanski.arch.logic.rest.ApiError
import me.szymanski.arch.logic.rest.Repository
import me.szymanski.arch.logic.rest.RestApi
import me.szymanski.arch.logic.rest.RestConfig
import javax.inject.Inject

interface ListLogic : Logic {
    fun reload()
    fun loadNextPage()
    fun itemClick(detailsLogic: DetailsLogic, repositoryName: String?)
    var userName: String
    var wideScreen: Boolean
    val list: Observable<List<Repository>>
    val loading: Observable<Boolean>
    val error: Observable<Optional<ErrorType>>
    val closeApp: Observable<Unit>
    val showList: Observable<Boolean>
    val showDetails: Observable<Boolean>
    val hasNextPage: Observable<Boolean>

    enum class ErrorType { USER_DOESNT_EXIST, NO_CONNECTION, OTHER }
}

class ListLogicImpl @Inject constructor(
    private val restApi: RestApi,
    private val restConfig: RestConfig,
    private val logger: Logger
) : ListLogic {
    private val scope = instantiateCoroutineScope()
    private val loadedItems = ArrayList<Repository>()
    private val firstPage = 1
    private var currentPage = firstPage
    private var lastJob: Job? = null
    override val list: BehaviorRelay<List<Repository>> = BehaviorRelay.create<List<Repository>>()
    override val loading: BehaviorRelay<Boolean> = BehaviorRelay.create<Boolean>()
    override val error: BehaviorRelay<Optional<ListLogic.ErrorType>> = BehaviorRelay.create()
    override val closeApp: PublishRelay<Unit> = PublishRelay.create<Unit>()
    override val showList: BehaviorRelay<Boolean> = BehaviorRelay.createDefault<Boolean>(true)
    override val showDetails: BehaviorRelay<Boolean> = BehaviorRelay.createDefault<Boolean>(false)
    override val hasNextPage: BehaviorRelay<Boolean> = BehaviorRelay.create<Boolean>()
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

    override fun reload() = loadNextPage(true)

    override fun loadNextPage() = loadNextPage(false)

    private fun loadNextPage(fromFirstPage: Boolean = false) {
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
            fun loadingFinished(items: List<Repository>, errorType: ListLogic.ErrorType?) {
                if (!isActive) return
                if (fromFirstPage && errorType == null) loadedItems.clear()
                loadedItems.addAll(items)
                list.accept(loadedItems)
                error.accept(Optional(errorType))
                loading.accept(false)
                hasNextPage.accept(items.size == restConfig.pageLimit)
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
