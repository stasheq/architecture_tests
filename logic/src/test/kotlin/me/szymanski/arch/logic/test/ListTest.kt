package me.szymanski.arch.logic.test

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldNotBe
import me.szymanski.arch.logic.test.di.DaggerTestComponent
import me.szymanski.arch.logic.test.di.LogicContainer

class List : FreeSpec({
    "Init" - {
        val logicContainer = LogicContainer()
        DaggerTestComponent.builder().build().inject(logicContainer)
        val logic = logicContainer.listLogic
        logic shouldNotBe null

        "On start" - {

        }
    }
})
