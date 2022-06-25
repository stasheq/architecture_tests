package me.szymanski.arch.domain.test.di

import dagger.BindsInstance
import dagger.Component
import me.szymanski.arch.domain.details.DetailsLogic
import me.szymanski.arch.domain.list.ListLogic
import me.szymanski.arch.rest.RestConfig
import me.szymanski.arch.rest.RestModule
import javax.inject.Singleton
import me.szymanski.arch.domain.navigation.NavigationCoordinator

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

    fun getNavigationCoordinator(): NavigationCoordinator
}
