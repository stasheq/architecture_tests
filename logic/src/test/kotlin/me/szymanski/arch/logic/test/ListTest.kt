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
        val restConfig = RestConfig(
            baseUrl = server.url("").toString(),
            defaultUser = "user",
            pageLimit = 5,
            callTimeout = 500
        )

        val logic = DaggerTestComponent.builder()
            .restConfig(restConfig)
            .build().getListLogic()
        logic shouldNotBe null

        val loading = logic.loading.test()
        val list = logic.list.test()
        val error = logic.error.test()
        val close = logic.closeApp.test()
        val showList = logic.showList.test()
        val showDetails = logic.showDetails.test()
        val hasNextPage = logic.hasNextPage.test()

        "loading not started"  { loading.assertNoValues() }
        "has no items yet" { list.assertNoValues() }
        "no error" { error.assertNoValues() }
        "app alive" { close.assertNoValues() }
        "list shown" { showList.assertLast(true) }
        "details not shown" { showDetails.assertLast(false) }
        "next page not known" { hasNextPage.assertNoValues() }

        "On started with unsupported response" - {
            server.dispatch("/users/user/repos" to "Unsupported Format Response")
            logic.create()
            error.awaitCount(1)
            "finished loading" { loading.containsValue(true); loading.assertLast(false) }
            "received error" { error.assertLast(Optional(ListLogic.ErrorType.OTHER)) }
        }

        "On started with not found response" - {
            server.dispatch("/users/user/repos" to 404)
            logic.create()
            error.awaitCount(1)
            "finished loading" { loading.containsValue(true); loading.assertLast(false) }
            "received error" { error.assertLast(Optional(ListLogic.ErrorType.USER_DOESNT_EXIST)) }
        }

        "On started with no connection" - {
            server.shutdown()
            logic.create()
            error.awaitCount(1)
            "finished loading" { loading.containsValue(true); loading.assertLast(false) }
            "received error" {
                val oo = error.values()
                error.assertLast(Optional(ListLogic.ErrorType.NO_CONNECTION))
            }
        }
    }
})
