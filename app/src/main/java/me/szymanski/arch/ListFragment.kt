package me.szymanski.arch

import android.content.Context
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
import me.szymanski.arch.utils.AndroidScreen
import me.szymanski.arch.widgets.list.ListItemData
import java.util.concurrent.TimeUnit

class ListViewModel @ViewModelInject constructor(logic: ListLogicImpl) : LogicViewModel<ListLogic>(logic)

@AndroidEntryPoint
class ListFragment : Fragment(), AndroidScreen {
    private val listModel: ListViewModel by activityViewModels()
    private val detailsModel: DetailsViewModel by activityViewModels()
    private lateinit var view: RepositoriesList
    override val ctx: Context by lazy { requireContext() }
    override var disposables = CompositeDisposable()

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
        logic.loading.observeChangedOnUi { view.refreshing = it }
        logic.error.observeChangedOnUi {
            view.errorText = when (it.value) {
                ListLogic.ErrorType.DOESNT_EXIST -> getString(R.string.loading_error_doesnt_exist)
                ListLogic.ErrorType.OTHER -> getString(R.string.loading_error_other)
                null -> null
            }
        }
        logic.list
            .map { list -> list.map { ListItemData(it.name, it.name, it.description) } }
            .observeOnUi { result ->
                view.lastItemMessage = if (result.isEmpty()) getString(R.string.empty_list) else null
                view.items = result
            }
        logic.hasNextPage.observeChangedOnUi { view.hasNextPage = it }
        view.userName = logic.userName
        view.refreshAction.observeOnUi { logic.reload() }
        view.userNameChanges.observeChangedOnUi { logic.userName = it }
        view.selectAction.observeOnUi { logic.itemClick(detailsLogic, it) }
        view.loadNextPageAction.debounce(500, TimeUnit.MILLISECONDS).observeOnUi { logic.loadNextPage() }
    }
}
