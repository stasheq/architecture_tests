package me.szymanski.logic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

open class Case {
    val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    open fun create() = Unit

    open fun destroy() = Unit
}
