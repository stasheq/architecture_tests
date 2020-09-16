package me.szymanski.listtest.ui.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.view.*
import me.szymanski.listtest.*
import me.szymanski.logic.cases.RepositoriesListCase
import me.szymanski.logic.cases.RepositoriesListCase.LoadingState.*
import me.szymanski.widgets.ListItem
import me.szymanski.widgets.ListWidget

class ListFragment : BaseFragment<RepositoriesListCase>() {
    private lateinit var listWidget: ListWidget

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        listWidget = ListWidget(requireContext(), view.reposMainFrame)
        view.reposMainFrame.addView(listWidget.root)
        return view
    }

    override fun linkCase(case: RepositoriesListCase) {
        case.loading.onNext { loadingState ->
            listWidget.refreshing = loadingState == LOADING
            listWidget.root.isVisible = loadingState == LOADING || loadingState == SUCCESS
            reposEmptyText.isVisible = loadingState == EMPTY
            reposErrorText.isVisible = loadingState == ERROR
        }
        case.list.onNext { result -> listWidget.items = result.map { ListItem(it.name, it.description) } }

        listWidget.refreshAction.onNext { case.reload() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun caseFactory() = component.reposListViewModelFactory()
}
