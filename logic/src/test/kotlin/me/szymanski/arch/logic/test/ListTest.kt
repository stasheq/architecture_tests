package me.szymanski.arch.logic.test

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldNotBe
import me.szymanski.arch.logic.Optional
import me.szymanski.arch.logic.cases.ListLogic
import me.szymanski.arch.logic.test.di.DaggerTestComponent
import javax.inject.Inject

class LogicContainer {
    @Inject
    lateinit var listLogic: ListLogic
}

class ListTest : FreeSpec({
    "On init" - {
        val logic = LogicContainer().apply { DaggerTestComponent.builder().build().inject(this) }.listLogic
        logic shouldNotBe null
        logic.create()
        val loading = logic.loading.test()
        val list = logic.list.test()
        val error = logic.error.test()
        val close = logic.closeApp.test()
        val showList = logic.showList.test()
        val showDetails = logic.showDetails.test()
        val hasNextPage = logic.hasNextPage.test()

        "is loading"  { loading.assertLast(true) }
        "has no items yet" { list.assertNoValues() }
        "no error" { error.assertLast(Optional<ListLogic.ErrorType>(null)) }
        "app alive" { close.assertNoValues() }
        "list shown" { showList.assertLast(true) }
        "detailsShown" { showDetails.assertLast(false) }
        "has next page" { hasNextPage.assertLast(false) }
    }
})
