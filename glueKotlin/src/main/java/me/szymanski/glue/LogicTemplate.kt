package me.szymanski.glue

import com.jakewharton.rxrelay3.PublishRelay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

open class LogicTemplate : Logic {
    override fun create() = Unit
    override fun destroy() = Unit
    override fun onSaveState(): String? = null
    override fun onRestoreState(state: String?) = Unit

    val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val backPressed: PublishRelay<Unit> = PublishRelay.create()
    var enableBackPress = false
    private val children = ArrayList<Logic>()
    override var parent: Logic? = null
        set(value) {
            field?.removeChild(this)
            field = value
            field?.addChild(this)
        }

    override fun addChild(child: Logic) {
        children.add(child)
    }

    override fun removeChild(child: Logic) {
        children.remove(child)
    }

    override fun onBackPressed(): Boolean {
        for (child in children) {
            if (child.onBackPressed()) return true
        }
        if (enableBackPress) {
            backPressed.accept(Unit)
            return true
        }
        return false
    }
}
