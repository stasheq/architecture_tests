package me.szymanski.arch.logic.test.di

import dagger.Component
import me.szymanski.arch.logic.rest.RestModule
import me.szymanski.arch.logic.test.LogicContainer
import javax.inject.Singleton

@Singleton
@Component(modules = [RestModule::class, TestModule::class])
interface TestComponent {

    @Component.Builder
    interface Builder {
        fun build(): TestComponent
    }

    fun inject(case: LogicContainer)
}
