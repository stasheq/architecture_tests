package me.szymanski.arch.rest

import me.szymanski.arch.Logger
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException
import javax.inject.Inject

class RestApi @Inject internal constructor(
    private val restApiService: GitHubRestApiService,
    private val restConfig: RestConfig,
    private val logger: Logger
) {

    suspend fun getRepositories(user: String, page: Int) =
        call { restApiService.getRepositoriesList(user, restConfig.pageLimit, page) }

    suspend fun getRepository(user: String, repoName: String) =
        call { restApiService.getRepositoryDetails(user, repoName) }

    private suspend fun <T> call(request: suspend () -> T): T {
        try {
            return request()
        } catch (e: CancellationException) {
            logger.log("Cancelled API request")
            throw e
        } catch (e: HttpException) {
            logger.log("API call error", e, level = Logger.Level.DEBUG)
            throw ApiError.HttpErrorResponse(e, e.code())
        } catch (e: InterruptedIOException) {
            logger.log("API call error", e, level = Logger.Level.DEBUG)
            throw ApiError.NoConnection(e)
        } catch (e: UnknownHostException) {
            logger.log("API call error", e, level = Logger.Level.DEBUG)
            throw ApiError.NoConnection(e)
        } catch (e: Throwable) {
            logger.log("API call error", e, level = Logger.Level.DEBUG)
            throw ApiError.UnknownError(e)
        }
    }
}
