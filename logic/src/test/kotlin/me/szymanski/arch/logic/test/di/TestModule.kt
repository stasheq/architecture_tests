package me.szymanski.arch.logic.test.di

import dagger.Module
import dagger.Provides
import me.szymanski.arch.logic.Logger
import me.szymanski.arch.logic.cases.DetailsLogic
import me.szymanski.arch.logic.cases.DetailsLogicImpl
import me.szymanski.arch.logic.cases.ListLogic
import me.szymanski.arch.logic.cases.ListLogicImpl

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
}
