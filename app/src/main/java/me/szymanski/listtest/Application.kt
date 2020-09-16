package me.szymanski.listtest

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import me.szymanski.logic.Logger
import javax.inject.Inject

class Application : android.app.Application() {
    lateinit var component: ApplicationComponent

    @Inject
    lateinit var logger: Logger

    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.builder()
            .applicationContext(applicationContext)
            .build()
        component.inject(this)
    }
}

val Activity.component get() = (application as Application).component
val Fragment.component get() = requireActivity().component
fun Context.log(msg: String) = (applicationContext as? Application)?.logger?.log(msg)
fun Fragment.log(msg: String) = context?.log(msg)
