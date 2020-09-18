package me.szymanski.arch

import android.content.Context
import android.view.ViewGroup
import me.szymanski.arch.glue.GlueFragment
import me.szymanski.arch.logic.cases.DetailsCase
import me.szymanski.arch.screens.RepositoryDetails
import me.szymanski.arch.logic.cases.DetailsCase.LoadingState.LOADING
import me.szymanski.arch.logic.cases.DetailsCase.LoadingState.ERROR
import me.szymanski.arch.logic.cases.DetailsCase.LoadingState.SUCCESS

class DetailsFragment : GlueFragment<DetailsCase, RepositoryDetails>() {

    override fun caseFactory() = component.detailsVMFactory()
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
