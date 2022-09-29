package me.szymanski.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.szymanski.arch.domain.data.Repository
import me.szymanski.arch.domain.list.ListLogic
import me.szymanski.arch.domain.list.data.ErrorType
import me.szymanski.arch.screens.ListScreen
import me.szymanski.arch.widgets.list.ListItemType.ListItem

@AndroidEntryPoint
class ListFragment : Fragment() {
    @Inject
    lateinit var logic: ListLogic
    private var viewUpdateJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        ListScreen(inflater.context, container).apply {
            viewUpdateJob = lifecycleScope.launch { subscribeToLogic(this@apply, logic) }
        }.root

    override fun onDestroyView() {
        viewUpdateJob?.cancel()
        viewUpdateJob = null
        super.onDestroyView()
    }

    private fun CoroutineScope.subscribeToLogic(view: ListScreen, logic: ListLogic) {
        view.userName = logic.userName
        launch { logic.loading.collect { view.refreshing = it } }
        launch {
            logic.error.collect {
                view.errorText = when (it) {
                    ErrorType.USER_DOESNT_EXIST -> getString(R.string.loading_error_doesnt_exist)
                    ErrorType.NO_CONNECTION, ErrorType.OTHER -> getString(
                        R.string.loading_error_other
                    )
                    null -> null
                }
            }
        }
        launch {
            logic.list.map { it?.mapToUI() }.collect { result ->
                if (result == null) return@collect
                view.lastItemMessage = if (result.isEmpty()) getString(R.string.empty_list) else null
                view.items = result
            }
        }
        launch { logic.hasNextPage.collect { view.hasNextPage = it } }
        launch { view.refreshAction.collect { logic.reload() } }
        launch { view.userNameChanges.collect { logic.userName = it } }
        launch { view.loadNextPageAction.collect { logic.loadNextPage() } }
    }

    private fun List<Repository>.mapToUI() =
        map { ListItem(it.name, it.name, it.description).apply { onClick = { logic.itemClick(it) } } }

    companion object {
        fun instantiate() = ListFragment()
    }
}
