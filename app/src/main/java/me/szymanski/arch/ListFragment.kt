package me.szymanski.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.szymanski.arch.screens.RepositoriesList
import me.szymanski.arch.widgets.ListItem
import me.szymanski.arch.logic.cases.ListLogic.LoadingState.LOADING
import me.szymanski.arch.logic.cases.ListLogic.LoadingState.EMPTY
import me.szymanski.arch.logic.cases.ListLogic.LoadingState.ERROR
import me.szymanski.arch.logic.cases.ListLogic.LoadingState.SUCCESS
import me.szymanski.arch.logic.cases.ListLogic
import me.szymanski.arch.utils.observeOnUi
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment() {

    @Inject
    lateinit var logic: ListLogic
    private lateinit var view: RepositoriesList
    private var disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = RepositoriesList(inflater.context, container)
        return view.root
    }

    override fun onStart() {
        super.onStart()
        linkViewAndLogic(view, logic)
    }

    override fun onStop() {
        disposables.dispose()
        disposables = CompositeDisposable()
        super.onStop()
    }

    private fun linkViewAndLogic(view: RepositoriesList, case: ListLogic) {
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
