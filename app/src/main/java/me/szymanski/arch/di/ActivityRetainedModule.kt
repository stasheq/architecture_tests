package me.szymanski.arch.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import me.szymanski.arch.domain.navigation.NavigationCoordinator
import me.szymanski.arch.domain.navigation.NavigationCoordinatorImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
class ActivityRetainedModule

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityRetainedModuleBindings {

    @Binds
    @ActivityRetainedScoped
    abstract fun navigationCoordinator(navigationCoordinatorImpl: NavigationCoordinatorImpl): NavigationCoordinator
}
