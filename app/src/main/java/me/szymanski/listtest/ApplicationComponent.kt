package me.szymanski.listtest

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import me.szymanski.logic.cases.RepositoriesListCase
import me.szymanski.logic.rest.RestModule
import javax.inject.Singleton

@Singleton
@Component(modules = [RestModule::class])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        fun build(): ApplicationComponent
    }

    fun reposListViewModelFactory(): ViewModelFactory<GenericViewModel<RepositoriesListCase>>

}