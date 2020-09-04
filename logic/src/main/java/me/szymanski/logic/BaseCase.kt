package me.szymanski.logic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.szymanski.gluekotlin.Case

open class BaseCase : Case {
    val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override var parent: Case? = null
    override fun create() = Unit
    override fun destroy() = Unit
}
