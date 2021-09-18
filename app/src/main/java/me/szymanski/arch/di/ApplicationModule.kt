package me.szymanski.arch.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.szymanski.arch.Logger
import me.szymanski.arch.rest.RestConfig
import me.szymanski.arch.rest.RestModule
import javax.inject.Singleton
import me.szymanski.arch.BuildConfig

@Module(includes = [RestModule::class])
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideRestConfig(): RestConfig = RestConfig(
        baseUrl = BuildConfig.CONF_ENVIRONMENT,
        defaultUser = BuildConfig.CONF_USER,
        pageLimit = BuildConfig.CONF_PAGE_LIMIT,
        callTimeout = BuildConfig.CONF_REST_TIMEOUT
    )

    @Provides
    @Singleton
    fun provideLogger(): Logger = object : Logger {
        override fun log(message: String, t: Throwable?, tag: String?, level: Logger.Level) {
            val priority = when (level) {
                Logger.Level.VERBOSE -> Log.VERBOSE
                Logger.Level.DEBUG -> Log.DEBUG
                Logger.Level.INFO -> Log.INFO
                Logger.Level.WARN -> Log.WARN
                Logger.Level.ERROR -> Log.ERROR
            }
            val text = t?.let { "${t::class.simpleName} ${t.message} $message" } ?: message
            Log.println(priority, tag ?: "ArchTest", text)
        }
    }
}
