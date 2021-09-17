package me.szymanski.arch.logic.test.di

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.szymanski.arch.logic.Logger
import me.szymanski.arch.logic.screenslogic.DetailsLogic
import me.szymanski.arch.logic.screenslogic.DetailsLogicImpl
import me.szymanski.arch.logic.screenslogic.ListLogic
import me.szymanski.arch.logic.screenslogic.ListLogicImpl
import me.szymanski.arch.logic.screenslogic.NavigationLogic
import me.szymanski.arch.logic.screenslogic.NavigationLogicImpl

@Module
class TestModule {
    @Singleton
    @Provides
    fun logger(): Logger = object : Logger {

        override fun log(message: String, t: Throwable?, tag: String?, level: Logger.Level) {
            tag?.let { print("$it ") }
            t?.let {
                print("${it::class.simpleName} ")
                it.message?.let { msg -> println(msg) }
            }
            println(message)
        }
    }

    @Singleton
    @Provides
    fun coroutineScope() = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Singleton
    @Provides
    fun listLogic(logic: ListLogicImpl): ListLogic = logic

    @Singleton
    @Provides
    fun detailsLogic(logic: DetailsLogicImpl): DetailsLogic = logic

    @Singleton
    @Provides
    fun navigationLogic(logic: NavigationLogicImpl): NavigationLogic = logic
}
