package me.szymanski.listtest.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.szymanski.listtest.*
import me.szymanski.logic.cases.RepositoriesListCase
import me.szymanski.logic.cases.RepositoriesListCase.LoadingState.*
import me.szymanski.screens.RepositoriesList
import me.szymanski.widgets.ListItem

class ListFragment : BaseFragment<RepositoriesListCase>() {
    private lateinit var reposListScreen: RepositoriesList

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        reposListScreen = RepositoriesList(inflater.context, container)
        return reposListScreen.root
    }

    override fun linkCase(case: RepositoriesListCase) {
        case.loading.onNext { loadingState ->
            reposListScreen.refreshing = loadingState == LOADING
            reposListScreen.listVisible = loadingState == LOADING || loadingState == SUCCESS
            reposListScreen.emptyTextVisible = loadingState == EMPTY
            reposListScreen.errorTextVisible = loadingState == ERROR
        }
        case.list.onNext { result -> reposListScreen.items = result.map { ListItem(it.name, it.description) } }
        reposListScreen.refreshAction.onNext { case.reload() }
    }

    override fun caseFactory() = component.reposListViewModelFactory()
}
