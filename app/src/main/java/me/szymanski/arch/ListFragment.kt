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
import me.szymanski.arch.logic.cases.ListLogicImpl
import me.szymanski.arch.screens.RepositoriesList
import me.szymanski.arch.utils.observeChangesOnUi
import me.szymanski.arch.utils.observeOnUi
import me.szymanski.arch.widgets.ListItemData

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
        logic.loading.observeChangesOnUi(disposables) { view.refreshing = it }
        logic.error.observeChangesOnUi(disposables) {
            view.errorText = when (it.value) {
                ListLogic.ErrorType.DOESNT_EXIST -> getString(R.string.loading_error_doesnt_exist)
                ListLogic.ErrorType.OTHER -> getString(R.string.loading_error_other)
                null -> null
            }
        }
        logic.list
            .map { list -> list.map { ListItemData(it.name, it.name, it.description) } }
            .observeOnUi(disposables) { result ->
                view.emptyText = if (result.isEmpty()) getString(R.string.empty_list) else null
                view.items = result
            }
        view.userName = logic.userName
        view.refreshAction.observeOnUi(disposables) { logic.reload() }
        view.userNameChanges.observeChangesOnUi(disposables) { logic.userName = it }
        view.selectAction.observeOnUi(disposables) { logic.itemClick(detailsLogic, it) }
    }
}
