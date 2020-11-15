package me.szymanski.arch.logic.test

import io.reactivex.rxjava3.observers.TestObserver
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.Assertions

fun <T> TestObserver<T>.assertLast(value: T) = Assertions.assertEquals(value, values().last())

fun <T> TestObserver<T>.containsValue(value: T) = Assertions.assertTrue(values().contains(value))

fun MockWebServer.dispatch(vararg pairs: Pair<String, Any>) {
    val map = hashMapOf(*pairs)
    dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val path = request.path?.substringBeforeLast("?")
            println("Request path: $path")
            val response = map[path]
            return MockResponse().apply {
                when (response) {
                    is String -> setBody(response)
                    is Int -> setResponseCode(response)
                    else -> setResponseCode(404)
                }
            }
        }
    }
}
