package me.szymanski.arch.logic.screenslogic

import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.cancel
import me.szymanski.arch.logic.Logic
import me.szymanski.arch.logic.Optional
import me.szymanski.arch.logic.cases.GetReposListCase
import me.szymanski.arch.logic.rest.Repository
import me.szymanski.arch.logic.screenslogic.ListLogic.ShowViews.*
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
    val showViews: Observable<ShowViews>
    val hasNextPage: Observable<Boolean>

    enum class ErrorType { USER_DOESNT_EXIST, NO_CONNECTION, OTHER }
    enum class ShowViews { LIST, DETAILS, BOTH }
}

class ListLogicImpl @Inject constructor(
    private val getReposListCase: GetReposListCase
) : ListLogic {
    private val scope = instantiateCoroutineScope()
    private var itemClicked = false
    override val list = getReposListCase.list
    override val loading = getReposListCase.loading
    override val error = getReposListCase.error
    override val closeApp: PublishRelay<Unit> = PublishRelay.create()
    override val showViews: BehaviorRelay<ListLogic.ShowViews> = BehaviorRelay.create()
    override val hasNextPage = getReposListCase.hasNextPage
    override var userName by getReposListCase::userName
    override var wideScreen: Boolean = false
        set(value) {
            if (value) {
                showViews.accept(BOTH)
            } else {
                val lastValue: ListLogic.ShowViews? = showViews.value
                showViews.accept(if (lastValue == null || lastValue == LIST || !itemClicked) LIST else DETAILS)
            }
            field = value
        }

    init {
        getReposListCase.scope = scope
    }

    override fun reload() = getReposListCase.loadNextPage(true)

    override fun loadNextPage() = getReposListCase.loadNextPage(false)

    override fun itemClick(detailsLogic: DetailsLogic, repositoryName: String?) {
        detailsLogic.repositoryName = repositoryName
        detailsLogic.userName = userName
        detailsLogic.reload()
        itemClicked = true
        showViews.accept(if (wideScreen) BOTH else DETAILS)
    }

    override fun onBackPressed(): Boolean {
        itemClicked = false
        if (showViews.value == BOTH || showViews.value == LIST) {
            closeApp.accept(Unit)
        } else {
            showViews.accept(LIST)
        }
        return true
    }

    override fun create() {
        reload()
    }

    override fun destroy() = scope.cancel()
}
