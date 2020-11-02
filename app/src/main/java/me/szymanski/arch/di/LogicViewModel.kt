package me.szymanski.arch.di

import androidx.lifecycle.ViewModel
import me.szymanski.glue.Logic

open class LogicViewModel<L : Logic> constructor(val logic: L) : ViewModel() {
    init {
        logic.create()
    }

    override fun onCleared() {
        logic.destroy()
    }
}
