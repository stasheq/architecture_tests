package me.szymanski.arch.logic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

interface Logic {
    fun create() = Unit
    fun destroy() = Unit
    fun onSaveState(): String? = null
    fun onRestoreState(state: String?) = Unit
    fun instantiateCoroutineScope() = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * returns true when handled action
     */
    fun onBackPressed(): Boolean = false
}
