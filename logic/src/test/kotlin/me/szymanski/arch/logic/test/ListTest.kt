package me.szymanski.arch.logic.test

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldNotBe
import me.szymanski.arch.logic.Optional
import me.szymanski.arch.logic.cases.ListLogic
import me.szymanski.arch.logic.rest.RestConfig
import me.szymanski.arch.logic.test.di.DaggerTestComponent
import okhttp3.mockwebserver.MockWebServer

class ListTest : FreeSpec({
    "On init" - {
        val server = MockWebServer()
        server.start()
        val restConfig = RestConfig(server.url("").toString(), "user", 5)

        val logic = DaggerTestComponent.builder()
            .restConfig(restConfig)
            .build().getListLogic()
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
        "details not shown" { showDetails.assertLast(false) }
        "has next page" { hasNextPage.assertLast(false) }
    }
})
