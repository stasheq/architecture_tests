package me.szymanski.arch.logic.screenslogic

import me.szymanski.arch.logic.Logic
import me.szymanski.arch.logic.cases.GetReposListCase
import me.szymanski.arch.logic.rest.Repository
import me.szymanski.arch.logic.screenslogic.ListLogic.ShowViews.*
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

interface ListLogic : Logic {
    fun reload()
    fun loadNextPage()
    fun itemClick(detailsLogic: DetailsLogic, repositoryName: String?)
    var userName: String
    var wideScreen: Boolean
    val list: SharedFlow<List<Repository>>
    val loading: SharedFlow<Boolean>
    val error: SharedFlow<ErrorType?>
    val closeApp: SharedFlow<Unit>
    val showViews: SharedFlow<ShowViews>
    val hasNextPage: SharedFlow<Boolean>

    enum class ErrorType { USER_DOESNT_EXIST, NO_CONNECTION, OTHER }
    enum class ShowViews { LIST, DETAILS, BOTH }
}

class ListLogicImpl @Inject constructor(
    private val getReposListCase: GetReposListCase
) : ListLogic {
    private var itemClicked = false
    override val list = getReposListCase.list
    override val loading = getReposListCase.loading
    override val error = getReposListCase.error
    override val closeApp = MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val showViews = MutableStateFlow(LIST)
    override val hasNextPage = getReposListCase.hasNextPage
    override var userName by getReposListCase::userName
    override var wideScreen: Boolean = false
        set(value) {
            if (value) {
                showViews.value = BOTH
            } else {
                val lastValue = showViews.value
                showViews.value = if (lastValue == LIST || !itemClicked) LIST else DETAILS
            }
            field = value
        }

    override fun reload() = getReposListCase.loadNextPage(true)

    override fun loadNextPage() = getReposListCase.loadNextPage(false)

    override fun itemClick(detailsLogic: DetailsLogic, repositoryName: String?) {
        detailsLogic.repositoryName = repositoryName
        detailsLogic.userName = userName
        detailsLogic.reload()
        itemClicked = true
        showViews.value = if (wideScreen) BOTH else DETAILS
    }

    override fun onBackPressed(): Boolean {
        itemClicked = false
        if (showViews.value == BOTH || showViews.value == LIST) {
            closeApp.tryEmit(Unit)
        } else {
            showViews.value = LIST
        }
        return true
    }

    override fun create() {
        reload()
    }
}
