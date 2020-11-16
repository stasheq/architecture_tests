package me.szymanski.arch.logic.test.utils

import io.reactivex.rxjava3.observers.TestObserver
import okhttp3.mockwebserver.*

fun <T> TestObserver<T>.last(): T = values().last()

fun MockWebServer.dispatch(vararg pairs: Pair<String, Any>) {
    val map = hashMapOf(*pairs)
    dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val path = request.path?.substringBeforeLast("?")
            println("Request path: $path")
            val response = map[path]
            return MockResponse().apply {
                socketPolicy = SocketPolicy.DISCONNECT_AT_END
                when (response) {
                    is String -> setBody(response)
                    is Int -> setResponseCode(response)
                    else -> setResponseCode(404)
                }
            }
        }
    }
}

fun MockWebServer.noConnection() {
    dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val path = request.path?.substringBeforeLast("?")
            println("Request path: $path")
            return MockResponse().apply {
                socketPolicy = SocketPolicy.NO_RESPONSE
            }
        }
    }
}
