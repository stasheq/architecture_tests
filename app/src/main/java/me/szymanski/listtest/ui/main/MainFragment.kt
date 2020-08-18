package me.szymanski.listtest.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.main_fragment.*
import me.szymanski.listtest.ApplicationComponent
import me.szymanski.listtest.BaseFragment
import me.szymanski.listtest.R
import me.szymanski.logic.cases.RepositoriesListCase
import me.szymanski.logic.cases.RepositoriesListCase.LoadingState.*

class MainFragment : BaseFragment<RepositoriesListCase>() {
    private val adapter = ReposAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        reposRecycler.adapter = this.adapter
        reposRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    override fun inject(component: ApplicationComponent) = component.inject(this)

    override fun linkCase(case: RepositoriesListCase) {
        case.loading.onNext { loadingState ->
            reposSwipeRefresh.isRefreshing = loadingState == LOADING
            reposRecycler.isVisible = loadingState == LOADING || loadingState == SUCCESS
            reposEmptyText.isVisible = loadingState == EMPTY
            reposErrorText.isVisible = loadingState == ERROR
        }
        case.list.onNext { result -> adapter.setItems(result) }
        reposSwipeRefresh.setOnRefreshListener { case.reload() }
    }
}
