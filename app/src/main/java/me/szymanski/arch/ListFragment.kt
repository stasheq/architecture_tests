package me.szymanski.arch

import android.content.Context
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import me.szymanski.arch.logic.cases.ListCase
import me.szymanski.arch.screens.RepositoriesList
import me.szymanski.arch.widgets.ListItem
import me.szymanski.arch.logic.cases.ListCase.LoadingState.LOADING
import me.szymanski.arch.logic.cases.ListCase.LoadingState.EMPTY
import me.szymanski.arch.logic.cases.ListCase.LoadingState.ERROR
import me.szymanski.arch.logic.cases.ListCase.LoadingState.SUCCESS
import me.szymanski.glue.GlueFragment

@AndroidEntryPoint
class ListFragment : GlueFragment<ListCase, RepositoriesList>() {

    override fun createView(ctx: Context, parent: ViewGroup?) = RepositoriesList(ctx, parent)

    override fun linkViewAndLogic(view: RepositoriesList, case: ListCase) {
        case.loading.onNext { loadingState ->
            view.refreshing = loadingState == LOADING
            view.listVisible = loadingState == LOADING || loadingState == SUCCESS
            view.emptyText = if (loadingState == EMPTY) getString(R.string.empty_list) else null
            view.errorText = if (loadingState == ERROR) getString(R.string.error) else null
        }
        case.list
            .map { list -> list.map { ListItem(it.name, it.name, it.description) } }
            .onNext { result -> view.items = result }
        view.userName = case.userName
        view.refreshAction.onNext { case.reload() }
        view.selectAction.onNext { case.selectItem(it) }
        view.userNameChanges.onNext { case.userName = it.toString() }
    }
}
