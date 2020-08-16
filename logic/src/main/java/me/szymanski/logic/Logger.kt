package me.szymanski.logic

interface Logger {
    fun log(message: String, tag: String? = null, level: Level = Level.DEBUG)

    enum class Level {
        VERBOSE, DEBUG, INFO, WARN, ERROR
    }
}
