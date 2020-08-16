package me.szymanski.listtest.ui.main

import android.content.Context
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.component.inject(this)
    }

    override fun onStart() {
        super.onStart()
        case.loading.onNext { result ->
            log("$result")
        }

        case.list.onNext { result ->
            log("$result")
        }
    }
}
