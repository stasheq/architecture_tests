package me.szymanski.arch

interface Logger {
    fun log(message: String, t: Throwable? = null, tag: String? = null, level: Level = Level.DEBUG)

    enum class Level {
        VERBOSE, DEBUG, INFO, WARN, ERROR
    }
}
