package me.szymanski.arch.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.szymanski.arch.logic.screenslogic.DetailsLogic
import me.szymanski.arch.logic.screenslogic.DetailsLogicImpl
import me.szymanski.arch.logic.screenslogic.ListLogic
import me.szymanski.arch.logic.screenslogic.ListLogicImpl
import me.szymanski.arch.logic.screenslogic.NavigationLogic
import me.szymanski.arch.logic.screenslogic.NavigationLogicImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
class ActivityRetainedModule {

    @Provides
    @ActivityRetainedScoped
    fun coroutineScope() = CoroutineScope(SupervisorJob() + Dispatchers.IO)
}

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityRetainedModuleBindings {

    @Binds
    @ActivityRetainedScoped
    abstract fun navigationLogic(navigationLogicImpl: NavigationLogicImpl): NavigationLogic

    @Binds
    @ActivityRetainedScoped
    abstract fun listLogic(listLogicImpl: ListLogicImpl): ListLogic

    @Binds
    @ActivityRetainedScoped
    abstract fun detailsLogic(detailsLogicImpl: DetailsLogicImpl): DetailsLogic
}
