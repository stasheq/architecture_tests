package me.szymanski.arch

import android.content.Context
import android.view.ViewGroup
import me.szymanski.arch.logic.cases.RepositoriesListCase
import me.szymanski.arch.screens.RepositoriesList
import me.szymanski.arch.widgets.ListItem
import me.szymanski.arch.logic.cases.RepositoriesListCase.LoadingState.LOADING
import me.szymanski.arch.logic.cases.RepositoriesListCase.LoadingState.EMPTY
import me.szymanski.arch.logic.cases.RepositoriesListCase.LoadingState.ERROR
import me.szymanski.arch.logic.cases.RepositoriesListCase.LoadingState.SUCCESS
import me.szymanski.glue.GlueFragment

class ListFragment : GlueFragment<RepositoriesListCase, RepositoriesList>() {

    override fun createView(ctx: Context, parent: ViewGroup?) = RepositoriesList(ctx, parent)
    override fun caseFactory() = component.reposListVMFactory()

    override fun linkViewAndLogic(view: RepositoriesList, case: RepositoriesListCase) {
        case.loading.onNext { loadingState ->
            view.refreshing = loadingState == LOADING
            view.listVisible = loadingState == LOADING || loadingState == SUCCESS
            view.emptyText = if (loadingState == EMPTY) getString(R.string.empty_list) else null
            view.errorText = if (loadingState == ERROR) getString(R.string.error) else null
        }
        case.list
            .map { list -> list.map { ListItem(it.name, it.name, it.description) } }
            .onNext { result -> view.items = result }
        view.refreshAction.onNext { case.reload() }
        view.selectAction.onNext { case.selectItem(it) }
    }
}
