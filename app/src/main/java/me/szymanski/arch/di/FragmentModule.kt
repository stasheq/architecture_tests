package me.szymanski.arch.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.job
import me.szymanski.arch.logic.details.DetailsLogic
import me.szymanski.arch.logic.details.DetailsLogicImpl
import me.szymanski.arch.logic.list.ListLogic
import me.szymanski.arch.logic.list.ListLogicImpl

@Module
@InstallIn(FragmentComponent::class)
class FragmentModule {

    @Provides
    @FragmentScoped
    fun coroutineScope(fragment: Fragment): CoroutineScope =
        CoroutineScope(SupervisorJob(fragment.lifecycleScope.coroutineContext.job) + Dispatchers.IO)
}

@Module
@InstallIn(FragmentComponent::class)
abstract class FragmentModuleBindings {

    @Binds
    @FragmentScoped
    abstract fun listLogic(listLogicImpl: ListLogicImpl): ListLogic

    @Binds
    @FragmentScoped
    abstract fun detailsLogic(detailsLogicImpl: DetailsLogicImpl): DetailsLogic
}
