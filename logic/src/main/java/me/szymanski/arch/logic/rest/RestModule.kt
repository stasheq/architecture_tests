package me.szymanski.arch.logic.rest

import dagger.Module
import dagger.Provides
import me.szymanski.arch.logic.Logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
open class RestModule {

    @Provides
    @Singleton
    fun getApiService(config: RestConfig, logger: Logger): GitHubService =
        Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .client(getHttpClient(logger).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubService::class.java)

    private fun getHttpClient(logger: Logger): OkHttpClient.Builder {
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) = logger.log(message)
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(loggingInterceptor)
        return httpClient
    }
}
