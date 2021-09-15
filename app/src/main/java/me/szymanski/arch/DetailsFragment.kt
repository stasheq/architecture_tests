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
import me.szymanski.arch.di.LogicViewModel
import me.szymanski.arch.logic.screenslogic.DetailId
import me.szymanski.arch.screens.DetailsScreen
import me.szymanski.arch.logic.screenslogic.DetailsLogic
import me.szymanski.arch.logic.screenslogic.DetailsLogic.LoadingState.*
import me.szymanski.arch.logic.screenslogic.DetailsLogicImpl
import me.szymanski.arch.utils.isWideScreen
import me.szymanski.arch.widgets.list.ListItemData
import javax.inject.Inject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class DetailsViewModel @Inject constructor(logic: DetailsLogicImpl) : LogicViewModel<DetailsLogic>(logic)

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private val viewModel: DetailsViewModel by activityViewModels()
    private val listViewModel: ListViewModel by activityViewModels()
    private lateinit var view: DetailsScreen

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = DetailsScreen(inflater.context, container)
        return view.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                subscribeToLogic(view, viewModel.logic)
            }
        }
    }

    private suspend fun subscribeToLogic(view: DetailsScreen, logic: DetailsLogic) = coroutineScope {
        view.showBackIcon = !isWideScreen()
        view.backClick = { listViewModel.logic.onBackPressed() }

        launch {
            logic.state.collect { state ->
                view.loading = state == LOADING
                view.errorText = if (state == ERROR) getString(R.string.loading_details_error) else null
                view.listVisible = state == SUCCESS
            }
        }
        launch {
            logic.result.collect { list ->
                view.items = list.map { ListItemData(it.type.name, it.type.toTitle(), it.value) }
            }
        }
        launch { logic.title.collect { view.title = it } }
    }

    private fun DetailId.toTitle(): String = getString(
        when (this) {
            DetailId.NAME -> R.string.detail_name
            DetailId.DESCRIPTION -> R.string.detail_description
            DetailId.PRIVATE -> R.string.detail_private
            DetailId.OWNER -> R.string.detail_owner
            DetailId.FORKS -> R.string.detail_forks
            DetailId.LANGUAGE -> R.string.detail_language
            DetailId.ISSUES -> R.string.detail_issues
            DetailId.LICENSE -> R.string.detail_license
            DetailId.WATCHERS -> R.string.detail_watchers
            DetailId.BRANCH -> R.string.detail_branch
        }
    )
}
