package me.szymanski.arch.di

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.szymanski.arch.Logger
import me.szymanski.arch.rest.RestConfig
import me.szymanski.arch.rest.RestModule
import javax.inject.Singleton
import me.szymanski.arch.BuildConfig
import me.szymanski.arch.R

@Module(includes = [RestModule::class])
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideRestConfig(): RestConfig = RestConfig(
        baseUrl = BuildConfig.CONF_ENVIRONMENT,
        defaultUser = BuildConfig.CONF_USER,
        pageLimit = BuildConfig.CONF_PAGE_LIMIT,
        callTimeout = BuildConfig.CONF_REST_TIMEOUT,
        logEnabled = BuildConfig.CONF_API_LOG
    )

    @Provides
    @Singleton
    fun provideLogger(@ApplicationContext context: Context): Logger = object : Logger {
        override fun log(message: String, t: Throwable?, tag: String?, level: Logger.Level) {
            val priority = when (level) {
                Logger.Level.VERBOSE -> Log.VERBOSE
                Logger.Level.DEBUG -> Log.DEBUG
                Logger.Level.INFO -> Log.INFO
                Logger.Level.WARN -> Log.WARN
                Logger.Level.ERROR -> Log.ERROR
            }
            val text = t?.let { "${t::class.simpleName} ${t.message} $message" } ?: message
            Log.println(priority, tag ?: context.getString(R.string.app_name), text)
        }
    }
}
