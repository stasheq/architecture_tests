package me.szymanski.arch.domain.list

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import me.szymanski.arch.domain.data.Repository
import me.szymanski.arch.domain.list.cases.GetReposListCase
import me.szymanski.arch.domain.list.data.ErrorType
import me.szymanski.arch.domain.navigation.NavigationCoordinator
import javax.inject.Inject

interface ListLogic {
    fun reload(scope: CoroutineScope)
    fun loadNextPage(scope: CoroutineScope)
    fun itemClick(repository: Repository)
    fun onUserNameInput(scope: CoroutineScope, user: String)
    val defaultUser: String
    val list: StateFlow<List<Repository>?>
    val loading: StateFlow<Boolean>
    val error: StateFlow<ErrorType?>
    val hasNextPage: StateFlow<Boolean>
}

class ListLogicImpl @Inject constructor(
    private val getReposListCase: GetReposListCase,
    private val navigationCoordinator: NavigationCoordinator
) : ListLogic {
    override val list = getReposListCase.list
    override val loading = getReposListCase.loading
    override val error = getReposListCase.error
    override val hasNextPage = getReposListCase.hasNextPage
    override val defaultUser = getReposListCase.defaultUser

    override fun reload(scope: CoroutineScope) = getReposListCase.loadNextPage(scope, true)

    override fun loadNextPage(scope: CoroutineScope) = getReposListCase.loadNextPage(scope, false)

    override fun itemClick(repository: Repository) {
        navigationCoordinator.openDetails(repository)
    }

    override fun onUserNameInput(scope: CoroutineScope, user: String) {
        getReposListCase.onUserNameInput(scope, user)
    }
}
