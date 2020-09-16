package me.szymanski.listtest

import me.szymanski.glueandroid.GlueFragment
import me.szymanski.gluekotlin.Case

abstract class BaseFragment<T : Case> : GlueFragment<T>()
