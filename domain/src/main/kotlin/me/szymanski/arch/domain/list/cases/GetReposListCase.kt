package me.szymanski.arch.domain.list.cases

import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import me.szymanski.arch.domain.common.LoadPagedListCase
import me.szymanski.arch.domain.data.Repository
import me.szymanski.arch.domain.list.data.ErrorType
import me.szymanski.arch.rest.ApiError
import me.szymanski.arch.rest.RestApi
import me.szymanski.arch.rest.RestConfig

class GetReposListCase @Inject constructor(
    private val restApi: RestApi,
    private val restConfig: RestConfig
) : LoadPagedListCase<Repository, Int, ErrorType>(1) {

    val defaultUser = restConfig.defaultUser
    private var userName = restConfig.defaultUser

    fun onUserNameInput(scope: CoroutineScope, user: String) {
        if (userName == user) return
        userName = user
        if (user.isNotBlank()) loadNextPage(scope, true)
    }

    override suspend fun getPage(page: Int): LoadingResult<Repository> {
        val result = restApi.getRepositories(userName, page).map { Repository(it) }
        return LoadingResult(result, result.size == restConfig.pageLimit)
    }

    override fun mapError(e: ApiError): ErrorType = when {
        e is ApiError.HttpErrorResponse && e.code == 404 -> ErrorType.USER_DOESNT_EXIST
        e is ApiError.NoConnection -> ErrorType.NO_CONNECTION
        else -> ErrorType.OTHER
    }

    override fun nextPageInfo(page: Int): Int = page + 1
}