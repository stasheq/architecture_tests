package me.szymanski.arch.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import me.szymanski.arch.Application
import me.szymanski.arch.logic.cases.DetailsCase
import me.szymanski.arch.logic.cases.MainCase
import me.szymanski.arch.logic.cases.RepositoriesListCase
import me.szymanski.arch.logic.rest.RestModule
import me.szymanski.glue.GenericViewModel
import me.szymanski.glue.ViewModelFactory
import javax.inject.Singleton

@Singleton
@Component(modules = [RestModule::class, ApplicationModule::class])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        fun build(): ApplicationComponent
    }

    fun inject(application: Application)

    fun mainVMFactory(): ViewModelFactory<GenericViewModel<MainCase>>
    fun reposListVMFactory(): ViewModelFactory<GenericViewModel<RepositoriesListCase>>
    fun detailsVMFactory(): ViewModelFactory<GenericViewModel<DetailsCase>>
}
