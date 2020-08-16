package me.szymanski.listtest

import android.app.Activity

class Application : android.app.Application() {
    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.builder()
            .applicationContext(applicationContext)
            .build()
    }
}

val Activity.injector get() = (application as Application).component
