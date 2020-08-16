package me.szymanski.logic.cases

import com.jakewharton.rxrelay3.PublishRelay
import kotlinx.coroutines.launch
import me.szymanski.logic.Case
import me.szymanski.logic.rest.Repository
import me.szymanski.logic.rest.RestApi
import javax.inject.Inject

class RepositoriesListCase @Inject constructor(private val restApi: RestApi) : Case() {
    val loading = PublishRelay.create<Boolean>()
    val list = PublishRelay.create<List<Repository>>()

    override fun start() {
        loading.accept(true)
        ioScope.launch {
            // TODO error handling
            val items = restApi.getRepositories()
            loading.accept(false)
            list.accept(items)
        }

    }
}