package me.szymanski.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.szymanski.arch.di.LogicViewModel
import me.szymanski.arch.logic.screenslogic.DetailsLogic
import me.szymanski.arch.logic.screenslogic.ListLogic
import me.szymanski.arch.logic.screenslogic.ListLogicImpl
import me.szymanski.arch.screens.ListScreen
import me.szymanski.arch.widgets.list.ListItemData

@HiltViewModel
class ListViewModel @Inject constructor(logic: ListLogicImpl) : LogicViewModel<ListLogic>(logic)

@AndroidEntryPoint
class ListFragment : Fragment() {
    private val listModel: ListViewModel by activityViewModels()
    private val detailsModel: DetailsViewModel by activityViewModels()
    private lateinit var view: ListScreen

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        ListScreen(inflater.context, container).also { view = it }.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                subscribeToLogic(view, listModel.logic, detailsModel.logic)
            }
        }
    }

    private suspend fun subscribeToLogic(view: ListScreen, logic: ListLogic, detailsLogic: DetailsLogic) = coroutineScope {
        view.userName = logic.userName
        launch { logic.loading.collect { view.refreshing = it } }
        launch {
            logic.error.collect {
                view.errorText = when (it) {
                    ListLogic.ErrorType.USER_DOESNT_EXIST -> getString(R.string.loading_error_doesnt_exist)
                    ListLogic.ErrorType.NO_CONNECTION, ListLogic.ErrorType.OTHER -> getString(R.string.loading_error_other)
                    null -> null
                }
            }
        }
        launch {
            logic.list
                .map { list -> list.map { ListItemData(it.name, it.name, it.description) } }
                .collect { result ->
                    view.lastItemMessage = if (result.isEmpty()) getString(R.string.empty_list) else null
                    view.items = result
                }
        }
        launch { logic.hasNextPage.collect { view.hasNextPage = it } }
        launch { view.refreshAction.collect { logic.reload() } }
        launch { view.userNameChanges.collect { logic.userName = it } }
        launch { view.selectAction.collect { logic.itemClick(detailsLogic, it) } }
        launch { view.loadNextPageAction.collect { logic.loadNextPage() } }
    }
}
