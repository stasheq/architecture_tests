package me.szymanski.arch.logic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.szymanski.glue.Case

open class BaseCase : Case {
    val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override var parent: Case? = null
    override fun create() = Unit
    override fun destroy() = Unit
    override fun onSaveState(): String? = null
    override fun onRestoreState(state: String?) = Unit
}
