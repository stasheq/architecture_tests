package me.szymanski.arch.logic

import me.szymanski.arch.logic.cases.ListLogic
import me.szymanski.arch.logic.cases.ListLogicImpl
import org.koin.core.context.startKoin
import org.koin.dsl.module

val logicModule = module {
    single<ListLogic> { ListLogicImpl(get(), get(), get()) }
}

val koin = startKoin {
    logicModule
}.koin
