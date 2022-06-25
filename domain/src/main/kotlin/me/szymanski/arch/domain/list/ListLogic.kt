package me.szymanski.arch.domain.list

import javax.inject.Inject
import kotlinx.coroutines.flow.SharedFlow
import me.szymanski.arch.domain.data.Repository
import me.szymanski.arch.domain.data.RepositoryId
import me.szymanski.arch.domain.list.cases.GetReposListCase
import me.szymanski.arch.domain.navigation.NavigationCoordinator
import me.szymanski.arch.domain.list.data.ErrorType

interface ListLogic {
    fun reload()
    fun loadNextPage()
    fun itemClick(repository: Repository)
    var userName: String
    val list: SharedFlow<List<Repository>?>
    val loading: SharedFlow<Boolean>
    val error: SharedFlow<ErrorType?>
    val hasNextPage: SharedFlow<Boolean>
}

class ListLogicImpl @Inject constructor(
    private val getReposListCase: GetReposListCase,
    private val navigationCoordinator: NavigationCoordinator
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
        navigationCoordinator.openDetails(RepositoryId(userName, repository.name))
    }
}
