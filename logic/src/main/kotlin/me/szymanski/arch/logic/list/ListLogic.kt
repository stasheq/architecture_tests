package me.szymanski.arch.logic.list

import javax.inject.Inject
import kotlinx.coroutines.flow.SharedFlow
import me.szymanski.arch.logic.list.cases.GetReposListCase
import me.szymanski.arch.logic.navigation.NavigationLogic
import me.szymanski.arch.logic.details.RepositoryId
import me.szymanski.arch.rest.models.Repository

interface ListLogic {
    fun reload()
    fun loadNextPage()
    fun itemClick(repository: Repository)
    var userName: String
    val list: SharedFlow<List<Repository>?>
    val loading: SharedFlow<Boolean>
    val error: SharedFlow<ErrorType?>
    val hasNextPage: SharedFlow<Boolean>

    enum class ErrorType { USER_DOESNT_EXIST, NO_CONNECTION, OTHER }
}

class ListLogicImpl @Inject constructor(
    private val getReposListCase: GetReposListCase,
    private val navigationLogic: NavigationLogic
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

    override fun itemClick(repository: Repository) {
        navigationLogic.openDetails(RepositoryId(userName, repository.name))
    }
}
