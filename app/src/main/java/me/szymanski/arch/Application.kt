package me.szymanski.arch

import dagger.hilt.android.HiltAndroidApp
import me.szymanski.arch.logic.Logger
import javax.inject.Inject

@HiltAndroidApp
class Application : android.app.Application() {
    @Inject
    lateinit var logger: Logger
}
