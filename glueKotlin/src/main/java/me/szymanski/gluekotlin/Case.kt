package me.szymanski.gluekotlin

interface Case {
    var parent: Case?
    fun create()
    fun destroy()
}
