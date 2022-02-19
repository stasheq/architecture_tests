package me.szymanski.arch.rest

import dagger.Module
import dagger.Provides
import me.szymanski.arch.Logger
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
    internal fun getApiService(config: RestConfig, okHttpClient: OkHttpClient): GitHubRestApiService =
        Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubRestApiService::class.java)

    @Provides
    @Singleton
    internal fun getHttpClient(config: RestConfig, logger: Logger): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.callTimeout(config.callTimeout.toLong(), TimeUnit.MILLISECONDS)
        if (config.logEnabled) {
            val loggingInterceptor = HttpLoggingInterceptor { message -> logger.log(message) }
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(loggingInterceptor)
        }
        return httpClient.build()
    }
}
