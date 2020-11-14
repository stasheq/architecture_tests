package me.szymanski.arch.logic.test.di

import dagger.Module
import dagger.Provides
import me.szymanski.arch.logic.Logger
import me.szymanski.arch.logic.cases.ListLogic
import me.szymanski.arch.logic.cases.ListLogicImpl

@Module
class TestModule {
    @Provides
    fun logger(): Logger = object : Logger {
        override fun log(message: String, tag: String?, level: Logger.Level) {
            tag?.let { print("$it ") }
            println(message)
        }

        override fun log(t: Throwable, tag: String?, level: Logger.Level) {
            tag?.let { print("$it ") }
            print("${t::class.simpleName} ")
            t.message?.let { println(it) }
        }
    }

    @Provides
    fun listLogic(logic: ListLogicImpl): ListLogic = logic
}
