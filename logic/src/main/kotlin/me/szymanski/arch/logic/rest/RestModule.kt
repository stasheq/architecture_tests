package me.szymanski.arch.logic.rest

import dagger.Module
import dagger.Provides
import me.szymanski.arch.logic.Logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class RestModule {

    @Provides
    @Singleton
    fun getApiService(config: RestConfig, okHttpClient: OkHttpClient): GitHubService =
        Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubService::class.java)

    @Provides
    @Singleton
    fun getHttpClient(config: RestConfig, logger: Logger): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) = logger.log(message)
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        val httpClient = OkHttpClient.Builder()
        httpClient.callTimeout(config.callTimeout.toLong(), TimeUnit.MILLISECONDS)
        httpClient.addInterceptor(loggingInterceptor)
        return httpClient.build()
    }
}
