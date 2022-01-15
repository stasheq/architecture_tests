package me.szymanski.arch.logic.test

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.szymanski.arch.logic.list.ListLogic
import me.szymanski.arch.rest.RestConfig
import me.szymanski.arch.logic.navigation.NavigationLogic
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
        val listLogic = component.getListLogic()
        val detailsLogic = component.getDetailsLogic()
        val navigationLogic = component.getNavigationLogic()

        navigationLogic.wideScreen = false
        listLogic shouldNotBe null
        detailsLogic shouldNotBe null
        navigationLogic shouldNotBe null

        val loading = listLogic.loading.test()
        val list = listLogic.list.test()
        val error = listLogic.error.test()
        val hasNextPage = listLogic.hasNextPage.test()
        val close = navigationLogic.closeApp.test()
        val columnsState = navigationLogic.currentScreen.test()

        "loading not started"  { loading.last shouldBe true }
        "has no items yet" { list.last?.isEmpty() shouldBe true }
        "no error" { error.last shouldBe null }
        "app alive" { close.assertNoValues() }
        "list shown" { columnsState.last shouldBe NavigationLogic.Screen.LIST }
        "next page not known" { hasNextPage.last shouldBe false }

        "On started with unsupported response" - {
            server.dispatch(reposPath to "Unsupported Format Response")
            listLogic.reload()
            error.awaitValue(ListLogic.ErrorType.OTHER)
            "finished loading" { true shouldBeIn loading.values; loading.last shouldBe false }
            "received error" { error.last shouldBe ListLogic.ErrorType.OTHER }
        }

        "On started with not found response" - {
            server.dispatch(reposPath to 404)
            listLogic.reload()
            error.awaitValue(ListLogic.ErrorType.USER_DOESNT_EXIST)
            "finished loading" { true shouldBeIn loading.values; loading.last shouldBe false }
            "received error" { error.last shouldBe ListLogic.ErrorType.USER_DOESNT_EXIST }
        }

        "On started with no connection" - {
            server.noConnection()
            listLogic.reload()
            error.awaitValue(ListLogic.ErrorType.NO_CONNECTION)
            "finished loading" { true shouldBeIn loading.values; loading.last shouldBe false }
            "received error" { error.last shouldBe ListLogic.ErrorType.NO_CONNECTION }
        }

        "On started and download 1st page" - {
            server.dispatch(reposPath to FileReader.readText(firstPageFile))
            listLogic.reload()
            list.awaitCondition { it.last().isNotEmpty() }
            "finished loading" { true shouldBeIn loading.values; loading.last shouldBe false }
            "no error" { error.last shouldBe null }
            "received list of 2 items" { list.last?.size shouldBe 2 }
            "has 2nd page" { hasNextPage.last shouldBe true }

            "On download 2nd page" - {
                server.dispatch(reposPath to FileReader.readText(secondPageFile))
                listLogic.loadNextPage()
                list.awaitCondition { it.last().size > 2 }
                "finished loading" { loading.last shouldBe false }
                "no error" { error.last shouldBe null }
                "received list of 3 items" { list.last?.size shouldBe 3 }
                "no 3rd page" { hasNextPage.last shouldBe false }
            }

            "On error downloading 2nd page" - {
                server.noConnection()
                listLogic.loadNextPage()
                error.awaitValue(ListLogic.ErrorType.NO_CONNECTION)
                "finished loading" { loading.last shouldBe false }
                "received error" { error.last shouldBe ListLogic.ErrorType.NO_CONNECTION }
                "received list of prevoius 2 items" { list.last?.size shouldBe 2 }
                "no next page" { hasNextPage.last shouldBe false }
            }

            "On Click first item" - {
                val name = list.last?.get(0)?.name
                "it has no empty name" { name.isNullOrBlank() shouldBe false }
                listLogic.itemClick(name!!)
                "details are shown" { columnsState.last shouldBe NavigationLogic.Screen.DETAILS }
            }

            "On big screen" - {
                navigationLogic.wideScreen = true
                "both columns are shown" { columnsState.last shouldBe NavigationLogic.Screen.LIST_AND_DETAILS }
                "On Click item" - {
                    val name = list.last?.get(0)?.name
                    "it has no empty name" { name.isNullOrBlank() shouldBe false }
                    listLogic.itemClick(name!!)
                    "details are shown" { columnsState.last shouldBe NavigationLogic.Screen.LIST_AND_DETAILS }
                }
            }
        }
    }
})
