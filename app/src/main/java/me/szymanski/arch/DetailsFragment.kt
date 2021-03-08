package me.szymanski.arch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.szymanski.arch.di.LogicViewModel
import me.szymanski.arch.logic.cases.DetailId
import me.szymanski.arch.screens.DetailsScreen
import me.szymanski.arch.logic.cases.DetailsLogic
import me.szymanski.arch.logic.cases.DetailsLogic.LoadingState.*
import me.szymanski.arch.logic.cases.DetailsLogicImpl
import me.szymanski.arch.utils.AndroidScreen
import me.szymanski.arch.utils.isWideScreen
import me.szymanski.arch.widgets.list.ListItemData
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(logic: DetailsLogicImpl) : LogicViewModel<DetailsLogic>(logic)

@AndroidEntryPoint
class DetailsFragment : Fragment(), AndroidScreen {
    private val viewModel: DetailsViewModel by activityViewModels()
    private val listViewModel: ListViewModel by activityViewModels()
    private lateinit var view: DetailsScreen
    override var disposables = CompositeDisposable()
    override val ctx: Context by lazy { requireContext() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = DetailsScreen(inflater.context, container)
        return view.root
    }

    override fun onStart() {
        super.onStart()
        linkViewAndLogic(view, viewModel.logic)
    }

    override fun onStop() {
        disposables.dispose()
        disposables = CompositeDisposable()
        super.onStop()
    }

    private fun linkViewAndLogic(view: DetailsScreen, logic: DetailsLogic) {
        view.showBackIcon = !isWideScreen()
        logic.state.observeOnUi { state ->
            view.loading = state == LOADING
            view.errorText = if (state == ERROR) getString(R.string.loading_details_error) else null
            view.listVisible = state == SUCCESS
        }
        logic.result.observeOnUi { list ->
            view.items = list.map { ListItemData(it.type.name, it.type.toTitle(), it.value) }
        }
        logic.title.observeOnUi { view.title = it }
        view.backClick = { listViewModel.logic.onBackPressed() }
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
