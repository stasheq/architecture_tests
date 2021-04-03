package me.szymanski.arch.logic

interface Logic {
    fun create() = Unit
    fun destroy() = Unit
    fun onSaveState(): String? = null
    fun onRestoreState(state: String?) = Unit

    /**
     * returns true when handled action
     */
    fun onBackPressed(): Boolean = false
}
