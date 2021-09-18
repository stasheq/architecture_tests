package me.szymanski.arch.logic.screenslogic

import javax.inject.Inject
import kotlinx.coroutines.flow.SharedFlow
import me.szymanski.arch.logic.cases.GetReposListCase
import me.szymanski.arch.rest.Repository

interface ListLogic {
    fun reload()
    fun loadNextPage()
    fun itemClick(repositoryName: String)
    var userName: String
    val list: SharedFlow<List<Repository>>
    val loading: SharedFlow<Boolean>
    val error: SharedFlow<ErrorType?>
    val hasNextPage: SharedFlow<Boolean>

    enum class ErrorType { USER_DOESNT_EXIST, NO_CONNECTION, OTHER }
}

class ListLogicImpl @Inject constructor(
    private val getReposListCase: GetReposListCase,
    private val navigationLogic: NavigationLogic,
    private val detailsLogic: DetailsLogic,
) : ListLogic {
    override val list = getReposListCase.list
    override val loading = getReposListCase.loading
    override val error = getReposListCase.error
    override val hasNextPage = getReposListCase.hasNextPage
    override var userName by getReposListCase::userName

    init {
        reload()
    }

    override fun reload() = getReposListCase.loadNextPage(true)

    override fun loadNextPage() = getReposListCase.loadNextPage(false)

    override fun itemClick(repositoryName: String) {
        detailsLogic.repositoryId = RepositoryId(userName, repositoryName)
        navigationLogic.openDetails()
    }
}
