package me.szymanski.arch.logic.cases

import me.szymanski.arch.logic.rest.ApiError
import me.szymanski.arch.logic.rest.Repository
import me.szymanski.arch.logic.rest.RestApi
import me.szymanski.arch.logic.rest.RestConfig
import me.szymanski.arch.logic.screenslogic.ListLogic
import javax.inject.Inject

class GetReposListCase @Inject constructor(
    private val restApi: RestApi,
    private val restConfig: RestConfig
) : LoadPagedListCase<Repository, ListLogic.ErrorType>() {

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
}
