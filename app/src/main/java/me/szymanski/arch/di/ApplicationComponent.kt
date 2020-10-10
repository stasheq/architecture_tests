package me.szymanski.arch.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import me.szymanski.arch.Application
import me.szymanski.arch.DetailsFragment
import me.szymanski.arch.ListFragment
import me.szymanski.arch.MainActivity
import me.szymanski.arch.logic.rest.RestModule
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
    fun inject(activity: MainActivity)
    fun inject(fragment: ListFragment)
    fun inject(fragment: DetailsFragment)
}
