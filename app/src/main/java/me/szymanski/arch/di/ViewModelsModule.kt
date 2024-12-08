package me.szymanski.arch.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import me.szymanski.arch.domain.details.DetailsLogic
import me.szymanski.arch.domain.details.DetailsLogicImpl

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelsModule {

    @Provides
    @ViewModelScoped
    fun coroutineScope(viewModel: ViewModel): CoroutineScope = viewModel.viewModelScope
}

@Module
@InstallIn(FragmentComponent::class)
abstract class ViewModelsBindings {

    @Binds
    @ViewModelScoped
    abstract fun detailsLogic(detailsLogicImpl: DetailsLogicImpl): DetailsLogic
}
