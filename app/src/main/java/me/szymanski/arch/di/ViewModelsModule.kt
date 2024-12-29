package me.szymanski.arch.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import me.szymanski.arch.domain.details.DetailsLogic
import me.szymanski.arch.domain.details.DetailsLogicImpl
import me.szymanski.arch.domain.list.ListLogic
import me.szymanski.arch.domain.list.ListLogicImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelsBindings {

    @Binds
    @ViewModelScoped
    abstract fun detailsLogic(detailsLogicImpl: DetailsLogicImpl): DetailsLogic

    @Binds
    @ViewModelScoped
    abstract fun listLogic(detailsLogicImpl: ListLogicImpl): ListLogic
}
