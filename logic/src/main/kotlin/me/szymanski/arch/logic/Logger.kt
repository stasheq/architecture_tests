package me.szymanski.arch.logic

interface Logger {
    fun log(message: String, tag: String? = null, level: Level = Level.DEBUG)
    fun log(t: Throwable, tag: String? = null, level: Level = Level.WARN)

    enum class Level {
        VERBOSE, DEBUG, INFO, WARN, ERROR
    }
}
