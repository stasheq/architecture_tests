package me.szymanski.arch.logic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

open class BaseCase : Case {
    val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override var parent: Case? = null
    override fun create() = Unit
    override fun destroy() = Unit
}
