package me.szymanski.glue

interface Logic {
    var parent: Logic?
    fun create()
    fun destroy()
    fun onSaveState(): String?
    fun onRestoreState(state: String?)
    fun addChild(child: Logic)
    fun removeChild(child: Logic)

    /**
     * returns true when handled action
     */
    fun onBackPressed(): Boolean
}
