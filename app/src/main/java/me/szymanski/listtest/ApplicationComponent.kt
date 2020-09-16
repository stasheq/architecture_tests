package me.szymanski.listtest

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import me.szymanski.glueandroid.GenericViewModel
import me.szymanski.glueandroid.ViewModelFactory
import me.szymanski.logic.cases.RepositoriesListCase
import me.szymanski.logic.rest.RestModule
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

    fun reposListViewModelFactory(): ViewModelFactory<GenericViewModel<RepositoriesListCase>>
}
