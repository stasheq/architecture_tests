package me.szymanski.arch.logic.test

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.szymanski.arch.logic.Optional
import me.szymanski.arch.logic.cases.ListLogic
import me.szymanski.arch.logic.rest.RestConfig
import me.szymanski.arch.logic.test.di.DaggerTestComponent
import me.szymanski.arch.logic.test.utils.*
import okhttp3.mockwebserver.MockWebServer

class ListTest : FreeSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    val firstPageFile = "1st_page_response.json"
    val secondPageFile = "2nd_page_response.json"

    "On init" - {
        val server = MockWebServer()
        server.start()
        val user = "user"
        val restConfig = RestConfig(
            baseUrl = server.url("").toString(),
            defaultUser = user,
            pageLimit = 2,
            callTimeout = 500
        )
        val reposPath = "/users/$user/repos"

        val component = DaggerTestComponent.builder().restConfig(restConfig).build()
        val logic = component.getListLogic()
        val detailsLogic = component.getDetailsLogic()
        logic shouldNotBe null
        detailsLogic shouldNotBe null

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
        "list shown" { showList.last() shouldBe true }
        "details not shown" { showDetails.last() shouldBe false }
        "next page not known" { hasNextPage.assertNoValues() }

        "On started with unsupported response" - {
            server.dispatch(reposPath to "Unsupported Format Response")
            logic.create()
            error.awaitCount(1)
            "finished loading" { true shouldBeIn loading.values(); loading.last() shouldBe false }
            "received error" { error.last() shouldBe Optional(ListLogic.ErrorType.OTHER) }
        }

        "On started with not found response" - {
            server.dispatch(reposPath to 404)
            logic.create()
            error.awaitCount(1)
            "finished loading" { true shouldBeIn loading.values(); loading.last() shouldBe false }
            "received error" { error.last() shouldBe Optional(ListLogic.ErrorType.USER_DOESNT_EXIST) }
        }

        "On started with no connection" - {
            server.noConnection()
            logic.create()
            error.awaitCount(1)
            "finished loading" { true shouldBeIn loading.values(); loading.last() shouldBe false }
            "received error" { error.last() shouldBe Optional(ListLogic.ErrorType.NO_CONNECTION) }
        }

        "On started and download 1st page" - {
            server.dispatch(reposPath to FileReader.readText(firstPageFile))
            logic.create()
            list.awaitCount(1)
            "finished loading" { true shouldBeIn loading.values(); loading.last() shouldBe false }
            "no error" { error.last() shouldBe Optional<ListLogic.ErrorType>(null) }
            "received list of 2 items" { list.last().size shouldBe 2 }
            "has 2nd page" { hasNextPage.last() shouldBe true }

            "On download 2nd page" - {
                server.dispatch(reposPath to FileReader.readText(secondPageFile))
                logic.loadNextPage()
                list.awaitCount(2)
                "finished loading" { loading.last() shouldBe false }
                "no error" { error.last() shouldBe Optional<ListLogic.ErrorType>(null) }
                "received list of 3 items" { list.last().size shouldBe 3 }
                "no 3rd page" { hasNextPage.last() shouldBe false }
            }

            "On error downloading 2nd page" - {
                server.noConnection()
                logic.loadNextPage()
                list.awaitCount(2)
                "finished loading" { loading.last() shouldBe false }
                "received error" { error.last() shouldBe Optional(ListLogic.ErrorType.NO_CONNECTION) }
                "received list of prevoius 2 items" { list.last().size shouldBe 2 }
                "no next page" { hasNextPage.last() shouldBe false }
            }

            "On Click item" - {
                logic.itemClick(detailsLogic, list.last()[0].name)
                "details are shown" { showDetails.last() shouldBe true }
                "list is hidden" { showList.last() shouldBe false }
            }

            "On big screen" - {
                logic.wideScreen = true
                "details are shown" { showDetails.last() shouldBe true }
                "list is shown" { showList.last() shouldBe true }
                "On Click item" - {
                    logic.itemClick(detailsLogic, list.last()[0].name)
                    "details are shown" { showDetails.last() shouldBe true }
                    "list is shown" { showList.last() shouldBe true }
                }
            }
        }
    }
})
