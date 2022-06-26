package me.szymanski.arch.domain.test

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.szymanski.arch.domain.list.data.ErrorType
import me.szymanski.arch.domain.navigation.NavigationScreen
import me.szymanski.arch.domain.test.di.DaggerTestComponent
import me.szymanski.arch.domain.test.utils.FileReader
import me.szymanski.arch.domain.test.utils.dispatch
import me.szymanski.arch.domain.test.utils.noConnection
import me.szymanski.arch.domain.test.utils.test
import me.szymanski.arch.rest.RestConfig
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
            callTimeout = 500,
            logEnabled = true
        )
        val reposPath = "/users/$user/repos"

        val component = DaggerTestComponent.builder().restConfig(restConfig).build()
        val listLogic = component.getListLogic()
        val detailsLogic = component.getDetailsLogic()
        val navigationCoordinator = component.getNavigationCoordinator()

        navigationCoordinator.wideScreen = false
        listLogic shouldNotBe null
        detailsLogic shouldNotBe null
        navigationCoordinator shouldNotBe null

        val loading = listLogic.loading.test()
        val list = listLogic.list.test()
        val error = listLogic.error.test()
        val hasNextPage = listLogic.hasNextPage.test()
        val close = navigationCoordinator.closeApp.test()
        val currentScreen = navigationCoordinator.currentScreen.test()

        "loading not started"  { loading.last shouldBe true }
        "has no items yet" { list.last shouldBe null }
        "no error" { error.last shouldBe null }
        "app alive" { close.assertNoValues() }
        "list shown" { currentScreen.last?.javaClass shouldBe NavigationScreen.List::class.java }
        "next page not known" { hasNextPage.last shouldBe false }

        "On started with unsupported response" - {
            server.dispatch(reposPath to "Unsupported Format Response")
            listLogic.reload()
            error.awaitValue(ErrorType.OTHER)
            "finished loading" { true shouldBeIn loading.values; loading.last shouldBe false }
            "received error" { error.last shouldBe ErrorType.OTHER }
        }

        "On started with not found response" - {
            server.dispatch(reposPath to 404)
            listLogic.reload()
            error.awaitValue(ErrorType.USER_DOESNT_EXIST)
            "finished loading" { true shouldBeIn loading.values; loading.last shouldBe false }
            "received error" { error.last shouldBe ErrorType.USER_DOESNT_EXIST }
        }

        "On started with no connection" - {
            server.noConnection()
            listLogic.reload()
            error.awaitValue(ErrorType.NO_CONNECTION)
            "finished loading" { true shouldBeIn loading.values; loading.last shouldBe false }
            "received error" { error.last shouldBe ErrorType.NO_CONNECTION }
        }

        "On started and download 1st page" - {
            server.dispatch(reposPath to FileReader.readText(firstPageFile))
            listLogic.reload()
            list.awaitCondition { !it.last().isNullOrEmpty() }
            "finished loading" { true shouldBeIn loading.values; loading.last shouldBe false }
            "no error" { error.last shouldBe null }
            "received list of 2 items" { list.last?.size shouldBe 2 }
            "has 2nd page" { hasNextPage.last shouldBe true }

            "On download 2nd page" - {
                server.dispatch(reposPath to FileReader.readText(secondPageFile))
                listLogic.loadNextPage()
                list.awaitCondition {
                    val size = it.last()?.size
                    size != null && size > 2
                }
                "finished loading" { loading.last shouldBe false }
                "no error" { error.last shouldBe null }
                "received list of 3 items" { list.last?.size shouldBe 3 }
                "no 3rd page" { hasNextPage.last shouldBe false }
            }

            "On error downloading 2nd page" - {
                server.noConnection()
                listLogic.loadNextPage()
                error.awaitValue(ErrorType.NO_CONNECTION)
                "finished loading" { loading.last shouldBe false }
                "received error" { error.last shouldBe ErrorType.NO_CONNECTION }
                "received list of prevoius 2 items" { list.last?.size shouldBe 2 }
                "no next page" { hasNextPage.last shouldBe false }
            }

            "On Click first item" - {
                val item = list.last?.get(0)
                "it has no empty name" { item?.name.isNullOrBlank() shouldBe false }
                listLogic.itemClick(item!!)
                "details are shown" {
                    val screen = currentScreen.last as? NavigationScreen.Details
                    screen?.repositoryId?.repositoryName shouldBe item.name
                }
            }

            "On big screen" - {
                navigationCoordinator.wideScreen = true
                "both columns are shown" { currentScreen.last?.javaClass shouldBe NavigationScreen.ListAndDetails::class.java }
                "On Click item" - {
                    val item = list.last?.get(0)
                    "it has no empty name" { item?.name.isNullOrBlank() shouldBe false }
                    listLogic.itemClick(item!!)
                    "details are shown" {
                        val screen = currentScreen.last as? NavigationScreen.ListAndDetails
                        screen?.repositoryId?.repositoryName shouldBe item.name
                    }
                }
            }
        }
    }
})
