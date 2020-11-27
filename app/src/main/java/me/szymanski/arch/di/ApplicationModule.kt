package me.szymanski.arch.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.szymanski.arch.logic.Logger
import me.szymanski.arch.logic.rest.RestConfig
import me.szymanski.arch.logic.rest.RestModule
import javax.inject.Singleton

@Module(includes = [RestModule::class])
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideRestConfig(): RestConfig = RestConfig(
        baseUrl = "https://api.github.com/",
        defaultUser = "google",
        pageLimit = 20,
        callTimeout = 5000
    )

    @Provides
    @Singleton
    fun provideLogger(): Logger = object : Logger {
        override fun log(message: String, tag: String?, level: Logger.Level) {
            val priority = when (level) {
                Logger.Level.VERBOSE -> Log.VERBOSE
                Logger.Level.DEBUG -> Log.DEBUG
                Logger.Level.INFO -> Log.INFO
                Logger.Level.WARN -> Log.WARN
                Logger.Level.ERROR -> Log.ERROR
            }
            Log.println(priority, tag ?: "ArchTest", message)
        }

        override fun log(t: Throwable, tag: String?, level: Logger.Level) =
            this.log("${t.javaClass.simpleName} ${t.message}", tag, level)
    }
}
