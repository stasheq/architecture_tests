package me.szymanski.glue

interface Case {
    var parent: Case?
    fun create()
    fun destroy()
    fun onSaveState(): String?
    fun onRestoreState(state: String?)
    fun addChild(child: Case)
    fun removeChild(child: Case)

    /**
     * returns true when handled action
     */
    fun onBackPressed(): Boolean
}
