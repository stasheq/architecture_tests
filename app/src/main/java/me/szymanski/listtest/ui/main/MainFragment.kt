package me.szymanski.listtest.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.szymanski.listtest.*
import me.szymanski.logic.cases.RepositoriesListCase

class MainFragment : BaseFragment<RepositoriesListCase>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun inject(component: ApplicationComponent) = component.inject(this)

    override fun linkCase(case: RepositoriesListCase) {
        case.loading.onNext { result -> log("$result") }

        case.list.onNext { result ->
            log("$result")
        }
    }
}
