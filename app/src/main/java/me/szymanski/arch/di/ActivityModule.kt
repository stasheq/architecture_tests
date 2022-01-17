package me.szymanski.arch.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import me.szymanski.arch.logic.navigation.NavigationLogic
import me.szymanski.arch.logic.navigation.NavigationLogicImpl

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule

@Module
@InstallIn(ActivityComponent::class)
abstract class ActivityModuleBindings {

    @Binds
    @ActivityScoped
    abstract fun navigationLogic(navigationLogicImpl: NavigationLogicImpl): NavigationLogic
}
