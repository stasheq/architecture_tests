package me.szymanski.arch.logic.list.cases

import javax.inject.Inject
import me.szymanski.arch.logic.common.LoadPagedListCase
import me.szymanski.arch.logic.list.ListLogic
import me.szymanski.arch.rest.ApiError
import me.szymanski.arch.rest.Repository
import me.szymanski.arch.rest.RestApi
import me.szymanski.arch.rest.RestConfig

class GetReposListCase @Inject constructor(
    private val restApi: RestApi,
    private val restConfig: RestConfig
) : LoadPagedListCase<Repository, Int, ListLogic.ErrorType>(1) {

    var userName = restConfig.defaultUser
        set(value) {
            if (userName == value) return
            field = value
            if (value.isNotBlank()) loadNextPage(true)
        }

    override suspend fun getPage(page: Int): LoadingResult<Repository> {
        val result = restApi.getRepositories(userName, page)
        return LoadingResult(result, result.size == restConfig.pageLimit)
    }

    override fun mapError(e: ApiError): ListLogic.ErrorType = when {
        e is ApiError.HttpErrorResponse && e.code == 404 -> ListLogic.ErrorType.USER_DOESNT_EXIST
        e is ApiError.NoConnection -> ListLogic.ErrorType.NO_CONNECTION
        else -> ListLogic.ErrorType.OTHER
    }

    override fun nextPageInfo(page: Int): Int = page + 1
}