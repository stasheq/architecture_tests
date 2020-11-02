package me.szymanski.arch.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import me.szymanski.arch.logic.cases.*

@Module
@InstallIn(ActivityRetainedComponent::class)
class ActivityRetainedModule {

    @Provides
    @ActivityRetainedScoped
    fun mainLogic(mainLogic: MainLogicImpl): MainLogic = mainLogic

    @Provides
    @ActivityRetainedScoped
    fun listLogic(listLogic: ListLogicImpl): ListLogic = listLogic

    @Provides
    @ActivityRetainedScoped
    fun detailsLogic(detailsLogic: DetailsLogicImpl): DetailsLogic = detailsLogic
}
