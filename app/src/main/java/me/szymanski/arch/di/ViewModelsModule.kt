package me.szymanski.arch.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import me.szymanski.arch.domain.details.DetailsLogic
import me.szymanski.arch.domain.details.DetailsLogicImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelsBindings {

    @Binds
    @ViewModelScoped
    abstract fun detailsLogic(detailsLogicImpl: DetailsLogicImpl): DetailsLogic
}
