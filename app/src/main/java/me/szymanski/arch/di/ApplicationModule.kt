package me.szymanski.arch.di

import android.util.Log
import dagger.Module
import dagger.Provides
import me.szymanski.arch.logic.Logger
import me.szymanski.arch.logic.rest.RestConfig

@Module
class ApplicationModule {

    @Provides
    fun provideRestConfig(): RestConfig = RestConfig("https://api.github.com/", "google", 20)

    @Provides
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
    }
}
