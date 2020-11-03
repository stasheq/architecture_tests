package me.szymanski.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.lifecycle.ViewModelInject
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.szymanski.arch.di.LogicViewModel
import me.szymanski.arch.logic.cases.DetailsLogic
import me.szymanski.arch.logic.cases.ListLogic
import me.szymanski.arch.logic.cases.ListLogic.LoadingState.*
import me.szymanski.arch.logic.cases.ListLogicImpl
import me.szymanski.arch.screens.RepositoriesList
import me.szymanski.arch.utils.observeOnUi
import me.szymanski.arch.widgets.ListItem

class ListViewModel @ViewModelInject constructor(logic: ListLogicImpl) : LogicViewModel<ListLogic>(logic)

@AndroidEntryPoint
class ListFragment : Fragment() {
    private val listModel: ListViewModel by activityViewModels()
    private val detailsModel: DetailsViewModel by activityViewModels()
    private lateinit var view: RepositoriesList
    private var disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = RepositoriesList(inflater.context, container)
        return view.root
    }

    override fun onStart() {
        super.onStart()
        linkViewAndLogic(view, listModel.logic, detailsModel.logic)
    }

    override fun onStop() {
        disposables.dispose()
        disposables = CompositeDisposable()
        super.onStop()
    }

    private fun linkViewAndLogic(view: RepositoriesList, logic: ListLogic, detailsLogic: DetailsLogic) {
        logic.state.observeOnUi(disposables) { state ->
            view.refreshing = state == LOADING
            view.listVisible = state == LOADING || state == SUCCESS
            view.emptyText = if (state == EMPTY) getString(R.string.empty_list) else null
            view.errorText = if (state == ERROR) getString(R.string.error) else null
        }
        logic.list
            .map { list -> list.map { ListItem(it.name, it.name, it.description) } }
            .observeOnUi(disposables) { result -> view.items = result }
        view.userName = logic.userName
        view.refreshAction.observeOnUi(disposables) { logic.reload() }
        view.userNameChanges.observeOnUi(disposables) { logic.userName = it.toString() }
        view.selectAction.observeOnUi(disposables) {
            detailsLogic.repositoryName = it
            detailsLogic.userName = logic.userName
        }
    }
}
