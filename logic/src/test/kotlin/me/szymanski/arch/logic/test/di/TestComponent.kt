package me.szymanski.arch.logic.test.di

import dagger.BindsInstance
import dagger.Component
import me.szymanski.arch.logic.screenslogic.DetailsLogic
import me.szymanski.arch.logic.screenslogic.ListLogic
import me.szymanski.arch.logic.rest.RestConfig
import me.szymanski.arch.logic.rest.RestModule
import javax.inject.Singleton
import me.szymanski.arch.logic.screenslogic.NavigationLogic

@Singleton
@Component(modules = [RestModule::class, TestModule::class])
interface TestComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun restConfig(config: RestConfig): Builder

        fun build(): TestComponent
    }

    fun getListLogic(): ListLogic

    fun getDetailsLogic(): DetailsLogic

    fun getNavigationLogic(): NavigationLogic
}
