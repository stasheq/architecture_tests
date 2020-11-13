package me.szymanski.arch.logic.test.di

import me.szymanski.arch.logic.cases.ListLogic
import javax.inject.Inject

class LogicContainer {
    @Inject
    lateinit var listLogic: ListLogic
}
