package me.szymanski.listtest

import android.app.Activity
import androidx.fragment.app.Fragment

class Application : android.app.Application() {
    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.builder()
            .applicationContext(applicationContext)
            .build()
    }
}

val Activity.component get() = (application as Application).component
val Fragment.component get() = requireActivity().component
