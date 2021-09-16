package me.szymanski.arch.logic.test.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.szymanski.arch.logic.Logger
import me.szymanski.arch.logic.screenslogic.DetailsLogic
import me.szymanski.arch.logic.screenslogic.DetailsLogicImpl
import me.szymanski.arch.logic.screenslogic.ListLogic
import me.szymanski.arch.logic.screenslogic.ListLogicImpl

@Module
class TestModule {
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

    @Provides
    fun listLogic(logic: ListLogicImpl): ListLogic = logic

    @Provides
    fun detailsLogic(logic: DetailsLogicImpl): DetailsLogic = logic

    @Provides
    fun coroutineScope() = CoroutineScope(SupervisorJob() + Dispatchers.IO)
}
