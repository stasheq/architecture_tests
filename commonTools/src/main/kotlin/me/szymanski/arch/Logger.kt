package me.szymanski.arch

interface Logger {
    fun log(message: String, t: Throwable? = null, tag: String? = null, level: Level = Level.DEBUG)

    fun log(t: Throwable, tag: String? = null, level: Level = Level.WARN)

    enum class Level {
        VERBOSE, DEBUG, INFO, WARN, ERROR
    }
}
