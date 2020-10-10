package me.szymanski.arch

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import me.szymanski.arch.logic.cases.DetailsCase
import me.szymanski.arch.screens.RepositoryDetails
import me.szymanski.arch.logic.cases.DetailsCase.LoadingState.LOADING
import me.szymanski.arch.logic.cases.DetailsCase.LoadingState.ERROR
import me.szymanski.arch.logic.cases.DetailsCase.LoadingState.SUCCESS
import me.szymanski.glue.GlueFragment

class DetailsFragment : GlueFragment<DetailsCase, RepositoryDetails>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun createView(ctx: Context, parent: ViewGroup?) = RepositoryDetails(ctx, parent)

    override fun linkViewAndLogic(view: RepositoryDetails, case: DetailsCase) {
        case.loading.onNext { loadingState ->
            view.loading = loadingState == LOADING
            view.errorText = if (loadingState == ERROR) getString(R.string.error) else null
            view.detailsVisible = loadingState == SUCCESS
        }
        case.result.onNext { view.title = it.name }
        case.reload()
    }
}
