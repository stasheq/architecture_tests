package me.szymanski.arch.di

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import me.szymanski.arch.logic.Logic
import javax.inject.Inject

open class LogicViewModel<L : Logic> constructor(
    val logic: L
) : ViewModel() {
    @Inject
    lateinit var coroutineScope: CoroutineScope

    init {
        logic.create()
    }

    override fun onCleared() {
        coroutineScope.cancel()
        logic.destroy()
    }
}
