package me.szymanski.listtest

import me.szymanski.glueandroid.GlueFragment
import me.szymanski.gluekotlin.Case
import me.szymanski.logic.Logger
import javax.inject.Inject

abstract class BaseFragment<T : Case> : GlueFragment<T>() {

    @Inject
    lateinit var logger: Logger
    fun log(message: String, tag: String? = null, level: Logger.Level = Logger.Level.DEBUG) =
        logger.log(message, tag, level)
}
