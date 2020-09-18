package me.szymanski.arch.logic

interface Case {
    var parent: Case?
    fun create()
    fun destroy()
}
