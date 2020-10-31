package me.szymanski.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.szymanski.arch.di.CaseReference
import me.szymanski.arch.logic.cases.ListCase
import me.szymanski.arch.screens.RepositoriesList
import me.szymanski.arch.widgets.ListItem
import me.szymanski.arch.logic.cases.ListCase.LoadingState.LOADING
import me.szymanski.arch.logic.cases.ListCase.LoadingState.EMPTY
import me.szymanski.arch.logic.cases.ListCase.LoadingState.ERROR
import me.szymanski.arch.logic.cases.ListCase.LoadingState.SUCCESS
import me.szymanski.arch.utils.observeOnUi
import javax.inject.Inject


@AndroidEntryPoint
class ListFragment : Fragment() {

    @Inject
    lateinit var caseRef: CaseReference<ListCase>
    private lateinit var view: RepositoriesList
    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = RepositoriesList(inflater.context, container)
        linkViewAndLogic(view, caseRef.case)
        return view.root
    }

    private fun linkViewAndLogic(view: RepositoriesList, case: ListCase) {
        case.loading.observeOnUi(disposables) { loadingState ->
            view.refreshing = loadingState == LOADING
            view.listVisible = loadingState == LOADING || loadingState == SUCCESS
            view.emptyText = if (loadingState == EMPTY) getString(R.string.empty_list) else null
            view.errorText = if (loadingState == ERROR) getString(R.string.error) else null
        }
        case.list
            .map { list -> list.map { ListItem(it.name, it.name, it.description) } }
            .observeOnUi(disposables) { result -> view.items = result }
        view.userName = case.userName
        view.refreshAction.observeOnUi(disposables) { case.reload() }
        view.selectAction.observeOnUi(disposables) { case.selectItem(it) }
        view.userNameChanges.observeOnUi(disposables) { case.userName = it.toString() }
    }

    override fun onDestroyView() {
        disposables.dispose()
        super.onDestroyView()
    }
}
