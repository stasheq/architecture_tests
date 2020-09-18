package me.szymanski.arch.ui.list

import android.content.Context
import android.view.ViewGroup
import me.szymanski.arch.component
import me.szymanski.arch.glue.GlueFragment
import me.szymanski.arch.logic.cases.RepositoriesListCase
import me.szymanski.arch.logic.cases.RepositoriesListCase.LoadingState.*
import me.szymanski.arch.screens.RepositoriesList
import me.szymanski.arch.widgets.ListItem

class ListFragment : GlueFragment<RepositoriesListCase, RepositoriesList>() {

    override fun createView(ctx: Context, parent: ViewGroup?) = RepositoriesList(ctx, parent)

    override fun linkViewAndLogic(view: RepositoriesList, case: RepositoriesListCase) {
        case.loading.onNext { loadingState ->
            view.refreshing = loadingState == LOADING
            view.listVisible = loadingState == LOADING || loadingState == SUCCESS
            view.emptyTextVisible = loadingState == EMPTY
            view.errorTextVisible = loadingState == ERROR
        }
        case.list
            .map { list -> list.map { ListItem(it.name, it.description) } }
            .onNext { result -> view.items = result }
        view.refreshAction.onNext { case.reload() }
    }

    override fun caseFactory() = component.reposListVMFactory()
}
